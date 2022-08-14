package com.example.springbatchdemo.itemreader.fromdb;

import com.example.springbatchdemo.listener.MyJobListener;
import com.example.springbatchdemo.pojo.User;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepScope;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.database.JdbcPagingItemReader;
import org.springframework.batch.item.database.Order;
import org.springframework.batch.item.database.support.MySqlPagingQueryProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.RowMapper;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

/**
 * @ClassName ItemDbDemo
 * @Description ItemDbDemo
 * @Author ZX
 * @Date 2020/5/30
 */
//@Configuration
//@EnableBatchProcessing
public class ItemReaderDbDemo {

    @Autowired
    private JobBuilderFactory jobBuilderFactory;

    @Autowired
    private StepBuilderFactory stepBuilderFactory;

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("dbJdbcWriter")
    private ItemWriter<? super User> dbJdbcWriter;

    @Bean
    public Job ItemReaderDbDemoJob(){
        return jobBuilderFactory.get("itemReaderDbDemoJob")
                .start(itemReaderDbStep())
                .listener(new MyJobListener())
                .build();
    }

    @Bean
    public Step itemReaderDbStep() {
        return stepBuilderFactory.get("itemReaderDemoStep")
                .<User, User>chunk(2)
                .reader(dbJdbcReader())
                .writer(dbJdbcWriter)
                .build();
    }


    /**
     * @Description: dbJdbcReader 从数据库中读取数据
     * @Param: []
     * @Return: org.springframework.batch.item.database.JdbcPagingItemReader<com.example.springbatchdemo.pojo.User>
     */
    @Bean
    @StepScope
    public JdbcPagingItemReader<User> dbJdbcReader() {
        JdbcPagingItemReader<User> reader = new JdbcPagingItemReader<User>();
        reader.setDataSource(dataSource);
        //设置读取缓存
        reader.setFetchSize(2);
        //把读取到的记录转换成User对象
        reader.setRowMapper(new RowMapper<User>() {
            /**
             * @Description: 结果集的映射
             * @Param: [resultSet, i]
             * @Return: com.example.springbootjdbc.pojo.Users
             */
            @Override
            public User mapRow(ResultSet resultSet, int rowNum) throws SQLException {
                User users = new User();
                users.setId(resultSet.getInt("id"));
                users.setAge(resultSet.getInt("age"));
                users.setPhone(resultSet.getInt("phone"));
                users.setUsername(resultSet.getString("username"));
                users.setEmail(resultSet.getString("email"));
                return users;
            }
        });
        //指定sq1语句
        MySqlPagingQueryProvider provider =new MySqlPagingQueryProvider () ;
        provider.setSelectClause ("id,username,age,phone,email") ;
        provider.setFromClause ("from users") ;
        //指定根据哪个字段进行排序
        Map<String, Order> sort = new HashMap<>(1) ;
        sort.put("id", Order.ASCENDING);
        provider.setSortKeys(sort);
        reader.setQueryProvider(provider);
        return reader ;
    }

}
