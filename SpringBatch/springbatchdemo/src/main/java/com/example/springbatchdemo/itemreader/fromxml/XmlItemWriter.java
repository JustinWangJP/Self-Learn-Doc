package com.example.springbatchdemo.itemreader.fromxml;

import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName FileFileItemWritter
 * @Description FileFileItemWritter
 * @Author ZX
 * @Date 2020/5/30
 */
@Component("xmlFileWriter")
public class XmlItemWriter implements ItemWriter<Customer> {
    @Override
    public void write(List<? extends Customer> list) throws Exception {
        for (Customer customer:list){
            System.out.println(customer);
        }
    }
}
