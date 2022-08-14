package com.example.springbatchdemo.itemreader;

import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.stereotype.Component;

import java.util.Iterator;
import java.util.List;

/**
 * @ClassName MyReader
 * 自定义ItemReader
 * @Description MyReader
 * @Author ZX
 * @Date 2020/5/30
 */
public class MyReader implements ItemReader<String> {

    private Iterator<String> iterator;

    public MyReader(List<String> list){
        this.iterator=list.iterator();
    }

    @Override
    public String read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
        //默认一个一个读数据
        if(iterator.hasNext()){
            return this.iterator.next();
        }else {
            return null;
        }
    }
}
