package com.xuecheng.manage_cms.web.controller;

import com.xuecheng.framework.web.BaseController;
import com.xuecheng.manage_cms.service.PageService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.ServletOutputStream;
import java.io.IOException;

@Controller
public class CmsPagePreviewController extends BaseController {

    @Autowired
    PageService pageService;

    /**
     * 点击页面预览执行这里
     * @param pageid
     */
    @RequestMapping("/cms/preview/{pageid}")
    public void perview(@PathVariable("pageid")String pageid){
        String pageHtml = pageService.getPageHtml(pageid);
        //如果不是空就会进来
        if(StringUtils.isNotEmpty(pageHtml)){
            try {
                //使用response对象获取输出流
                ServletOutputStream outputStream = response.getOutputStream();
                //输出到页面
                outputStream.write(pageHtml.getBytes());
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }
}
