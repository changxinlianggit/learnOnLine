package com.xuecheng.manage_cms.service;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PageService {

    @Autowired
    private CmsPageRepository cmsPageRepository;

    /**
     * 分页查询
     *
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    public QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest) {
        if (queryPageRequest == null) {
            queryPageRequest = new QueryPageRequest();
        }
        //设置条件实例
        ExampleMatcher exampleMatcher = ExampleMatcher.matching()
                .withMatcher("pageAliase", ExampleMatcher.GenericPropertyMatchers.contains());
        CmsPage cmsPage = new CmsPage();
        //站点id
        if (StringUtils.isNotEmpty(queryPageRequest.getSiteId())) {
            cmsPage.setSiteId(queryPageRequest.getSiteId());
        }
        //模板id
        if (StringUtils.isNotEmpty(queryPageRequest.getTemplateId())) {
            cmsPage.setTemplateId(queryPageRequest.getTemplateId());
        }
        //页面别名
        if (StringUtils.isNotEmpty(queryPageRequest.getPageAliase())) {
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
        Example<CmsPage> example = Example.of(cmsPage, exampleMatcher);
        //设置分页参数
        PageRequest pageRequest = new PageRequest(page, size);
        //调用dao
        Page<CmsPage> all = cmsPageRepository.findAll(example, pageRequest);
        QueryResult<CmsPage> pageQueryResult = new QueryResult<>();
        //设置本页数据
        pageQueryResult.setList(all.getContent());
        //设置总页数
        pageQueryResult.setTotal(all.getTotalElements());
        return new QueryResponseResult(CommonCode.SUCCESS, pageQueryResult);
    }

    /**
     * 添加页面，先查询要添加的页面是否存在
     * 如果存在直接返回
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage) {
        CmsPage cms = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(
                cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        //如果查询出来的对象数空的，则进行添加
        if(cms!=null){
            ExceptionCast.cast(CommonCode.Cunzai);
        }
        cmsPage.setPageId(null);//主键由spring date自己生成
        cmsPageRepository.save(cmsPage);
        //添加成功返回结果
        return new CmsPageResult(CommonCode.SUCCESS,cmsPage);
//        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 根据页面的id来查询页面的详细信息
     * @return
     */
    public CmsPage getByid(String id){
        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        //判断结果是否为空
        if(byId.isPresent()){
            return byId.get();
        }
        return null;
    }

    /**
     * 根据页面的id修改页面的信息
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult upDate(String id,CmsPage cmsPage){
        CmsPage cms = this.getByid(id);
        //查看是否查询成功
        if(cms !=null ){
            //更新模板id
            cms.setTemplateId(cmsPage.getTemplateId());
            //更新所属站点
            cms.setSiteId(cmsPage.getSiteId());
            //更新页面别名
            cms.setPageAliase(cmsPage.getPageAliase());
            //更新页面名称
            cms.setPageName(cmsPage.getPageName());
            //更新访问路径
            cms.setPageWebPath(cmsPage.getPageWebPath());
            //更新物理路径
            cms.setPagePhysicalPath(cmsPage.getPagePhysicalPath());
            CmsPage save = cmsPageRepository.save(cms);
            //看看是否修改成功
            if(save!=null){
                return new CmsPageResult(CommonCode.SUCCESS,save);
            }
        }
        //到这里就说明修改失败
        return new CmsPageResult(CommonCode.FAIL,null);
    }


    /**
     * 删除页面
     * @param id
     * @return
     */
    public ResponseResult deleteByid(String id) {
        /*
            先根据id查询一下，如果有值就删除
            没有值就直接返回
         */
        CmsPage cms = this.getByid(id);
        if(cms==null){
            ExceptionCast.cast(CommonCode.DELETEBYID);
        }
        cmsPageRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }
}
