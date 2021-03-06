package com.demo.study.web.course;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.study.entity.*;
import com.demo.study.service.CourseService;
import com.demo.study.service.UserService;
import com.demo.study.util.Status;
import com.demo.study.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/course")
public class CourseController {
    @Autowired
    private CourseService courseService;
    @Autowired
    private UserService userService;

    @RequestMapping(value = "getcoursecategory",method = RequestMethod.GET)
    @ResponseBody
    private BaseBackBean getCourseCategoryList(){
        BaseBackBean backBean = new BaseBackBean();
        try {
            List<CourseCategorybean> categoryList = courseService.getCourseCategoryList();
            List<CourseCategorybean> list=new ArrayList<>();
            for (CourseCategorybean bean:categoryList){
                bean.setTitle(getCategoryString(bean.getCategoryId()));
            }

            for (int i = 0; i < categoryList.size()-1; i++) {
                for (int j = categoryList.size()-1; j > i; j--) {
                    if (categoryList.get(j).getTitle().equals(categoryList.get(i).getTitle())) {
                        categoryList.remove(j);
                    }
                }
            }
            for (CourseCategorybean bean:categoryList){
                Integer categoryId = bean.getCategoryId();
                if (categoryId==1||categoryId==2||categoryId==7||categoryId==8||categoryId==9||categoryId==11){
                    list.add(0,bean);
                }else {
                    list.add(bean);
                }
            }
            
            backBean.setStatus(Status.STATUS_SUCCESS);
            backBean.setData(list);
        } catch (Exception e) {
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("获取课程类型异常");
        }
        return backBean;
    }

    private String getCategoryString(Integer categoryId) {
        //课程类别，1-热门，2-精品，9-营销管理，6-其他，10-企业内训，8-运营商，7-手机厂商，11-智能数码
        switch (categoryId){
            case 1:return "热门推荐";
            case 2:return "精品课程";
            case 9:return "营销管理";
            case 8:return "运营商";
            case 7:return "手机厂商";
            case 11:return "智能数码";
            default:return "其他";
        }
    }


    /**
     * 获取课程列表
     * @param str
     * @return
     */
    @RequestMapping(value = "getcourselist", method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean getCourseList(@RequestBody String str){
        BaseBackBean backBean = new BaseBackBean();
        try {
            if (Util.isEmpty(str)){
                backBean.setStatus(Status.STATUS_FAIL);
                backBean.setErrorMsg("传参错误");
            }else {
                JSONObject jsonObject = JSON.parseObject(str);
                List<CourseListBean> courseList = courseService.getCourseList(jsonObject.getInteger("categoryId"),
                        jsonObject.getInteger("pageNum"),
                        jsonObject.getInteger("pageSize"));
                backBean.setStatus(Status.STATUS_SUCCESS);
                backBean.setData(courseList);
            }
        }catch (Exception e){
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("获取课程列表异常");
        }
        return backBean;
    }

    /**
     * 获取课程详情
     * @param str
     * @param userId
     * @return
     */
    @RequestMapping(value = "getcoursedetail",method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean getCoueseDetail(@RequestBody String str,
                                         @RequestHeader(required = false,value = "userId") String userId){
        BaseBackBean backBean = new BaseBackBean();
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            Integer courseId = jsonObject.getInteger("courseId");
            if (null==courseId||courseId<=0){
                backBean.setStatus(Status.STATUS_FAIL);
                backBean.setErrorMsg("传参错误");
            }else {
                //获取课程详情
                CourseListBean courseDetail = courseService.getCourseDetail(courseId);
                if (null==courseDetail){
                    backBean.setStatus(Status.STATUS_FAIL);
                    backBean.setErrorMsg("没有该课程详情");
                }else {
                    if (Util.isEmpty(userId)){
                        courseDetail.setIsBuy(0);
                    }else {
                        //查询该课程是否已被购买
                        int isBuy = userService.getCourseIsBuy(Integer.valueOf(userId), courseId);
                        //没有购买
                        if (isBuy<=0){
                            courseDetail.setIsBuy(0);
                        }else {  //已购买
                            courseDetail.setIsBuy(1);
                        }
                    }

                    backBean.setStatus(Status.STATUS_SUCCESS);
                    backBean.setData(courseDetail);
                }
            }
        } catch (Exception e) {
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("获取课程详情异常");
        }
        return backBean;
    }

    /**
     * 获取课程播放目录
     * @param str
     * @return
     */
    @RequestMapping(value = "getcoursemenulist",method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean getCourseMenuList(@RequestBody String str){
        BaseBackBean backBean = new BaseBackBean();
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            Integer menuId = jsonObject.getInteger("menuId");
            if (null==menuId||menuId<=0){
                backBean.setStatus(Status.STATUS_FAIL);
                backBean.setErrorMsg("传参错误");
            }else{
                List<CourseMenuBean> courseMenuList = courseService.getCourseMenuList(menuId);
                backBean.setStatus(Status.STATUS_SUCCESS);
                backBean.setData(courseMenuList);
            }
        } catch (Exception e) {
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("获取课程目录异常");
        }
        return backBean;
    }

    @RequestMapping(value = "getcoursecomment",method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean getCourseCommentList(@RequestBody String str){
        BaseBackBean backBean = new BaseBackBean();
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            Integer menuId = jsonObject.getInteger("menuId");
            Integer pageNum = jsonObject.getInteger("pageNum");
            Integer pageSize = jsonObject.getInteger("pageSize");
            List<CourseCommentBean> commentList = courseService.getCourseCommentList(menuId, pageNum, pageSize);
            backBean.setStatus(Status.STATUS_SUCCESS);
            backBean.setData(commentList);
        } catch (Exception e) {
            System.out.println("======"+e.getMessage());
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("获取评论列表异常");
        }
        return backBean;
    }

    @RequestMapping(value = "addcoursecomment.do",method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean addCourseCommentList(@RequestBody String str,@RequestHeader("userId")Integer userId){
        BaseBackBean backBean = new BaseBackBean();
        try {
            JSONObject jsonObject = JSON.parseObject(str);
            String content = jsonObject.getString("content");
            Integer menuId = jsonObject.getInteger("menuId");
            int num = courseService.addCourseComment(userId, content, new Date(), menuId);
            if (num<=0){
                backBean.setStatus(Status.STATUS_FAIL);
                backBean.setErrorMsg("发表评论失败");
            }else {
                backBean.setStatus(Status.STATUS_SUCCESS);
            }
        } catch (Exception e) {
            System.out.println("============"+e.getMessage());
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("发表评论异常");
        }
        return backBean;
    }


    @RequestMapping(value = "buycourse.do",method = RequestMethod.POST)
    @ResponseBody
    private BaseBackBean buyCourse(@RequestBody String str,@RequestHeader("userId")Integer userId){
        BaseBackBean backBean = new BaseBackBean();
        JSONObject jsonObject = JSON.parseObject(str);
        Integer menuId = jsonObject.getInteger("menuId");
        try {
            backBean = courseService.buyCourse(userId, menuId);
        } catch (Exception e) {
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg(e.getMessage());
        }
        return backBean;
    }
}
