package com.cxp.im;

import com.cxp.im.bean.TestBean;
import com.google.gson.Gson;

import org.junit.Test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 文 件 名: JsonTest
 * 创 建 人: CXP
 * 创建日期: 2020-10-21 17:40
 * 描    述:
 * 修 改 人:
 * 修改时间：
 * 修改备注：
 */
public class JsonTest {

    @Test
    public void test() {

        TestBean.UserBean.BookBean.CarBean carBean=new  TestBean.UserBean.BookBean.CarBean("奔驰");
        TestBean.UserBean.BookBean.CarBean carBean1=new  TestBean.UserBean.BookBean.CarBean("奔驰1");
        TestBean.UserBean.BookBean.CarBean carBean2=new  TestBean.UserBean.BookBean.CarBean("奔驰2");
        List<TestBean.UserBean.BookBean.CarBean> catList=new ArrayList<>();
        catList.add(carBean);
        catList.add(carBean1);
        catList.add(carBean2);
        TestBean.UserBean.BookBean bookBean=new TestBean.UserBean.BookBean("图书",catList);
        TestBean.UserBean.BookBean bookBean1=new TestBean.UserBean.BookBean("图书1",catList);
        TestBean.UserBean.BookBean bookBean2=new TestBean.UserBean.BookBean("图书2",catList);
        List<TestBean.UserBean.BookBean> bookList=new ArrayList<>();
        bookList.add(bookBean);
        bookList.add(bookBean1);
        bookList.add(bookBean2);
        TestBean.UserBean userBean=new TestBean.UserBean("CXP",bookList);
        TestBean.UserBean userBean1=new TestBean.UserBean("CXP1",bookList);
        TestBean.UserBean userBean2=new TestBean.UserBean("CXP2",bookList);
        List<TestBean.UserBean> userList=new ArrayList<>();
        userList.add(userBean);
        userList.add(userBean1);
        userList.add(userBean2);
        TestBean testBean=new TestBean("测试",userList);
        TestBean testBean1=new TestBean("测试1",userList);
        TestBean testBean2=new TestBean("测试2",userList);
        List<TestBean> testBeanList=new ArrayList<>();
        testBeanList.add(testBean);
        testBeanList.add(testBean1);
        testBeanList.add(testBean2);
        Map<String,Object> map=new HashMap<>();
        map.put("content","11111");
        map.put("list",testBeanList);
        System.out.println(new Gson().toJson(map));
    }
}
