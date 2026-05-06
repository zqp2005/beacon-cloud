# Beacon 短信平台系统

企业级分布式短信服务平台，基于 Spring Cloud 微服务架构，通过 CMPP 2.0 协议与运营商网关对接，实现大并发量短信发送、状态报告接收与推送、日志存储等核心功能。

## 核心功能

- **短信发送**：支持单条/批量短信发送，异步削峰处理
- **策略过滤**：黑名单校验、敏感词过滤（DFA）、滑动窗口限流、费用校验
- **通道路由**：多通道权重路由，按运营商（移动/联通/电信/全网通）自动匹配
- **CMPP 协议通信**：基于 Netty 实现与运营商 ISMG 网关的 TCP 长连接通信
- **状态报告**：接收运营商状态报告，通过 HTTP 回调推送给客户，支持延迟重试
- **日志存储**：基于 Elasticsearch 的发送日志存储与查询，按年份自动分片
- **缓存服务**：统一的 Redis 缓存 HTTP 接口，供各模块共享数据

## 技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 2.3.12.RELEASE | 基础框架 |
| Spring Cloud | Hoxton.SR12 | 微服务治理 |
| Spring Cloud Alibaba | 2.2.6.RELEASE | Nacos 服务发现与配置中心 |
| RabbitMQ | — | 异步消息队列，削峰填谷 |
| Redis | — | 数据缓存、黑名单、限流计数 |
| Elasticsearch | 7.6.2 | 日志存储与检索 |
| Netty | 4.1.69.Final | CMPP 协议 TCP 通信 |
| MyBatis | 2.2.2 | 后台管理数据持久化 |
| Shiro | 1.4.0 | 后台管理权限认证 |
| XXL-Job | 2.3.1 | 分布式定时任务调度 |
| Hippo4j | 1.5.0 | 动态线程池管理 |
| Hutool DFA | 5.8.12 | 敏感词过滤 |

## 系统架构

```
客户端请求
    │
    ▼
┌──────────────────────────────────────────────────────────┐
│  beacon-api（接口模块）                                    │
│  参数校验 → 雪花ID生成 → 发布到 RabbitMQ                   │
└────────────────────────┬─────────────────────────────────┘
                         │ sms_pre_send_topic
                         ▼
┌──────────────────────────────────────────────────────────┐
│  beacon-strategy（策略模块）                               │
│  黑名单 → 敏感词 → 限流 → 费用 → 路由选择 → 按通道分发      │
└────────────────────────┬─────────────────────────────────┘
                         │ sms_gateway_topic_{channelId}
                         ▼
┌──────────────────────────────────────────────────────────┐
│  beacon-smsgateway（网关模块）                             │
│  Netty + CMPP 2.0 → 运营商 ISMG → 接收状态报告             │
└──────────┬───────────────────────────────┬───────────────┘
           │                               │
           ▼                               ▼
┌─────────────────────┐       ┌───────────────────────────┐
│  beacon-push         │       │  beacon-search             │
│  HTTP 回调 + 延迟重试 │       │  Elasticsearch 日志存储     │
└─────────────────────┘       └───────────────────────────┘
```

**辅助模块**：
- **beacon-cache**：统一的 Redis 缓存 HTTP 服务
- **beacon-webmaster**：后台管理系统（客户管理、通道管理、日志查询）
- **beacon-monitor**：XXL-Job 定时任务（余额监控、队列监控、邮件告警）
- **beacon-common**：公共模块（共享实体、枚举、工具类）
- **beacon-test**：数据初始化工具

## 模块详解

### beacon-api — 接口模块

对外提供短信发送接口，是整个系统的入口。

- 接口：`POST /sms/single_send`
- 校验链：API Key 校验 → IP 白名单 → 签名校验 → 模板校验 → 费用校验
- 使用雪花算法（SnowFlake）生成全局唯一序列号
- 校验通过后将消息发布到 `sms_pre_send_topic`，立即返回响应

### beacon-strategy — 策略模块

消费 `sms_pre_send_topic` 消息，执行可配置的策略过滤链：

| 过滤器 | 说明 |
|--------|------|
| BlackGlobal | 全局黑名单（Redis 查询） |
| BlackClient | 客户级黑名单 |
| DirtyWord | DFA 敏感词过滤 |
| LimitOneMinute | 每分钟限流（Redis ZSet 滑动窗口） |
| LimitOneHour | 每小时限流 |
| Fee | 费用校验 |
| Route | 路由选择（权重 + 运营商匹配） |

路由选择流程：从 Redis 获取客户绑定的通道列表 → 按权重降序排列 → 匹配运营商 → 选择可用通道 → 发布到 `sms_gateway_topic_{channelId}`。每个通道的队列由 `AmqpAdmin` 动态创建。

### beacon-smsgateway — 网关模块

