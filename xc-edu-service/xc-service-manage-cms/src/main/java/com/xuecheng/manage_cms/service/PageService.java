package com.xuecheng.manage_cms.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.framework.domain.cms.CmsConfig;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsTemplate;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.dao.CmsConfigRepository;
import com.xuecheng.manage_cms.dao.CmsPageRepository;
import com.xuecheng.manage_cms.dao.CmsTemplateRepository;
import freemarker.cache.StringTemplateLoader;
import freemarker.template.Configuration;
import freemarker.template.Template;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.Map;
import java.util.Optional;

@Service
public class PageService {

    @Autowired
    CmsTemplateRepository cmsTemplateRepository;

    @Autowired
    private CmsPageRepository cmsPageRepository;

    @Autowired
    private CmsConfigRepository configRepository;

    @Autowired
    RestTemplate restTemplate;

    @Autowired
    GridFsTemplate gridFsTemplate;

    @Autowired
    GridFSBucket gridFSBucket;

    //页面静态化
    public String getPageHtml(String pageId) {
        //先获取页面模型数据
        Map model = this.getModelByPageId(pageId);
        if (model == null) {
            //获取页面模型数据为空
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAISNULL);
        }
        //获取页面模板
        String telement = this.getTelement(pageId);
        if(telement==null){
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //实现页面静态化
        String s = this.generateHtml(telement, model);
        //如果是空，就排除异常
        if (s == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        return s;
    }

    //实现页面静态化
    public String generateHtml(String templateText,Map model){
        try {
            //创建配置类
            Configuration configuration = new Configuration(Configuration.getVersion());
            //模板加载器
            StringTemplateLoader stringTemplateLoader = new StringTemplateLoader();
            stringTemplateLoader.putTemplate("telement",templateText);
            //添加模板
            configuration.setTemplateLoader(stringTemplateLoader);
            //获取模板
            Template telement = configuration.getTemplate("telement");
            String s = FreeMarkerTemplateUtils.processTemplateIntoString(telement, model);
            return s;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    //获取页面模板
    private String getTelement(String pageId) {
        /*
            先根据页面详细信息的id去查询出来页面的详细信息
            然后拿着模板id去模板表查询模板对应的二进制数据
         */
        CmsPage byid = this.getByid(pageId);
        if (byid == null) {
            //页面不存在
            ExceptionCast.cast(CommonCode.CMS_PAGE_NOTEXISTS);
        }
        String templateId = byid.getTemplateId();
        //根据模板id去查询模板的详细信息
        Optional<CmsTemplate> optional = cmsTemplateRepository.findById(templateId);
        //检查是不是空，不是空就是true
        if (optional.isPresent()) {
            //获取映射的对象
            CmsTemplate cmsTemplate = optional.get();
            //获取模板文件的id
            String templateFileId = cmsTemplate.getTemplateFileId();
            //取出模板文件内容
            GridFSFile gridFSFile = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(templateFileId)));
            //打开下载流对象
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(gridFSFile.getObjectId());
            //创建GridFsResource
            GridFsResource gridFsResource = new GridFsResource(gridFSFile, gridFSDownloadStream);

            try {
                String content = IOUtils.toString(gridFsResource.getInputStream(), "utf-8");
                return content;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    //请求模型数据
    private Map getModelByPageId(String pageId) {
        //先根据页面id去查询页面的详细信息
        CmsPage cms = this.getByid(pageId);
        if (cms == null) {
            //页面不存在
            ExceptionCast.cast(CommonCode.CMS_PAGE_NOTEXISTS);
        }
        //然后就可以拿着dateURL去查询模型数据
        String dataUrl = cms.getDataUrl();
        if (StringUtils.isEmpty(dataUrl)) {
            //从页面获取不到数据
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_DATAURLISNULL);
        }
        //走到这里就说明，可以去发送http请求了，这是使用的是RestTemplate
        return restTemplate.getForEntity(dataUrl, Map.class).getBody();
    }

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
     *
     * @param cmsPage
     * @return
     */
    public CmsPageResult add(CmsPage cmsPage) {
        CmsPage cms = cmsPageRepository.findBySiteIdAndPageNameAndPageWebPath(
                cmsPage.getSiteId(), cmsPage.getPageName(), cmsPage.getPageWebPath());
        //如果查询出来的对象数空的，则进行添加
        if (cms != null) {
            ExceptionCast.cast(CommonCode.Cunzai);
        }
        cmsPage.setPageId(null);//主键由spring date自己生成
        cmsPageRepository.save(cmsPage);
        //添加成功返回结果
        return new CmsPageResult(CommonCode.SUCCESS, cmsPage);
//        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 根据页面的id来查询页面的详细信息
     *
     * @return
     */
    public CmsPage getByid(String id) {
        Optional<CmsPage> byId = cmsPageRepository.findById(id);
        //判断结果是否为空
        if (byId.isPresent()) {
            return byId.get();
        }
        return null;
    }

    /**
     * 根据页面的id修改页面的信息
     *
     * @param id
     * @param cmsPage
     * @return
     */
    public CmsPageResult upDate(String id, CmsPage cmsPage) {
        CmsPage cms = this.getByid(id);
        //查看是否查询成功
        if (cms != null) {
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
            if (save != null) {
                return new CmsPageResult(CommonCode.SUCCESS, save);
            }
        }
        //到这里就说明修改失败
        return new CmsPageResult(CommonCode.FAIL, null);
    }


    /**
     * 删除页面
     *
     * @param id
     * @return
     */
    public ResponseResult deleteByid(String id) {
        /*
            先根据id查询一下，如果有值就删除
            没有值就直接返回
         */
        CmsPage cms = this.getByid(id);
        if (cms == null) {
            ExceptionCast.cast(CommonCode.DELETEBYID);
        }
        cmsPageRepository.deleteById(id);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    /**
     * 根据页面的id去查询cms配置信息
     *
     * @param id
     * @return
     */
    public CmsConfig getModel(String id) {
        Optional<CmsConfig> byId = configRepository.findById(id);
        if (byId.isPresent()) {
            CmsConfig cmsConfig = byId.get();
            return cmsConfig;
        }
        return null;
    }
}
