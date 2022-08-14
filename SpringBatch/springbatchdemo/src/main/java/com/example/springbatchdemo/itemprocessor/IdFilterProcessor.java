package com.example.springbatchdemo.itemprocessor;

import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.stereotype.Component;

/**
 * @ClassName IdFilterProcessor
 * @Description IdFilterProcessor
 * @Author ZX
 * @Date 2020/6/4
 */
@Component
public class IdFilterProcessor implements ItemProcessor<Customer, Customer> {
    @Override
    public Customer process(Customer customer) throws Exception {
        if(customer.getId()%2==0)
            return customer;
        else
            return null;
    }
}
