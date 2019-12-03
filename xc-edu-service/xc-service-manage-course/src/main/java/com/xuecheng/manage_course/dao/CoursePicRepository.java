package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.CourseBase;
import com.xuecheng.framework.domain.course.CoursePic;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * 课程图片信息
 */
public interface CoursePicRepository extends JpaRepository<CoursePic,String> {

    public CoursePic findByCourseid(String courseId);

    public void deleteByCourseid(String courseId);

}
