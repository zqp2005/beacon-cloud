package com.mashibing.test.mapper;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mashibing.test.client.CacheClient;
import com.mashibing.test.entity.Channel;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Map;

import static org.junit.Assert.*;
@SpringBootTest
@RunWith(SpringRunner.class)
public class ChannelMapperTest {
@Autowired
private ChannelMapper mapper;
@Autowired
private CacheClient cacheClient;
    @Test
    public void findAll() throws JsonProcessingException {
        List<Channel> list=mapper.findAll();
        for (Channel channel : list) {
            ObjectMapper objectMapper = new ObjectMapper();
            Map map = objectMapper.readValue(objectMapper.writeValueAsString(channel), Map.class);
            cacheClient.hmset("channel:"+channel.getId(),map);
        }
    }
}