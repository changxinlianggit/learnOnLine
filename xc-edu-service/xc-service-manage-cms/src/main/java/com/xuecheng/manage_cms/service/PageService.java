package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        //设置条件实例
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        //站点id
        if(StringUtils.isNotEmpty(queryPageRequest.getSiteId())){
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //模板id
        if(StringUtils.isNotEmpty(queryPageRequest.getTemplateId())){
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //页面别名
        if(StringUtils.isNotEmpty(queryPageRequest.getPageAliase())){
            cmsPage.setPageAliase(queryPageRequest.getPageAliase());
        }
        if (page <= 0) {
            page = 1;
        }
        page = page - 1;//因为MongoDB需要分页查询必须从0开始
        if (size <= 0) {
            size = 10;
        }
        //设置条件查询的条件
        Example<CmsPage> example=Example.of(cmsPage,exampleMatcher);
        //设置分页参数
        PageRequest pageRequest = new PageRequest(page, size);
        //调用dao
        Page<CmsPage> all = cmsPageRepository.findAll(example,pageRequest);
        QueryResult<CmsPage> pageQueryResult=new QueryResult<>();
        //设置本页数据
        pageQueryResult.setList(all.getContent());
        //设置总页数
        pageQueryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS,pageQueryResult);
    }
}
