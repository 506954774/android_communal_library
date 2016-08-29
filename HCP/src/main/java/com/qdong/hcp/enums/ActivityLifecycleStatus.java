package com.qdong.hcp.enums;

/**
 * ActivityLifecycle
 * 生命周期状态
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/7  14:50tate
 * Copyright : 趣动智能科技有限公司-版权所有
 */
public enum ActivityLifecycleStatus {

    CREATED(0),
    STARTED(1),
    RESUMED(2),
    PAUSED(3),
    STOPPED(4);

    public int getValue(){
        return mValue;
    }
    private final int mValue;

    private ActivityLifecycleStatus(int value){
        mValue = value;
    }

}
