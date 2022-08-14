package com.example.springbatchdemo.itemwriter.tomulti;

import com.example.springbatchdemo.listener.MyJobListener;
import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemStreamWriter;
import org.springframework.batch.item.ItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName MultiFileItemWriterDemo
 * @Description MultiFileItemWriterDemo
 * @Author ZX
 * @Date 2020/6/2
 */
//@Configuration
//@EnableBatchProcessing
public class MultiFileItemWriterDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    @Qualifier("dbJdbcReader")
    private ItemReader<? extends Customer> dbJdbcReader;
    @Autowired
    @Qualifier("multiFileItemWriter")
    private ItemWriter<? super Customer> multiFileItemWriter;


    //分类传数据给文件的ClassifierCompositeItemWriter没有实现ItemStream
    @Autowired
    @Qualifier("jsonFileWriter")
    private ItemStreamWriter<? extends Customer> jsonFileWriter;
    @Autowired
    @Qualifier("xmlWriter")
    private ItemStreamWriter<? extends Customer> xmlWriter;



    /**
     * @Description: multiFileItemWriterDemoJob
     * @Param: []
     * @Return: org.springframework.batch.core.Job
     */
    @Bean
    public Job multiFileItemWriterDemoJob2() {
        return jobBuilderFactory.get("multiFileItemWriterDemoJob2")
                .start(multiFileItemWriterDemoStep())
                .listener(new MyJobListener())
                .build();
    }

    /**
     * @Description: multiFileItemWriterDemoStep
     * @Param: []
     * @Return: org.springframework.batch.core.Step
     */
    @Bean
    public Step multiFileItemWriterDemoStep() {
        return stepBuilderFactory.get("multiFileItemWriterDemoStep")
                .<Customer,Customer>chunk(5)
                .reader(dbJdbcReader)
                .writer(multiFileItemWriter)
                //ClassifierCompositeItemWriter没有实现ItemStream
                .stream(jsonFileWriter)
                .stream(xmlWriter)
                .build();
    }
}
