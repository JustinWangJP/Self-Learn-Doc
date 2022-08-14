package com.example.springbatchdemo.itemwriter.tofile;

import com.example.springbatchdemo.pojo.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;

/**
 * @ClassName FIleItemWriter
 * @Description FIleItemWriter
 * @Author ZX
 * @Date 2020/6/2
 */
@Configuration
public class FIleItemWriterConfig {


    /**
     * @Description: fileItemWriter 向文件输出数据，覆盖原先数据
     * @Param: []
     * @Return: org.springframework.batch.item.file.FlatFileItemWriter<com.example.springbatchdemo.pojo.Customer>
     */
    @Bean
    public FlatFileItemWriter<Customer> fileItemWriter(){
        //把Customer对象转成字符串输出到文件
        FlatFileItemWriter<Customer> writer = new FlatFileItemWriter<Customer>();
        String path="F:\\test/customer.txt";
        writer.setResource(new FileSystemResource(path));
        //把Customer对象转成字符串
        writer.setLineAggregator(new LineAggregator<Customer>() {
            ObjectMapper mapper = new ObjectMapper();
            @Override
            public String aggregate(Customer customer) {
               String str = null;
                try {
                    str=mapper.writeValueAsString(customer);
                } catch (JsonProcessingException e) {
                    e.printStackTrace();
                }

                return str;
            }
        });
        try {
            writer.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;

    }
}