与运营商 ISMG 网关通信的核心模块。

- 基于 Netty 实现 TCP 长连接，心跳保活
- 实现 CMPP 2.0 协议编解码（CMPP_CONNECT → CMPP_SUBMIT → CMPP_SUBMIT_RESP → CMPP_DELIVER）
- 第一次响应（CMPP_SUBMIT_RESP）：获取运营商 msgId，建立映射关系
- 第二次响应（CMPP_DELIVER）：获取最终状态报告
- 使用 Hippo4j 动态线程池异步处理响应
- 状态报告分别推送到 push 队列和 search 死信队列

### beacon-push — 推送模块

将状态报告通过 HTTP 回调通知客户。

- 从 `StandardReport` 获取客户配置的回调地址
- 使用 RestTemplate 发送 POST 请求
- 失败后进入延迟重试队列：0s → 15s → 30s → 60s → 300s，最多 5 次
- 基于 RabbitMQ 延迟插件实现消息延迟投递

### beacon-search — 搜索模块

负责短信发送日志的 Elasticsearch 存储与查询。

- 消费网关发送的日志消息，写入 ES
- 索引按年份动态创建：`sms_submit_log_{year}`
- 同时处理日志更新（状态报告回填）

### beacon-cache — 缓存模块

统一的 Redis 缓存 HTTP 服务，为其他模块提供数据访问接口。

- 支持 String、Hash、Set、ZSet 等数据结构操作
- 存储客户信息、通道信息、黑名单、限流计数器等

### beacon-webmaster — 后台管理

基于 MyBatis + Shiro 的后台管理系统。

- 客户管理（客户信息、API Key、余额、IP 白名单）
- 通道管理（通道信息、权重、运营商类型）
- 日志查询（通过 OpenFeign 调用 beacon-search）
- Kaptcha 图形验证码

### beacon-monitor — 监控模块

基于 XXL-Job 的分布式定时任务调度。

- 客户余额监控，低于阈值邮件告警
- RabbitMQ 队列消息积压监控
- 邮件告警通知

## 环境依赖

| 组件 | 地址 | 说明 |
|------|------|------|
| Nacos | 192.168.205.1:8848 | 服务注册与配置中心，必须启动 |
| Redis | 192.168.205.130:6379 | 缓存服务 |
| RabbitMQ | 192.168.205.130:5672 | 消息队列 |
| Elasticsearch | 192.168.205.130:9200 | 日志存储 |
| MySQL | 192.168.205.130:3306 | 后台管理数据库 |

## 快速开始

```bash
# 克隆项目
git clone <repo-url>
cd beacon-cloud

# 编译全部模块（跳过测试）
mvn clean install -DskipTests

# 按以下顺序启动各模块（Nacos 必须先启动）
# 1. beacon-cache      — 缓存服务
# 2. beacon-api         — 接口服务
# 3. beacon-strategy    — 策略服务
# 4. beacon-smsgateway  — 网关服务
# 5. beacon-search      — 搜索服务
# 6. beacon-push        — 推送服务
# 7. beacon-webmaster   — 后台管理
# 8. beacon-monitor     — 监控服务
```

启动后访问：`POST http://localhost:{port}/sms/single_send`

## 关键设计

### 校验链模式

`beacon-api` 和 `beacon-strategy` 均采用责任链模式。Spring 自动注入 `Map<String, Filter>` 实现类，通过 Nacos 配置的 `filters` 属性（如 `filters=apikey,ip,sign,template,fee`）决定过滤器执行顺序，支持运行时动态调整。

### 滑动窗口限流

使用 Redis ZSet 实现。每次请求将当前时间戳作为 score 和 member 添加到 ZSet，通过 `zremrangeByScore` 清除窗口外的记录，再通过 `zcard` 统计当前窗口内的请求数。

### 异步削峰

接口模块接收请求后立即返回，后续处理全部通过 RabbitMQ 异步进行。网关模块按通道创建独立队列，实现通道级别的流量隔离。

### 延迟重试

推送模块使用 RabbitMQ 延迟插件实现指数退避重试（0s → 15s → 30s → 60s → 300s），最多重试 5 次后放弃。

## 项目结构

```
beacon-cloud/
├── beacon-api/           # 接口模块
├── beacon-strategy/      # 策略模块
├── beacon-smsgateway/    # 网关模块（CMPP 协议）
├── beacon-push/          # 推送模块（状态报告回调）
├── beacon-search/        # 搜索模块（ES 日志）
├── beacon-cache/         # 缓存模块（Redis 服务）
├── beacon-webmaster/     # 后台管理
├── beacon-monitor/       # 监控模块（XXL-Job）
├── beacon-common/        # 公共模块
├── beacon-test/          # 数据初始化
└── pom.xml               # 父 POM
```
