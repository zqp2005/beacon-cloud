package com.mashibing.common.util;

import com.mashibing.common.enums.MobileOperatorEnum;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zjw
 * @description
 */
public class OperatorUtil {

    private static Map<String,Integer> operators = new HashMap<>();

    static{
        MobileOperatorEnum[] operatorEnums = MobileOperatorEnum.values();
        for (MobileOperatorEnum operatorEnum : operatorEnums) {
            operators.put(operatorEnum.getOperatorName(),operatorEnum.getOperatorId());
        }
    }

    /**
     * 通过运营商名称获取运营商Id
     * @param operatorName
     * @return
     */
    public static Integer getOperatorIdByOperatorName(String operatorName){
        return operators.get(operatorName);
    }


}
