package com.example.springbatchdemo.itemwriter;

import org.springframework.batch.item.ItemWriter;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * @ClassName MyWriter
 * @Description MyWriter
 * @Author ZX
 * @Date 2020/6/1
 */
@Component("myWriter")
public class MyWriter implements ItemWriter<String> {
    @Override
    public void write(List<? extends String> list) throws Exception {
        //输出一批的数量，chunk的值
        System.out.println(list.size());
        for (String str:list){
            System.out.println(str);
        }
    }
}
