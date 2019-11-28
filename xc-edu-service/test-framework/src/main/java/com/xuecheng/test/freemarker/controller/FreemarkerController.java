package com.xuecheng.test.freemarker.controller;

import com.xuecheng.test.freemarker.model.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping("/freemarker")
public class FreemarkerController {

    @RequestMapping("/test1")
    public String freemarker(Map<String,Object> map){
        map.put("name","zhangsan");
        //返回模板的名称
        return "test1";
    }
    @RequestMapping("/test2")
    public String freemarker2(Map<String,Object> map){
        map.put("name","zhangsan");
        Student student = new Student();
        student.setAge(19);
        student.setMoney(19.2f);
        student.setName("wangewu");

        Student student2 = new Student();
        student2.setAge(12);
        student2.setMoney(15.23f);
        student2.setName("lisi");

        List<Student> list=new ArrayList<>();
        list.add(student2);
        list.add(student);
        map.put("stus",list);

        Map<String,Student> mapstu=new HashMap<>();
        mapstu.put("stu1",student);
        mapstu.put("stu2",student2);

        map.put("mapstu",mapstu);
        //返回模板的名称
        return "test1";
    }

}
