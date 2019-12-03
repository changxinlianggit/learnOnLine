package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.system.SysDictionary;
import com.xuecheng.manage_cms.dao.SysDictionaryDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SysDictionaryService {

    @Autowired
    private SysDictionaryDao sysDictionaryDao;

    //查询课程的等级
    public SysDictionary getBytype(String type){
        SysDictionary byDType = sysDictionaryDao.findByDType(type);
        return byDType;
    }
}
