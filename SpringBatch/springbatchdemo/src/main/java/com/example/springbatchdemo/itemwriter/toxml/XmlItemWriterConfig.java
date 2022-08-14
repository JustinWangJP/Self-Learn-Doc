package com.example.springbatchdemo.itemwriter.toxml;

import com.example.springbatchdemo.pojo.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName XmlItemWriterConfig
 * @Description XmlItemWriterConfig
 * @Author ZX
 * @Date 2020/6/2
 */
@Configuration
public class XmlItemWriterConfig {

    @Bean
    public StaxEventItemWriter<Customer> xmlItemWriter(){
        StaxEventItemWriter writer = new StaxEventItemWriter<Customer>();
        XStreamMarshaller marshaller = new XStreamMarshaller();
        //告诉marshaller把数据转成什么类型
        Map<String,Class> map = new HashMap<>();
        map.put("customer",Customer.class);
        marshaller.setAliases(map);

        writer.setRootTagName("customers");
        writer.setMarshaller(marshaller);

        String path = "F:\\test/cus.xml";
        writer.setResource(new FileSystemResource(path));
        try {
            writer.afterPropertiesSet();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return writer;

    }


}
