package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.SysDictionaryControllerApi;
import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.service.SysDictionaryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**、
 * 这个类控制的是MongoDB缓存数据库中的sys_Dictionary这张表
 */
@RestController
@RequestMapping("/sys/dictionary")
public class SysDictionaryController implements SysDictionaryControllerApi {

    @Autowired
    SysDictionaryService sysDictionaryService;

    //根据type查询课程等级
    @GetMapping("/get/{type}")
    public SysDictionary getsysDictionary(@PathVariable("type") String type) {
        return sysDictionaryService.getBytype(type);
    }
}
