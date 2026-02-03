package com.mashibing.strategy.filter.impl;

import com.mashibing.common.model.StandardSubmit;
import com.mashibing.strategy.filter.StrategyFilter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service(value = "black")
@Slf4j
public class BlackStrategyFilter implements StrategyFilter {
    @Override
    public void strategy(StandardSubmit submit) {
        log.info("【策略模块-黑名单校验】   校验ing…………");
    }
}