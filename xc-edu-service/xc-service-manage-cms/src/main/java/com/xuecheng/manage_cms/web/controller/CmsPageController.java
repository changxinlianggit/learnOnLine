package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.api.cms.CmsPageControllerApi;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_cms.service.PageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cms/page")
public class CmsPageController implements CmsPageControllerApi {

    @Autowired
    private PageService pageService;

    /**
     * 分页模糊查询
     * @param page
     * @param size
     * @param queryPageRequest
     * @return
     */
    @GetMapping("/list/{page}/{size}")
    public QueryResponseResult findList(@PathVariable("page") int page,@PathVariable("size") int size, QueryPageRequest queryPageRequest) {
        return pageService.findList(page,size,queryPageRequest);
    }

    /**
     * 添加页面
     * @param cmsPage 要添加页面的参数
     * @return
     */
    @PostMapping("/add")
    //使用RequestBody接收json字符串
    public CmsPageResult add(@RequestBody CmsPage cmsPage) {
        return pageService.add(cmsPage);
    }

    /**
     * 根据页面id查询页面详细信息
     * @param id
     * @return
     */

    @GetMapping("/findById/{id}")
    public CmsPageResult findById(@PathVariable("id") String id) {
        CmsPage cms = pageService.getByid(id);
        if(cms != null){
            return new CmsPageResult(CommonCode.SUCCESS,cms);
        }
        return new CmsPageResult(CommonCode.FAIL,null);
    }

    /**
     * 修改页面信息
     * @param id
     * @param cmsPage
     * @return
     */
    @PutMapping("/edit/{id}")
    public CmsPageResult edit(@PathVariable("id") String id,@RequestBody CmsPage cmsPage) {
        return pageService.upDate(id,cmsPage);
    }

    /**
     * 根据id删除页面
     * @param id
     * @return
     */
    @DeleteMapping("/del/{id}")
    public ResponseResult deleteByid(@PathVariable("id") String id) {
        return pageService.deleteByid(id);
    }

    /**
     * 生成静态化页面文件，然后保存到gridFS中
     * @param pageId
     * @return
     */
    @PostMapping("/postPage/{pageId}")
    public ResponseResult post(@PathVariable("pageId") String pageId) {
        ResponseResult responseResult = pageService.postPage(pageId);
        return responseResult;
    }
}
