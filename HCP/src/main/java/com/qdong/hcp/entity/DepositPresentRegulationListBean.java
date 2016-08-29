package com.qdong.hcp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * DepositPresentRegulationListBean
 * 充值赠送规则
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/7/26  16:08
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class DepositPresentRegulationListBean implements Serializable {

    private static final long serialVersionUID = 19840902L;


    private boolean success;
    private String errorCode;
    private String message;


    private List<ResultBean> result;

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ResultBean> getResult() {
        return result;
    }

    public void setResult(List<ResultBean> result) {
        this.result = result;
    }

    public static class ResultBean {
        private int depositMoney;
        private int presentMoney;

        public int getDepositMoney() {
            return depositMoney;
        }

        public void setDepositMoney(int depositMoney) {
            this.depositMoney = depositMoney;
        }

        public int getPresentMoney() {
            return presentMoney;
        }

        public void setPresentMoney(int presentMoney) {
            this.presentMoney = presentMoney;
        }
    }
}
