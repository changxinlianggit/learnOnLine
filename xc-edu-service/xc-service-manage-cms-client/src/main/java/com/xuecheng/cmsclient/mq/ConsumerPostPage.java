package com.xuecheng.cmsclient.mq;

import com.alibaba.fastjson.JSON;
import com.xuecheng.cmsclient.dao.CmsPageRepository;
import com.xuecheng.cmsclient.service.PageService;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.Optional;

@Component
public class ConsumerPostPage {

    //日志记录
    public static final Logger LOGGER= LoggerFactory.getLogger(ConsumerPostPage.class);

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    PageService pageService;
    //监听 队列，只要监听到携带这个key的她就会执行
    @RabbitListener(queues = {"${xuecheng.mq.queue}"})
    public void postpage(String msg) {
        //把接收到的消息转换成json字符串
        Map map = JSON.parseObject(msg, Map.class);
        if (map == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        String pageID = (String) map.get("pageID");
        Optional<CmsPage> byId = cmsPageRepository.findById(pageID);
        //如果是空的话，直接返回
        if(!byId.isPresent()){
            LOGGER.error("receive cms post page,cmsPage is null:{}",msg.toString());
            return ;
        }
        //调用service保存静态化文件到本地
        pageService.savePageToServerPath(pageID);
    }
}
