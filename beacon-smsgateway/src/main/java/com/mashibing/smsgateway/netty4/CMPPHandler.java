package com.mashibing.smsgateway.netty4;

import com.mashibing.common.constant.SmsConstant;
import com.mashibing.common.model.StandardReport;
import com.mashibing.common.model.StandardSubmit;
import com.mashibing.common.util.CMPP2ResultUtil;
import com.mashibing.common.util.CMPPSubmitRepoMapUtil;
import com.mashibing.smsgateway.netty4.entity.CmppDeliver;
import com.mashibing.smsgateway.netty4.entity.CmppSubmitResp;
import com.mashibing.smsgateway.netty4.utils.MsgUtils;
import com.mashibing.smsgateway.runnable.DeliverRunnable;
import com.mashibing.smsgateway.runnable.SubmitRepoRunnable;
import com.mashibing.smsgateway.util.SpringUtil;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.concurrent.ThreadPoolExecutor;


/**
 * 主要业务 handler,运营商响应信息
 */
@Slf4j
public class CMPPHandler extends SimpleChannelInboundHandler {

    @Override
    protected void channelRead0(ChannelHandlerContext context, Object msg) throws Exception {

        if (msg instanceof CmppSubmitResp) {
            CmppSubmitResp resp = (CmppSubmitResp) msg;
            log.info("-------------接收到短信提交应答-------------");
            log.info("----自增id：" + resp.getSequenceId());
            log.info("----状态：" + resp.getResult());
            log.info("----第一次响应：" + resp.getMsgId());
            // 将封装好的任务扔到线程池中，执行即可
            ThreadPoolExecutor cmppSubmitPool = (ThreadPoolExecutor) SpringUtil.getBeanByName("messageConsumeDynamicExecutor");
            cmppSubmitPool.execute(new SubmitRepoRunnable(resp));
        }

        if (msg instanceof CmppDeliver) {
            CmppDeliver resp = (CmppDeliver) msg;
            // 是否为状态报告 0：非状态报告1：状态报告
            if (resp.getRegistered_Delivery() == 1) {
                // 如果是状态报告的话
                log.info("-------------状态报告---------------");
                log.info("----第二次响应：" + resp.getMsg_Id_DELIVRD());
                log.info("----手机号：" + resp.getDest_terminal_Id());
                log.info("----状态：" + resp.getStat());
                // 提前获取一手~~~
                ThreadPoolExecutor cmppDeliverPool = (ThreadPoolExecutor) SpringUtil.getBeanByName("messageProduceDynamicExecutor");
                cmppDeliverPool.execute(new DeliverRunnable(resp.getMsg_Id_DELIVRD(),resp.getStat()));
            } else {
                //用户回复会打印在这里
                log.info("" + MsgUtils.bytesToLong(resp.getMsg_Id()));
                log.info(resp.getSrc_terminal_Id());
                log.info(resp.getMsg_Content());
            }
        }
    }

}
