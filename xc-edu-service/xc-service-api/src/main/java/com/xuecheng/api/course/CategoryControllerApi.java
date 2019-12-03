package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.ext.CategoryNode;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "管理课程的分类",description = "实现课程管理添加中查询课程的分类")
public interface CategoryControllerApi {

    @ApiOperation("查询课程管理的分类")
    CategoryNode getcoureseType();
}
