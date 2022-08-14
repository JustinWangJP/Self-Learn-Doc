package com.example.springbatchdemo.itemwriter;

import com.example.springbatchdemo.itemreader.MyReader;
import com.example.springbatchdemo.listener.MyJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ItemWriterDemo
 * @Description ItemWriterDemo
 * @Author ZX
 * @Date 2020/6/1
 */
//@Configuration
//@EnableBatchProcessing
public class ItemWriterDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("myWriter")
    private ItemWriter<? super String> myWriter;


    /**
     * @Description: itemWriterDemoJob
     * @Param: []
     * @Return: org.springframework.batch.core.Job
     */
    @Bean
    public Job itemWriterDemoJob() {
        return jobBuilderFactory.get("itemWriterDemoJob")
                .start(itemWriterDemoStep())
                .listener(new MyJobListener())
                .build();
    }


    /**
     * @Description: itemWriterDemoStep
     * @Param: []
     * @Return: org.springframework.batch.core.Step
     */
    @Bean
    public Step itemWriterDemoStep() {
        return stepBuilderFactory.get("itemWriterDemoStep")
                .<String, String>chunk(2)
                .reader(myReader())
                .writer(myWriter).build();
    }



    /**
     * @Description: myReader
     * @Param: []
     * @Return: org.springframework.batch.item.ItemReader<java.lang.String>
     */
    @Bean
    public ItemReader<String> myReader() {
        List<String> items = new ArrayList<>();
        for (int i = 1; i <= 50; i++) {
            items.add("java" + i);
        }
        return new ListItemReader<String>(items);
    }
}
