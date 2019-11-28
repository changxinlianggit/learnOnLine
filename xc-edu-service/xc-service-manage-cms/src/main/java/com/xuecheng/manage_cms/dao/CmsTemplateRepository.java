package com.xuecheng.manage_cms.dao;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import org.springframework.data.mongodb.repository.MongoRepository;
//页面模板的dao
public interface CmsTemplateRepository extends MongoRepository<CmsTemplate,String> {
}
