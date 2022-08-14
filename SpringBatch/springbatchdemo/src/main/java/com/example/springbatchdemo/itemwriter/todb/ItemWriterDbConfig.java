package com.example.springbatchdemo.itemwriter.todb;

import com.example.springbatchdemo.pojo.Customer;
import org.springframework.batch.item.database.BeanPropertyItemSqlParameterSourceProvider;
import org.springframework.batch.item.database.JdbcBatchItemWriter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.sql.DataSource;

/**
 * @ClassName ItemWriterDbConfig
 * @Description ItemWriterDbConfig
 * @Author ZX
 * @Date 2020/6/1
 */
@Configuration
public class ItemWriterDbConfig {
    @Autowired
    private DataSource dataSource;


    /**
     * @Description: itemWriterDb
     * @Param: []
     * @Return: org.springframework.batch.item.database.JdbcBatchItemWriter<com.example.springbatchdemo.pojo.Customer>
     */
    @Bean
    public JdbcBatchItemWriter<Customer> itemWriterDb(){
        JdbcBatchItemWriter writer = new JdbcBatchItemWriter<Customer>();
        writer.setDataSource(dataSource);
        writer.setSql("insert into customer(id,firstName,lastName,birthday) values" +
                    "(:id,:firstName,:lastName,:birthday)");
        //将Customer中对应属性的值与Sql语句中的四个值进行替换
        writer.setItemSqlParameterSourceProvider(new BeanPropertyItemSqlParameterSourceProvider<Customer>());
        return writer;
    }
}
