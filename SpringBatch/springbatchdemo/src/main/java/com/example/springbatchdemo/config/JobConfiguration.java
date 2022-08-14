package com.example.springbatchdemo.config;

import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.StepContribution;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @ClassName JobConfiguration
 * @Description JobConfiguration
 * @Author ZX
 * @Date 2020/5/28
 */
//@Configuration
//@EnableBatchProcessing
public class JobConfiguration {
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


    /**
     * @Description: 创建任务对象
     * @Param: []
     * @Return: org.springframework.batch.core.Job
     */
    @Bean
    public Job helloWorldJob() {
        //job名字//开始step
        return jobBuilderFactory.get("helloWorldJob")
                .start(step1())
                .build();
    }

    @Bean
    public Step step1() {
        /**
         * @Description: step1 step的名字，tasklet执行任务 可以用chunk
         * @Param: []
         * @Return: org.springframework.batch.core.Step
         */
        return stepBuilderFactory.get("step1")
                .tasklet(new Tasklet() {
                    @Override
                    /**
                     * @Description: RepeatStatus 状态值，一步步来，一个step结束开始下一个
                     * @Param: [stepContribution, chunkContext]
                     * @Return: org.springframework.batch.repeat.RepeatStatus
                     */
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        System.out.println("He11o World!");
                        return RepeatStatus.FINISHED;
                    }

                }).build();
    }
}



