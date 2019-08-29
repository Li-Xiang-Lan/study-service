package com.demo.study.service.impl;

import com.demo.study.dao.CourseDao;
import com.demo.study.dao.UserDao;
import com.demo.study.entity.*;
import com.demo.study.service.CourseService;
import com.demo.study.util.Status;
import com.demo.study.util.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    private CourseDao courseDao;
    @Autowired
    private UserDao userDao;

    @Override
    public List<CourseCategorybean> getCourseCategoryList() {
        return courseDao.queryCourseCategoryList();
    }

    @Override
    public List<CourseListBean> getCourseList(Integer categoryId, Integer pageNum, Integer pageSize) {
        return courseDao.queryCourseList(categoryId, Util.calculateRowIndex(pageNum, pageSize),pageSize);
    }

    @Override
    public CourseListBean getCourseDetail(Integer id) {
        return courseDao.queryCourseDetail(id);
    }

    @Override
    public List<CourseMenuBean> getCourseMenuList(Integer menuId) {
        return courseDao.queryCourseMenuList(menuId);
    }

    @Override
    public List<CourseCommentBean> getCourseCommentList(Integer menuId, Integer pageNum, Integer pageSize) {
        return courseDao.queryCourseComment(menuId, Util.calculateRowIndex(pageNum, pageSize), pageSize);
    }

    @Override
    public int addCourseComment(Integer userId, String content, Date createTime, Integer menuId) {
        return courseDao.insertCourseComment(userId, content, createTime, menuId);
    }

    @Override
    @Transactional
    public BaseBackBean buyCourse(Integer userId, Integer menuId){
        BaseBackBean backBean = new BaseBackBean();
        //查询课程价格
        CourseListBean courseDetail = courseDao.queryCourseDetail(menuId);
        //查询用户钱包金额
        int money = userDao.queryUserMoney(userId);
        //用户金额不足
        if (courseDetail.getPrice()>money){
            backBean.setStatus(Status.STATUS_FAIL);
            backBean.setErrorMsg("账户余额不足，请充值后再重试");
        }else {  //账户余额够了
            //先扣钱
            int num = userDao.updateUserMoney(userId, money - courseDetail.getPrice());
            if (num>0){
                //查询是否已经购买此课程
                int isBuy = userDao.queryCourseIsBuy(userId, menuId);
                //已购买
                if (isBuy>0){
                    throw new RuntimeException("你已购买此课程");
                }else {
                    //添加购买记录
                    MoneyRecordBean recordBean = new MoneyRecordBean();
                    recordBean.setUserId(userId);
                    recordBean.setMenuId(menuId);
                    recordBean.setCreateTime(new Date());
                    recordBean.setTitle(courseDetail.getTitle());
                    recordBean.setPrice(courseDetail.getPrice());
                    recordBean.setType(1);
                    int courseBuy = courseDao.insertMoneyRecord(recordBean);
                    if (courseBuy>0){
                        backBean.setStatus(Status.STATUS_SUCCESS);
                    }else {
                        throw new RuntimeException("购买课程失败");
                    }
                }
            }else {
                throw new RuntimeException("购买课程失败");
            }
        }
        return backBean;
    }
}
