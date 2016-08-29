package com.qdong.hcp.enums;


import com.qdong.hcp.fragment.ContactsFt;
import com.qdong.hcp.fragment.HomeFragment;
import com.qdong.hcp.fragment.MyFt;
import com.qdong.hcp.fragment.WorkManagerFt;

/**
 * MainMoudleType
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/8  10:00
 * Copyright : 趣动智能科技有限公司-版权所有
 */
public enum MainMoudleType {

    FRAGMENT_HOME(HomeFragment.class),
    FRAGMENT_JOB_MANAGE(WorkManagerFt.class),
    FRAGMENT_CONTACTS(ContactsFt.class),
    FRAGMENT_MINE(MyFt.class);


    private MainMoudleType(Class<?> mClass) {

        this.mClass = mClass;

    }


    public Class<?> getmClass() {
        return mClass;
    }

    private Class<?> mClass;


}
