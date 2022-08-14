package com.example.springbatchdemo.itemwriter.todb;

import com.example.springbatchdemo.listener.MyJobListener;
import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemReader;
import org.springframework.batch.item.file.mapping.DefaultLineMapper;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.BindException;

/**
 * @ClassName DbItemWriterDemo
 * @Description DbItemWriterDemo
 * @Author ZX
 * @Date 2020/6/1
 */
//@Configuration
//@EnableBatchProcessing
public class ItemWriterDbDemo {
    @Autowired
    private JobBuilderFactory jobBuilderFactory;
    @Autowired
    private StepBuilderFactory stepBuilderFactory;
    @Autowired
    @Qualifier("itemWriterDb")
    private ItemWriter<? super Customer> itemWriterDb;

    @Autowired
    @Qualifier("fileItemReaderDemoReader")
    private ItemReader<? extends Customer> flatFileReader;




    /**
     * @Description: itemWriterDbDemoJob
     * @Param: []
     * @Return: org.springframework.batch.core.Job
     */
    @Bean
    public Job itemWriterDbDemoJob() {
        return jobBuilderFactory.get("itemWriterDbDemoJob")
                .start(itemWriterDbDemoStep())
                .listener(new MyJobListener())
                .build();
    }


    /**
     * @Description: itemWriterDbDemoStep
     * @Param: []
     * @Return: org.springframework.batch.core.Step
     */
    @Bean
    public Step itemWriterDbDemoStep() {

        return stepBuilderFactory.get("itemWriterDbDemoStep")
                .<Customer,Customer>chunk(5)
                .reader(flatFileReader)
                .writer(itemWriterDb)
                .build();
    }




}
