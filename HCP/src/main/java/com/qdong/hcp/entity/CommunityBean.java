package com.qdong.hcp.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Data
 * 责任人:  Chuck
 * 修改人： Chuck
 * 创建/修改时间: 2016/6/29  20:47
 * Copyright : 趣动智能科技有限公司-版权所有
 **/
public class CommunityBean implements Serializable {

    private static final long serialVersionUID = 19840902L;

    /**
     * type : 0
     * createTime : 1467166805000
     * result : {"imgTxtId":281,"description":"","lat":22.541287,"lng":113.939203,"address":null,"cTime":1467166805000,"descImages":["T1KahTByVT1RCvBVdK","T1OthTBXDT1RCvBVdK","T1KRhTByLT1RCvBVdK","T1cRhTBXdT1RCvBVdK","T1KyhTBybT1RCvBVdK"],"likeNum":0,"likedUsers":[],"commentNum":0,"comments":[],"creator":{"accId":282,"nickname":"快乐的小毛驴","photoUrl":"T1waxTBydT1RCvBVdK"},"isLiked":0}
     */

    private int type;
    private long createTime;
    /**
     * imgTxtId : 281
     * description :
     * lat : 22.541287
     * lng : 113.939203
     * address : null
     * cTime : 1467166805000
     * descImages : ["T1KahTByVT1RCvBVdK","T1OthTBXDT1RCvBVdK","T1KRhTByLT1RCvBVdK","T1cRhTBXdT1RCvBVdK","T1KyhTBybT1RCvBVdK"]
     * likeNum : 0
     * likedUsers : []
     * commentNum : 0
     * comments : []
     * creator : {"accId":282,"nickname":"快乐的小毛驴","photoUrl":"T1waxTBydT1RCvBVdK"}
     * isLiked : 0
     */

    private ResultBean result;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public ResultBean getResult() {
        return result;
    }

    public void setResult(ResultBean result) {
        this.result = result;
    }

    public static class ResultBean {
        private int imgTxtId;
        private String description;
        private double lat;
        private double lng;
        private Object address;
        private long cTime;
        private int likeNum;
        private int commentNum;
        /**
         * accId : 282
         * nickname : 快乐的小毛驴
         * photoUrl : T1waxTBydT1RCvBVdK
         */

        private CreatorBean creator;
        private int isLiked;
        private List<String> descImages;
        private List<?> likedUsers;
        private List<?> comments;

        public int getImgTxtId() {
            return imgTxtId;
        }

        public void setImgTxtId(int imgTxtId) {
            this.imgTxtId = imgTxtId;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public double getLat() {
            return lat;
        }

        public void setLat(double lat) {
            this.lat = lat;
        }

        public double getLng() {
            return lng;
        }

        public void setLng(double lng) {
            this.lng = lng;
        }

        public Object getAddress() {
            return address;
        }

        public void setAddress(Object address) {
            this.address = address;
        }

        public long getCTime() {
            return cTime;
        }

        public void setCTime(long cTime) {
            this.cTime = cTime;
        }

        public int getLikeNum() {
            return likeNum;
        }

        public void setLikeNum(int likeNum) {
            this.likeNum = likeNum;
        }

        public int getCommentNum() {
            return commentNum;
        }

        public void setCommentNum(int commentNum) {
            this.commentNum = commentNum;
        }

        public CreatorBean getCreator() {
            return creator;
        }

        public void setCreator(CreatorBean creator) {
            this.creator = creator;
        }

        public int getIsLiked() {
            return isLiked;
        }

        public void setIsLiked(int isLiked) {
            this.isLiked = isLiked;
        }

        public List<String> getDescImages() {
            return descImages;
        }

        public void setDescImages(List<String> descImages) {
            this.descImages = descImages;
        }

        public List<?> getLikedUsers() {
            return likedUsers;
        }

        public void setLikedUsers(List<?> likedUsers) {
            this.likedUsers = likedUsers;
        }

        public List<?> getComments() {
            return comments;
        }

        public void setComments(List<?> comments) {
            this.comments = comments;
        }

        public static class CreatorBean {
            private int accId;
            private String nickname;
            private String photoUrl;

            public int getAccId() {
                return accId;
            }

            public void setAccId(int accId) {
                this.accId = accId;
            }

            public String getNickname() {
                return nickname;
            }

            public void setNickname(String nickname) {
                this.nickname = nickname;
            }

            public String getPhotoUrl() {
                return photoUrl;
            }

            public void setPhotoUrl(String photoUrl) {
                this.photoUrl = photoUrl;
            }
        }
    }
}
