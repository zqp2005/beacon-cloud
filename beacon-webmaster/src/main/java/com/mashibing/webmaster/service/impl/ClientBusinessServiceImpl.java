package com.mashibing.webmaster.service.impl;

import com.mashibing.webmaster.entity.ClientBusiness;
import com.mashibing.webmaster.entity.ClientBusinessExample;
import com.mashibing.webmaster.mapper.ClientBusinessMapper;
import com.mashibing.webmaster.service.ClientBusinessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author zjw
 * @description
 */
@Service
public class ClientBusinessServiceImpl implements ClientBusinessService {

    @Resource
    private ClientBusinessMapper clientBusinessMapper;

    @Override
    public List<ClientBusiness> findAll() {
        List<ClientBusiness> list = clientBusinessMapper.selectByExample(null);
        return list;
    }

    @Override
    public List<ClientBusiness> findByUserId(Integer userId) {
        ClientBusinessExample example = new ClientBusinessExample();
        example.createCriteria().andExtend1EqualTo(userId + "");
        List<ClientBusiness> list = clientBusinessMapper.selectByExample(example);
        return list;
    }
}
