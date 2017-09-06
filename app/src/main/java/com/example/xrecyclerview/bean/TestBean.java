package com.example.xrecyclerview.bean;

import java.io.Serializable;

/**
 * 创建者:    wang.zhonghao
 * 创建时间:  2017/4/14
 * 描述:      供测试使用的
 */
public class TestBean implements Serializable {

    public TestBean(int number) {
        this.number = number;
    }

    public int getNumber() {

        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    private int number;
}
