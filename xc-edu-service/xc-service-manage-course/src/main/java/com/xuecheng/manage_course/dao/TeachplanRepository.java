package com.xuecheng.manage_course.dao;

import com.xuecheng.framework.domain.course.Teachplan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TeachplanRepository extends JpaRepository<Teachplan,String> {

    //根据课程的id加上父节点id查询
    List<Teachplan> findByCourseidAndParentid(String courseid, String parentid);

}
