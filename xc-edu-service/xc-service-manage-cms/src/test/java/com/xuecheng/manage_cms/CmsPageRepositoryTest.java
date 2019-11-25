package com.xuecheng.manage_cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CmsPageRepositoryTest {
    @Autowired
    CmsPageRepository cmsPageRepository;

    /**
     * 分页
     */
    @Test
    public void fingList() {
        PageRequest pageRequest = new PageRequest(1, 10);
        Page<CmsPage> all = cmsPageRepository.findAll(pageRequest);
        System.out.println(all);
    }

    /**
     * 分页
     */
    @Test
    public void fingListAll() {
        //这个方法是设置那个字段要  怎么进行模糊的拼接
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        cmsPage.setPageAliase("轮播");
//        cmsPage.setPageName("index.html");
//        cmsPage.setPageId("5a754adf6abb500ad05688d9");
        //条件的实例，如果是模糊查询的话需要一个条件的匹配器  ExampleMatcher
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        //设置分页参数
        PageRequest pageRequest = new PageRequest(0, 10);
        //参数1：条件的实例，和分页的参数
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageRequest);
        System.out.println(all);
    }

    /**
     * 添加
     */
    @Test
    public void TestInsert() {
        CmsPage cmsPage = new CmsPage();
        cmsPage.setSiteId("s01");
        cmsPage.setTemplateId("t01");
        cmsPage.setPageName("测试页面");
        cmsPage.setPageCreateTime(new Date());
        CmsPage save = cmsPageRepository.save(cmsPage);
        System.out.println(save);
    }

    /**
     * 删除
     */
    @Test
    public void delete() {
        cmsPageRepository.deleteById("5dd8a6a562d85069a89c0714");
    }

}
