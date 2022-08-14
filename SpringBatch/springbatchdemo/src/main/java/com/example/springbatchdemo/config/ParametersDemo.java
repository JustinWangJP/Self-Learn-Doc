package com.example.springbatchdemo.config;

import com.example.springbatchdemo.listener.MyJobListener;
import org.springframework.batch.core.*;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.scope.context.ChunkContext;
import org.springframework.batch.core.step.tasklet.Tasklet;
import org.springframework.batch.repeat.RepeatStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Map;

/**
 * @ClassName ParametersDemo 实现Step监听器
 * @Description ParametersDemo
 * @Author ZX
 * @Date 2020/5/30
 */
//@Configuration
//@EnableBatchProcessing
public class ParametersDemo implements StepExecutionListener {
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

    private Map<String,JobParameter> parameters;

    @Bean
    public Job parameterJob() {
        return jobBuilderFactory.get("parameterJob")
                .start(parameterStep())
                .listener(new MyJobListener())
                .build();


    }


    /**
     * @Description: parameterStep
     * Job执行的是Step，Job使用的数据肯定是在step中使用，只需给Step传递数据。
     * 使用监听，使用Step级别的监听来传递数据
     * @Param: []
     * @Return: org.springframework.batch.core.Step
     */
    @Bean
    public Step parameterStep() {
        return stepBuilderFactory.get("parameterStep")
                .listener(this)
                .tasklet(new Tasklet() {
                    @Override
                    public RepeatStatus execute(StepContribution stepContribution, ChunkContext chunkContext) throws Exception {
                        //输出接收到的参数的值
                        System.out.println(parameters.get("info"));
                        return RepeatStatus.FINISHED;
                    }
                }).build();
    }

    @Override
    public void beforeStep(StepExecution stepExecution) {
        parameters = stepExecution.getJobParameters().getParameters();
    }

    @Override
    public ExitStatus afterStep(StepExecution stepExecution) {
        return null;
    }
}
