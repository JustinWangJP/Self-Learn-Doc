package com.example.springbatchdemo.itemreader.fromfile;

import com.example.springbatchdemo.pojo.Customer;
import com.example.springbatchdemo.pojo.User;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName FileFileItemWritter
 * @Description FileFileItemWritter
 * @Author ZX
 * @Date 2020/5/30
 */
@Component("flatFileItemWriter")
public class FlatFileItemWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> list) throws Exception {
        for (Customer customer:list){
            System.out.println(customer);
        }
    }
}
