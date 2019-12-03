package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.request.QueryPageRequest;
import com.xuecheng.framework.domain.cms.response.CmsPageResult;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;

@Api(value = "cms页面接口",description = "cms页面管理接口， 提供页面的 crud")
public interface CmsPageControllerApi {
    @ApiOperation("分页查询页面列表")
    QueryResponseResult findList(int page, int size, QueryPageRequest queryPageRequest);

    @ApiOperation("新增页面")
    CmsPageResult add(CmsPage cmsPage);

    @ApiOperation("通过ID查询页面")
    public CmsPageResult findById(String id);

    @ApiOperation("修改页面")
    public CmsPageResult edit(String id,CmsPage cmsPage);

    @ApiOperation("删除页面")
    ResponseResult deleteByid(String id);

    @ApiOperation("发布页面")
    public ResponseResult post(String pageId);

}
