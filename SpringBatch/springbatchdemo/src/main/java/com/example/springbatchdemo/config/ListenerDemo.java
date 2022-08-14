package com.example.springbatchdemo.config;

import com.example.springbatchdemo.listener.MyChunkListener;
import com.example.springbatchdemo.listener.MyJobListener;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.support.ListItemReader;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Arrays;
import java.util.List;

/**
 * @ClassName ListenerDemo
 * @Description ListenerDemo
 * @Author ZX
 * @Date 2020/5/30
 */
//@Configuration
//@EnableBatchProcessing
public class ListenerDemo {

    /**
     * @Description: 注入创建任务对象的对象
     * @Param:
     * @Return:
     */
    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    /**
     * @Description: //任务的执行由Step决定,注入创建Step对象的对象
     * @Param:
     * @Return:
     */
    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Bean
    public Job listenerJob() {
        return jobBuilderFactory.get("listenerJob")
                .start(step1())
                .listener(new MyJobListener())
                .build();


    }

    /**
     * @Description: step1 Chunk使用方式
     * @Param: []
     * @Return: org.springframework.batch.core.Step
     */
    @Bean
    public Step step1() {
        return stepBuilderFactory.get("listenerStep1")
                //数据的读取，<String,String>规定读取和输出的数据类型每读完2个数据进行一次输出处理
                .<String,String>chunk(2)
                //容错
                .faultTolerant()
                .listener(new MyChunkListener())
                //数据的读取
                .reader(read())
                //数据的写入/输出
                .writer(write())
                .build();
    }


    /**
     * @Description: write
     * @Param: []
     * @Return: org.springframework.batch.item.ItemWriter<java.lang.String>
     */
    @Bean
    public ItemWriter<String> write() {
        return new ItemWriter<String>() {
            @Override
            public void write(List<? extends String> list) throws Exception {
                for(String item:list){
                    System.out.println(item);
                }
            }
        };
    }


    /**
     * @Description: read
     * @Param: []
     * @Return: org.springframework.batch.item.ItemReader<java.lang.String>
     */
    @Bean
    public ItemReader<String> read() {
        return new ListItemReader<>(Arrays.asList("java","spring","mybatis"));
    }
}
