package com.mashibing.api.util;

import com.mashibing.api.vo.ResultVo;
import com.mashibing.common.enums.ExceptionEnums;
import com.mashibing.common.exception.ApiException;

public class R {
    public static ResultVo ok()
        {

            ResultVo r = new ResultVo();
            r.setCode(0);
            r.setMsg("接受成功");
            return r;

        }
        public static ResultVo error(Integer code,String msg)
            {
            ResultVo r = new ResultVo();
            r.setCode(code);
            r.setMsg(msg);
            return r;
        }
        public static ResultVo error(ApiException ex)
        {
          ResultVo r = new ResultVo();
          r.setCode(ex.getCode());
          r.setMsg(ex.getMessage());
          return r;
        }
}
