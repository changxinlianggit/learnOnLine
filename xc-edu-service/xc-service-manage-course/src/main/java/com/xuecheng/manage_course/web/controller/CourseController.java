package com.xuecheng.manage_course.web.controller;

import com.xuecheng.api.course.CourseControllerApi;
import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import com.xuecheng.manage_course.service.CourseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/course")
public class CourseController implements CourseControllerApi {

    @Autowired
    CourseService courseService;

    //查询课程计划
    @GetMapping("/teachplan/list/{courseId}")
    public TeachplanNode findTeachplanList(@PathVariable("courseId") String courseId) {
        return courseService.findTeachplanList(courseId);
    }

    //添加课程计划
    @PostMapping("/teachplan/add")
    public ResponseResult addTeachplan(@RequestBody Teachplan teachplan) {
        return courseService.addTeachplan(teachplan);
    }

    //查询我课程
    @GetMapping("/coursebase/list/{page}/{size}")
    public QueryResponseResult getPagelist(@PathVariable("page") String page, @PathVariable("size") String size) {
        return courseService.getpageList(page, size);
    }

    //添加课程
    @PostMapping("/coursebase/add")
    public ResponseResult addCoursebase(@RequestBody CourseBase CourseBase) {
        return courseService.addCoursebase(CourseBase);
    }

    //根据id查询单个课程信息
    @GetMapping("/findByid/{courseId}")
    public CourseBase findByid(@PathVariable("courseId") String courseId) {
        return courseService.findById(courseId);
    }

    //修改课程
    @PostMapping("/update/{id}")
    public ResponseResult updatebyId(@PathVariable("id") String id, @RequestBody CourseBase courseBase) {
        return courseService.updatebyId(id, courseBase);
    }

    //查询课程营销
    @GetMapping("/MarketById/{id}")
    public CourseMarket findByMarketId(@PathVariable("id") String id) {
        return courseService.findByMarketId(id);
    }

    //修改课程营销
    @PostMapping("/updateMarket/{id}")
    public ResponseResult updatebyMarketId(@PathVariable("id") String id, @RequestBody CourseMarket courseMarket) {
        return courseService.updatebyMarketId(id, courseMarket);
    }

    //保存课程图片
    @PostMapping("/coursepic/add")
    public ResponseResult saveCoursePic(@RequestParam("courseId") String courseId, @RequestParam("pic") String pic) {
        return courseService.saveCoursePic(courseId, pic);
    }

    //查询课程图片
    @GetMapping("/coursepic/list/{courseid}")
    public CoursePic findCoursePicByid(@PathVariable("courseid") String courseId) {
        return courseService.findCoursePicByid(courseId);
    }

    //删除课程图片
    @DeleteMapping("/coursepic/delete")
    public ResponseResult deleteByCourseid(@RequestParam("courseId") String courseId) {
        return courseService.deleteByCourseid(courseId);
    }



}
