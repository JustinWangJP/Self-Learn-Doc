package com.example.springbatchdemo.itemreader.frommulti;

import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName MultiFileWriter
 * @Description MultiFileWriter
 * @Author ZX
 * @Date 2020/6/3
 */
@Component("multiFileWriter")
public class MultiFileWriter implements ItemWriter<Customer> {

    @Override
    public void write(List<? extends Customer> list) throws Exception {
        for (Customer customer:list){
            System.out.println(customer);
        }
    }
}
