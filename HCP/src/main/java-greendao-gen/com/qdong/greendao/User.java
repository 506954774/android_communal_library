package com.qdong.greendao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT. Enable "keep" sections if you want to edit. 
/**
 * Entity mapped to table USER.
 */
public class User {

    private Long id;
    private Integer user_id;
    /** Not-null value. */
    private String account;
    /** Not-null value. */
    private String password;

    public User() {
    }

    public User(Long id) {
        this.id = id;
    }

    public User(Long id, Integer user_id, String account, String password) {
        this.id = id;
        this.user_id = user_id;
        this.account = account;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getUser_id() {
        return user_id;
    }

    public void setUser_id(Integer user_id) {
        this.user_id = user_id;
    }

    /** Not-null value. */
    public String getAccount() {
        return account;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setAccount(String account) {
        this.account = account;
    }

    /** Not-null value. */
    public String getPassword() {
        return password;
    }

    /** Not-null value; ensure this value is available before it is saved to the database. */
    public void setPassword(String password) {
        this.password = password;
    }

}
