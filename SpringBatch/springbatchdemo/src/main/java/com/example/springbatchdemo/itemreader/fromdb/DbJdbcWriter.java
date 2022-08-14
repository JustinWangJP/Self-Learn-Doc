package com.example.springbatchdemo.itemreader.fromdb;

import com.example.springbatchdemo.pojo.User;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName DbJdbcWriter
 * @Description DbJdbcWriter
 * @Author ZX
 * @Date 2020/5/30
 */
@Component("dbJdbcWriter")
public class DbJdbcWriter implements ItemWriter<User>{


    @Override
    public void write(List<? extends User> list) throws Exception {
        for (User user:list){
            System.out.println(user);
        }
    }
}
