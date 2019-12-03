package com.xuecheng.cmsclient.service;

import com.mongodb.client.gridfs.GridFSBucket;
import com.mongodb.client.gridfs.GridFSDownloadStream;
import com.mongodb.client.gridfs.model.GridFSFile;
import com.xuecheng.cmsclient.dao.CmsPageRepository;
import com.xuecheng.cmsclient.dao.CmsSiteRepository;
import com.xuecheng.framework.domain.cms.CmsPage;
import com.xuecheng.framework.domain.cms.CmsSite;
import com.xuecheng.framework.domain.cms.response.CmsCode;
import com.xuecheng.framework.exception.ExceptionCast;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.gridfs.GridFsResource;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.stereotype.Service;

import java.io.*;
import java.util.Optional;

@Service
public class PageService {

    @Autowired
    CmsPageRepository cmsPageRepository;

    @Autowired
    CmsSiteRepository cmsSiteRepository;

    @Autowired
    GridFSBucket gridFSBucket;

    @Autowired
    GridFsTemplate gridFsTemplate;

    /**
     * 这个方法做的就是把静态化的页面保存到本地磁盘
     * 页面物理路径=站点物理路径+页面物理路径+页面名称。
     *
     * @param pageId
     */
    public void savePageToServerPath(String pageId) {
        CmsPage cmsPage = getFileById(pageId);
        if (cmsPage == null) {
            ExceptionCast.cast(CmsCode.CMS_PAGE_NOTEXISTS);
        }
        //静态化文件的id
        String htmlFileId = cmsPage.getHtmlFileId();
        //获取把文件保存在流中的流
        InputStream inputStream = gethtmlFileId(htmlFileId);
        if (inputStream == null) {
            ExceptionCast.cast(CmsCode.CMS_GENERATEHTML_HTMLISNULL);
        }
        //获取站点的详细信息
        CmsSite cmsSite = getCmsSite(cmsPage.getSiteId());
        //访问路径
        String siteWebPath = cmsSite.getSiteWebPath() + cmsPage.getPageWebPath() + cmsPage.getPageName();
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(new File(siteWebPath));
            IOUtils.copy(inputStream, out);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                inputStream.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    //把静态化的文件获取出来，放进输入流中
    public InputStream gethtmlFileId(String htmlFileId) {
        try {
            //把文件内容取出来
            GridFSFile id = gridFsTemplate.findOne(Query.query(Criteria.where("_id").is(htmlFileId)));
            //打开下载流
            GridFSDownloadStream gridFSDownloadStream = gridFSBucket.openDownloadStream(id.getObjectId());

            GridFsResource gridFsResource = new GridFsResource(id, gridFSDownloadStream);
            return gridFsResource.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;

    }

    //查询页面的详细信息
    public CmsPage getFileById(String pageId) {
        Optional<CmsPage> byId = cmsPageRepository.findById(pageId);
        if (byId.isPresent()) {
            CmsPage cmsPage = byId.get();
            return cmsPage;
        }
        return null;

    }

    //获取站点的信息
    public CmsSite getCmsSite(String cmssiteID) {
        Optional<CmsSite> byId = cmsSiteRepository.findById(cmssiteID);
        if (byId.isPresent()) {
            CmsSite cmsSite = byId.get();
            return cmsSite;
        }
        return null;
    }

}
