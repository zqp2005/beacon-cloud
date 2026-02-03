package com.mashibing.test.mapper;

import com.mashibing.test.client.CacheClient;
import com.mashibing.test.entity.MobileBlack;
import com.mashibing.test.entity.MobileTransfer;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

@SpringBootTest
@RunWith(SpringRunner.class)
public class MobileTransferMapperTest {

    @Autowired
    private MobileTransferMapper mapper;

    @Autowired
    private CacheClient cacheClient;


    @Test
    public void findAll() {
        List<MobileTransfer> list = mapper.findAll();

        for (MobileTransfer mobileTransfer : list) {
            cacheClient.set("transfer:" + mobileTransfer.getTransferNumber(),mobileTransfer.getNowIsp());
        }
    }
}