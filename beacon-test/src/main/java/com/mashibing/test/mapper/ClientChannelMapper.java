package com.mashibing.test.mapper;

import com.mashibing.test.entity.Channel;
import com.mashibing.test.entity.ClientChannel;
import org.apache.ibatis.annotations.Select;

import java.util.List;

public interface ClientChannelMapper {
    @Select("select * from client_channel where is_delete = 0")
    List<ClientChannel> findAll();
}
