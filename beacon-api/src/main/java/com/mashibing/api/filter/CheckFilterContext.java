package com.mashibing.api.filter;

import com.mashibing.common.model.StandardSubmit;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@RefreshScope
public class CheckFilterContext {
    @Autowired
    private Map<String, CheckFilter> checkFiltersMap;
    @Value("${filters:apikey,ip,sign,template,fee}")
    private String filters;
    public void check(StandardSubmit submit)
    {
 String[] filterArray =filters.split(",");
 for(String filter:filterArray)
     checkFiltersMap.get(filter).check(submit);
    }


}
