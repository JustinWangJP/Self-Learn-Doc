package com.example.springbatchdemo.itemwriter.tomulti;

import com.example.springbatchdemo.pojo.Customer;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.batch.item.support.ClassifierCompositeItemWriter;
import org.springframework.batch.item.support.CompositeItemWriter;
import org.springframework.batch.item.xml.StaxEventItemWriter;
import org.springframework.classify.Classifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.FileSystemResource;
import org.springframework.oxm.xstream.XStreamMarshaller;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName MultiFIleWriterConfig
 * @Description MultiFIleWriterConfig
 * @Author ZX
 * @Date 2020/6/2
 */
@Configuration
public class MultiFIleWriterConfig {

    /**
     * @Description: fileWriter 输出数据到txt文件
     * @Param: []
     * @Return: org.springframework.batch.item.file.FlatFileItemWriter<com.example.springbatchdemo.pojo.Customer>
     */
    @Bean
    public FlatFileItemWriter<Customer> jsonFileWriter(){
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

    /**
     * @Description: xmlWriter 输出数据到xml文件
     * @Param: []
     * @Return: org.springframework.batch.item.xml.StaxEventItemWriter<com.example.springbatchdemo.pojo.Customer>
     */
    @Bean
    public StaxEventItemWriter<Customer> xmlWriter(){
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


    /**
     * @Description: multiFileItemWriter 调用输出到单个文件操作来实现输出数据到多个文件
     * @Param: []
     * @Return: org.springframework.batch.item.support.CompositeItemWriter<com.example.springbatchdemo.pojo.Customer>
     */
//    @Bean
//    public CompositeItemWriter<Customer> multiFileItemWriter(){
//        CompositeItemWriter<Customer> writer = new CompositeItemWriter<Customer>();
//        //输出到两个不同的文件中
//        writer.setDelegates(Arrays.asList(jsonFileWriter(),xmlWriter()));
//        try {
//            writer.afterPropertiesSet();
//        } catch (Exception e) {
//            e.printStackTrace();
//        }
//        return writer;
//    }


    /**
     * @Description: multiFileItemWriter  按照某种条件对数据进行分类存储不同文件
     * 注意：ClassifierCompositeItemWriter没有实现ItemStream
     * @Param: []
     * @Return: org.springframework.batch.item.support.ClassifierCompositeItemWriter<com.example.springbatchdemo.pojo.Customer>
     */
    @Bean
    public ClassifierCompositeItemWriter<Customer> multiFileItemWriter(){
        ClassifierCompositeItemWriter<Customer> writer = new ClassifierCompositeItemWriter<Customer>();
        writer.setClassifier(new Classifier<Customer, ItemWriter<? super Customer>>() {
            @Override
            /**
             * @Description: classify 分类，比如按照年龄分成两个文件
             * @Param: [customer]
             * @Return: org.springframework.batch.item.ItemWriter<? super com.example.springbatchdemo.pojo.Customer>
             */
            public ItemWriter<? super Customer> classify(Customer customer) {
                //按照Customer的id进行分类
                ItemWriter<Customer> writer1 = customer.getId()%2==0?jsonFileWriter():xmlWriter();
                return writer1;
            }
        });
        return writer;

    }


}
