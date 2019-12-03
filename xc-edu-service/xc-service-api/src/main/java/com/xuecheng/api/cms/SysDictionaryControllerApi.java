package com.xuecheng.api.cms;

import com.xuecheng.framework.domain.system.SysDictionary;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api(value = "数据字典接口", description = "提供字典接口的管理、查询功能")
public interface SysDictionaryControllerApi {

    @ApiOperation("查询课程等级")
    SysDictionary getsysDictionary(String type);
}
