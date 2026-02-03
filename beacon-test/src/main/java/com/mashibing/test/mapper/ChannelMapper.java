package com.mashibing.test.mapper;

import com.mashibing.test.entity.Channel;
import com.mashibing.test.entity.ClientBalance;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public interface ChannelMapper {
    @Select("select * from channel where is_delete = 0")
    List<Channel> findAll();
}
