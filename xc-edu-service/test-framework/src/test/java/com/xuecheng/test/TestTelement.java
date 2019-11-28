package com.xuecheng.test;

import ch.qos.logback.core.db.ConnectionSourceBase;
import com.xuecheng.test.freemarker.model.Student;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import org.apache.commons.io.IOUtils;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import sun.rmi.runtime.NewThreadAction;

import javax.swing.*;
import java.io.*;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SpringBootTest
@RunWith(SpringRunner.class)
public class TestTelement {

    /**
     * 页面静态化测试
     *
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void test001() throws IOException, TemplateException {
        //创建配置类
        Configuration configuration = new Configuration(Configuration.getVersion());
        //获取模板路径
        String path = this.getClass().getResource("/").getPath();
        configuration.setDirectoryForTemplateLoading(new File(path + "/templates/"));
        //设置字符集
        configuration.setDefaultEncoding("utf-8");
        //加载模板
        Template template = configuration.getTemplate("test1.ftl");
        //加载数据模型
        Map map = getMap();
        String s = FreeMarkerTemplateUtils.processTemplateIntoString(template, map);
        InputStream inputStream = IOUtils.toInputStream(s);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("G:/test.html"));
        int copy = IOUtils.copy(inputStream, fileOutputStream);
        inputStream.close();
        fileOutputStream.close();


    }

    /**
     * 基本字符串生成模板
     *
     * @throws IOException
     * @throws TemplateException
     */
    @Test
    public void test002() throws IOException, TemplateException {
        Configuration configuration = new Configuration(Configuration.getVersion());
        //模板内容，这里测试时使用简单的字符串作为模板     
        String templateString = "" +
                "<html>\n" +
                "    <head></head>\n" +
                "    <body>\n" +
                "    名称：${name}\n" +
                "    </body>\n" + "</html>";
        //创建模板加载器
        StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
        stringTemplateLoader.putTemplate("telement", templateString);
        configuration.setTemplateLoader(stringTemplateLoader);
        //得到模板
        Template telement = configuration.getTemplate("telement");
        //加载数据模型
        Map map = getMap();
        //实现模板和数据进行结合，就是静态化
        String s = FreeMarkerTemplateUtils.processTemplateIntoString(telement, map);
        InputStream inputStream = IOUtils.toInputStream(s);
        FileOutputStream fileOutputStream = new FileOutputStream(new File("G:/test.html"));
        int copy = IOUtils.copy(inputStream, fileOutputStream);
        inputStream.close();
        fileOutputStream.close();

    }

    public Map getMap() {
        Map map = new HashMap();
        map.put("name", "zhangsan");
        Student student = new Student();
        student.setAge(19);
        student.setMoney(19.2f);
        student.setName("wangewu");

        Student student2 = new Student();
        student2.setAge(12);
        student2.setMoney(15.23f);
        student2.setName("lisi");

        List<Student> list = new ArrayList<>();
        list.add(student2);
        list.add(student);
        map.put("stus", list);

        Map<String, Student> mapstu = new HashMap<>();
        mapstu.put("stu1", student);
        mapstu.put("stu2", student2);

        map.put("mapstu", mapstu);
        return map;
    }
}
