package com.xuecheng.manage_course.service;

import com.alibaba.fastjson.JSON;
import com.github.pagehelper.Page;
import com.github.pagehelper.PageHelper;
import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CourseMarket;
import com.xuecheng.framework.domain.course.CoursePic;
import com.xuecheng.framework.domain.course.Teachplan;
import com.xuecheng.framework.domain.course.ext.CourseInfo;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.domain.course.response.CourseCode;
import com.xuecheng.framework.exception.ExceptionCast;
import com.xuecheng.framework.model.response.CommonCode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.QueryResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.dao.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import sun.security.util.Length;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CourseService {
    @Autowired
    CourseMapper courseMapper;

    @Autowired
    CourseBaseRepository courseBaseRepository;

    @Autowired
    TeachplanRepository teachplanRepository;

    @Autowired
    CourseMarketRepository courseMarketRepository;

    @Autowired
    CoursePicRepository coursePicRepository;

    //查询课程图片
    public CoursePic findCoursePicByid(String courseId) {
        if (courseId == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //
        CoursePic byCourseid = coursePicRepository.findByCourseid(courseId);
        return byCourseid;
    }

    //删除课程图片
    @Transactional
    public ResponseResult deleteByCourseid(String courseId) {
        if (courseId == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //查询是否存在
        CoursePic byCourseid = coursePicRepository.findByCourseid(courseId);
        if (byCourseid == null) {
            return new ResponseResult(CommonCode.FAIL);
        }
        //如果存在就删除
        coursePicRepository.deleteByCourseid(courseId);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //保存课程图片
    @Transactional
    public ResponseResult saveCoursePic(String courseId, String pic) {
//        String  courseId = (String) map.get("courseId");
////        String pic = (String) map.get("pic");
        if (courseId == null && pic == null) {
            return new ResponseResult(CommonCode.FAIL);
        }
        //查询课程图片是否有值
        CoursePic byId = coursePicRepository.findByCourseid(courseId);
        if (byId != null) {
            coursePicRepository.deleteByCourseid(courseId);
        }
        CoursePic coursePic = new CoursePic();
        coursePic.setCourseid(courseId);
        coursePic.setPic(pic);
        coursePicRepository.save(coursePic);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //修改课程营销
    public ResponseResult updatebyMarketId(String id, CourseMarket courseMarket) {
        Optional<CourseMarket> byId = courseMarketRepository.findById(id);
        if (!byId.isPresent()) {
            //如果数据库中没有课程计划，则直接保存
            courseMarketRepository.save(courseMarket);
            return new ResponseResult(CommonCode.SUCCESS);
        }
        //如果有的话再修改
        CourseMarket update = byId.get();
        update.setCharge(courseMarket.getCharge());
        update.setValid(courseMarket.getValid());
        //设置qq
        if (courseMarket.getQq() != null) {
            update.setQq(courseMarket.getQq());
        }
        //设置价格
        if (courseMarket.getPrice() != null) {
            update.setPrice(courseMarket.getPrice());
        }
        //设置原价
        if (courseMarket.getPrice_old() != null) {
            update.setPrice_old(courseMarket.getPrice_old());
        }
        //设置开始时间
        if (courseMarket.getStartTime() != null) {
            update.setStartTime(courseMarket.getStartTime());
        }
        //设置结束时间
        if (courseMarket.getEndTime() != null) {
            update.setEndTime(courseMarket.getEndTime());
        }
        courseMarketRepository.save(update);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询课程营销
    public CourseMarket findByMarketId(String id) {
        Optional<CourseMarket> byId = courseMarketRepository.findById(id);
        if (!byId.isPresent()) {
            ExceptionCast.cast(CommonCode.CANSHU);
        }
        return byId.get();
    }

    //修改课程
    public ResponseResult updatebyId(String id, CourseBase courseBase) {
        //查询课程是否存在
        Optional<CourseBase> byId = courseBaseRepository.findById(id);
        if (!byId.isPresent()) {
            ExceptionCast.cast(CommonCode.CANSHU);
        }
        //到这里就说明课程存在，获取课程信息
        //修改课程信息
        CourseBase one = byId.get();
        if (courseBase.getName() != null) {
            one.setName(courseBase.getName());
        }
        if (courseBase.getMt() != null) {
            one.setMt(courseBase.getMt());
        }
        if (courseBase.getSt() != null) {
            one.setSt(courseBase.getSt());
        }
        if (courseBase.getGrade() != null) {
            one.setGrade(courseBase.getGrade());
        }
        if (courseBase.getStudymodel() != null) {
            one.setStudymodel(courseBase.getStudymodel());
        }
        if (courseBase.getUsers() != null) {
            one.setUsers(courseBase.getUsers());
        }
        if (courseBase.getDescription() != null) {
            one.setDescription(courseBase.getDescription());
        }
        CourseBase save = courseBaseRepository.save(one);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询单个课程
    public CourseBase findById(String courseid) {
        Optional<CourseBase> byId = courseBaseRepository.findById(courseid);
        if (!byId.isPresent()) {
            ExceptionCast.cast(CommonCode.CANSHU);
        }
        return byId.get();
    }

    //添加课程
    public ResponseResult addCoursebase(CourseBase courseBase) {
        if (courseBase == null) {
            ExceptionCast.cast(CommonCode.CANSHU);
        }
        CourseBase save = courseBaseRepository.save(courseBase);
        if (save == null) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        System.out.println(JSON.toJSONString(save));
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //分页查询我的课程
    public QueryResponseResult getpageList(String page, String size) {
        if (page == null && page.length() <= 0 && page == null && page.length() <= 0) {
            return new QueryResponseResult(CommonCode.CANSHU, null);
        }
        int pg = Integer.parseInt(page);
        int sz = Integer.parseInt(size);
        PageHelper.startPage(pg, sz);//设置分页参数
        Page<CourseInfo> bypage = courseMapper.findBypage();

        List<CourseInfo> list = bypage.getResult();//获取查询结果

        QueryResult<CourseInfo> result1 = new QueryResult<>();
        result1.setTotal(bypage.getTotal());//设置总条数
        result1.setList(list);//设置查询结果
        return new QueryResponseResult(CommonCode.SUCCESS, result1);
    }

    //查询课程计划
    public TeachplanNode findTeachplanList(String courseId) {
        TeachplanNode teachplanNode = courseMapper.selectList(courseId);
        return teachplanNode;
    }

    //添加课程
    public ResponseResult addTeachplan(Teachplan teachplan) {
        //判断对象是否为空,还要判断课程id和课程名称都不能为空
        if (teachplan == null &&
                StringUtils.isNotEmpty(teachplan.getCourseid()) &&
                StringUtils.isNotEmpty(teachplan.getPname())) {
            ExceptionCast.cast(CommonCode.CANSHU);
        }
        //获取父节点的信息
        String parentid = teachplan.getParentid();
        if (parentid == null) {
            parentid = getparentID(teachplan);
        }
        if (StringUtils.isEmpty(parentid)) {
            ExceptionCast.cast(CommonCode.FAIL);
        }
        //获取父节点信息
        Optional<Teachplan> byId = teachplanRepository.findById(parentid);
        if (!byId.isPresent()) {
            ExceptionCast.cast(CommonCode.CANSHU);
        }
        //获取父节点的信息
        Teachplan teachplan1 = byId.get();
        String grade = teachplan1.getGrade();//获取父节点级别
        teachplan.setParentid(parentid);//设置父节点id
        teachplan.setStatus("0");//未发布
        //设置子节点的父节点
        if (grade.equals("1")) {
            teachplan.setGrade("2");
        } else {
            teachplan.setGrade("3");
        }
        //设置课程id
        teachplan.setCourseid(teachplan1.getCourseid());
        teachplanRepository.save(teachplan);
        return new ResponseResult(CommonCode.SUCCESS);
    }

    //查询上级节点id
    public String getparentID(Teachplan teachplan) {
        Optional<CourseBase> byId = courseBaseRepository.findById(teachplan.getCourseid());
        if (!byId.isPresent()) {//如果没有这个课程直接返回
            return null;
        }
        CourseBase courseBase = byId.get();
        //获取课程id
        String courseid = courseBase.getId();
        //查询该课程是否有根节点
        List<Teachplan> list = teachplanRepository.findByCourseidAndParentid(courseid, "0");
        if (list != null && list.size() > 0) {
            //有根节点直接返回
            return list.get(0).getId();
        }
        //到这里就说明没有根节点，需要添加
        //新增一个根结点
        Teachplan teachplanRoot = new Teachplan();
        teachplanRoot.setCourseid(courseid);//课程名称
        //当没有根节点的时候就以课程名称为根节点名称
        teachplanRoot.setPname(courseBase.getName());
        teachplanRoot.setParentid("0");//父节点id
        teachplanRoot.setGrade("1");//1级
        teachplanRoot.setStatus("0");//未发布
        teachplanRepository.save(teachplanRoot);
        //最后把新添加的节点返回
        return teachplanRoot.getId();
    }


}
