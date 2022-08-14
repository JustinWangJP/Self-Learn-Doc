package com.example.springbatchdemo.itemwriter.toxml;

import com.example.springbatchdemo.listener.MyJobListener;
import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName XmlItemWriterDemo
 * @Description XmlItemWriterDemo
 * @Author ZX
 * @Date 2020/6/1
 */
//@Configuration
//@EnableBatchProcessing
public class XmlItemWriterDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("dbJdbcReader")
    private ItemReader<? extends Customer> dbJdbcReader;
    @Autowired
    @Qualifier("xmlItemWriter")
    private ItemWriter<? super Customer> xmlItemWriter;



    /**
     * @Description: xmlItemWriterDemoJob
     * @Param: []
     * @Return: org.springframework.batch.core.Job
     */
    @Bean
    public Job xmlItemWriterDemoJob() {
        return jobBuilderFactory.get("xmlItemWriterDemoJob")
                .start(xmlItemWriterDemoStep())
                .listener(new MyJobListener())
                .build();
    }

    /**
     * @Description: xmlItemWriterDemoStep
     * @Param: []
     * @Return: org.springframework.batch.core.Step
     */
    @Bean
    public Step xmlItemWriterDemoStep() {

        return stepBuilderFactory.get("xmlItemWriterDemoStep")
                .<Customer,Customer>chunk(5)
                .reader(dbJdbcReader)
                .writer(xmlItemWriter)
                .build();
    }

}
