package com.xuecheng.framework.domain.cms.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class QueryPageRequest {
    //站点id
    @ApiModelProperty("站点id")
    private String siteId;
    // 页面ID
    @ApiModelProperty("网页id")
    private String pageId;
    // 页面名称
    @ApiModelProperty("网页名称")
    private String pageName;
    // 别名
    @ApiModelProperty("网页别名")
    private String pageAliase;
    // 模版id
    @ApiModelProperty("模板id")
    private String templateId;
}
