package com.example.springbatchdemo.itemreader;

import com.example.springbatchdemo.listener.MyJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ItemReaderDemo
 * @Description ItemReaderDemo
 * @Author ZX
 * @Date 2020/5/30
 */
//@Configuration
//@EnableBatchProcessing
public class ItemReaderDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;


    /**
     * @Description: itemReaderDemoJob
     * @Param: []
     * @Return: org.springframework.batch.core.Job
     */
    @Bean
    public Job itemReaderDemoJob() {
        return jobBuilderFactory.get("itemReaderDemoJob")
                .start(itemReaderDemoStep())
                .listener(new MyJobListener())
                .build();
    }


    /**
     * @Description: itemReaderDemoStep
     * @Param: []
     * @Return: org.springframework.batch.core.Step
     */
    @Bean
    public Step itemReaderDemoStep() {
        return stepBuilderFactory.get("itemReaderDemoStep")
                .chunk(2)
                .reader(itemReaderDemoRead())
                .writer(list->{
                    for (Object item:list){
                        System.out.println(item+"...");
                    }
                }).build();
    }


    /**
     * @Description: itemReaderDemoRead
     * 自定义itemReader
     * @Param: []
     * @Return: MyReader
     */
    @Bean
    public MyReader itemReaderDemoRead() {
        List<String> data = Arrays.asList("鼠","牛","虎","兔");
        return new MyReader(data);
    }

}
