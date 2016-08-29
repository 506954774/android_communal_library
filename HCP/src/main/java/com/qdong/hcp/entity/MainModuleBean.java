package com.qdong.hcp.entity;

import java.io.Serializable;

/**
 * MainModuleBean
 * 主页点击模块实体
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/8/20  18:24
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class MainModuleBean implements Serializable {

    private static final long serialVersionUID = 19840902L;
    private int id;
    private int stringResouseId;
    private int imageResouseId;

    public MainModuleBean() {
    }

    public MainModuleBean(int id, int stringResouseId, int imageResouseId) {
        this.id = id;
        this.stringResouseId = stringResouseId;
        this.imageResouseId = imageResouseId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getStringResouseId() {
        return stringResouseId;
    }

    public void setStringResouseId(int stringResouseId) {
        this.stringResouseId = stringResouseId;
    }

    public int getImageResouseId() {
        return imageResouseId;
    }

    public void setImageResouseId(int imageResouseId) {
        this.imageResouseId = imageResouseId;
    }
}
