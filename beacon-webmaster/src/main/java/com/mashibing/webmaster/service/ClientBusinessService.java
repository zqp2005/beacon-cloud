package com.mashibing.webmaster.service;

import java.util.List;
import com.mashibing.webmaster.entity.ClientBusiness;
/**
 * @author zjw
 * @description
 */
public interface ClientBusinessService {
    /**
     * 查询全部客户信息
     * @return
     */
    List<ClientBusiness> findAll();

    /**
     * 根据用户id查询客户信息
     * @param userId
     * @return
     */
    List<ClientBusiness> findByUserId(Integer userId);
}
