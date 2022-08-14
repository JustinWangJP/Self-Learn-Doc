package com.example.springbatchdemo.itemreader.fromxml;

import com.example.springbatchdemo.listener.MyJobListener;
import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.batch.item.xml.StaxEventItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.oxm.xstream.XStreamMarshaller;
import org.springframework.validation.BindException;

import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName FileItemReaderDemo 从文件中读取数据
 * @Description FileItemReaderDemo
 * @Author ZX
 * @Date 2020/5/30
 */
//@Configuration
//@EnableBatchProcessing
public class XmlItemReaderDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("xmlFileWriter")
    private ItemWriter<? super Customer> xmlFileWriter;


    /**
     * @Description: xmlItemReaderDemoJob
     * @Param: []
     * @Return: org.springframework.batch.core.Job
     */
    @Bean
    public Job xmlItemReaderDemoJob() {
        return jobBuilderFactory.get("xmlItemReaderDemoJob")
                .start(xmlItemReaderDemoStep())
                .listener(new MyJobListener())
                .build();
    }


    /**
     * @Description: xmlItemReaderDemoStep
     * @Param: []
     * @Return: org.springframework.batch.core.Step
     */
    @Bean
    public Step xmlItemReaderDemoStep() {

        return stepBuilderFactory.get("xmlItemReaderDemoStep")
                .<Customer,Customer>chunk(5)
                .reader(xmlFileReader())
                .writer(xmlFileWriter)
                .build();
    }



    /**
     * @Description: xmlFileReader 文件读取
     * @Param: []
     * @Return: org.springframework.batch.item.file.FlatFileItemReader<com.example.springbatchdemo.pojo.Customer>
     */
    @Bean
    @StepScope
    public StaxEventItemReader<Customer> xmlFileReader() {
        StaxEventItemReader<Customer> reader = new StaxEventItemReader<Customer>();
        //指定文件位置
        reader.setResource(new ClassPathResource("customer.xml"));
        //跳过第一行
        reader.setFragmentRootElementName("customer");
        //把xml转成对象
        XStreamMarshaller unmarshaller = new XStreamMarshaller();
        //告诉unmarshaller把xml转成什么类型
        Map<String,Class> map = new HashMap<>();
        map.put("customer",Customer.class);
        unmarshaller.setAliases(map);

        reader.setUnmarshaller(unmarshaller);
        return reader;

    }
}
