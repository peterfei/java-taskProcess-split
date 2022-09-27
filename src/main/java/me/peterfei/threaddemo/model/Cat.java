package me.peterfei.threaddemo.model;

import lombok.Data;
import org.springframework.stereotype.Service;

/**
 * @program: thread-demo
 * @description:
 * @author: peterfei
 * @create: 2022-09-27 09:17
 **/
@Data
@Service
public class Cat {
    private String catName;
    public Cat setCatName(String name){
        this.catName = name;
        return this;
    }
}
