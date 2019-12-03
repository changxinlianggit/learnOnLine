package com.xuecheng.api.course;

import com.xuecheng.framework.domain.course.*;
import com.xuecheng.framework.domain.course.ext.TeachplanNode;
import com.xuecheng.framework.model.response.QueryResponseResult;
import com.xuecheng.framework.model.response.ResponseResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.Map;

@Api(value = "Course页面接口", description = "Course页面管理接口,提供页面的 crud")
public interface CourseControllerApi {

    @ApiOperation("课程计划查询")
    TeachplanNode findTeachplanList(String courseId);

    @ApiOperation("添加课程计划")
    public ResponseResult addTeachplan(Teachplan teachplan);

    @ApiOperation("分页查询课程的详细信息")
    QueryResponseResult getPagelist(String page,String size);

    @ApiOperation("添加课程")
    ResponseResult addCoursebase( CourseBase courseBase);

    @ApiOperation("查询单个课程")
    CourseBase findByid( String courseId);

    @ApiOperation("根据id修改课程")
    ResponseResult updatebyId(String id,CourseBase courseBase);

    @ApiOperation("查询课程营销")
    CourseMarket findByMarketId(String id);

    @ApiOperation("根据id修改课程营销计划")
    ResponseResult updatebyMarketId(String id,CourseMarket courseMarket);

    @ApiOperation("保存课程图片")
    ResponseResult saveCoursePic(String courseId,String pic);

    @ApiOperation("查询课程图片")
    CoursePic findCoursePicByid(String courseId);

    @ApiOperation("删除课程图片")
    ResponseResult deleteByCourseid(String courseId);

}
