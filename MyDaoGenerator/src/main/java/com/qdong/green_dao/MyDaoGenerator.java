package com.qdong.green_dao;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MyDaoGenerator {

    public static final String PATH="com.qdong.greendao";//配置输出路径

    public static void main(String args[]) throws Exception {

        Schema schema = new Schema(1, PATH);//第一个参数是数据库版本

        addUser(schema);

        addNote(schema);
        //addSession(schema);
        //addReplay(schema);
        //addCustomerOrder(schema);
        new DaoGenerator().generateAll(schema, args[0]);
    }


    /**
     * MyDaoGenerator
     * 生成User表
     * 责任人:  Chuck
     * 修改人： Chuck
     * 创建/修改时间: 2016/8/19  18:31
     * Copyright : 趣动智能科技有限公司-版权所有
     **/
    private static void addUser(Schema schema){

        Entity note = schema.addEntity("User");
        note.addIdProperty().autoincrement();
        note.addIntProperty("user_id");
        note.addStringProperty("account").notNull().unique();
        note.addStringProperty("password").notNull();


    }





    private static void addNote(Schema schema)
    {

        Entity note = schema.addEntity("MqttChatEntity");
        note.addIdProperty().autoincrement();
        note.addIntProperty("mode").notNull();
        note.addStringProperty("sessionid").notNull();
        note.addStringProperty("come").notNull();
        note.addStringProperty("to").notNull();
        note.addStringProperty("v_code");
        note.addStringProperty("v_msg");
        note.addStringProperty("timestamp").notNull();
        note.addStringProperty("platform");
        note.addStringProperty("message");
        note.addBooleanProperty("isread").notNull();
        note.addLongProperty("gossipid");
        note.addStringProperty("gossip");
        note.addIntProperty("chattype").notNull();
        note.addStringProperty("imagepath");
        note.addStringProperty("base64image");
    }

    private static void addSession(Schema schema)
    {
        Entity note = schema.addEntity("SessionEntity");
        note.addIdProperty().autoincrement();
        note.addStringProperty("sessionid").notNull().unique();
        note.addStringProperty("come_from").notNull();
        note.addStringProperty("go_to").notNull();
        note.addLongProperty("gossipid").notNull();
        note.addStringProperty("gossip");
        note.addStringProperty("session_session");
        note.addStringProperty("des");
        note.addIntProperty("sessiontype").notNull();
        note.addBooleanProperty("asdasd").notNull();

        note.addStringProperty("hobby");
        note.addDateProperty("msg_date");

        //note.addBooleanProperty("is_true");
    }

    private static void addReplay(Schema schema)
    {

        Entity note = schema.addEntity("ReplayEntity");
        note.addIdProperty().autoincrement();
        note.addIntProperty("mode").notNull();
        note.addStringProperty("from").notNull();
        note.addStringProperty("to").notNull();
        note.addStringProperty("v_code");
        note.addStringProperty("timestamp").notNull();
        note.addStringProperty("platform");
        note.addStringProperty("message");
        note.addIntProperty("msgtype").notNull();
        note.addBooleanProperty("isread").notNull();
    }

    private static void addCustomerOrder(Schema schema)
    {
        Entity customer = schema.addEntity("Customer");
        customer.addIdProperty();
        customer.addStringProperty("name").notNull();


        Entity order = schema.addEntity("Order");
        order.setTableName("ORDERS"); // "ORDER" is a reserved keyword
        order.addIdProperty();
        Property orderDate = order.addDateProperty("date").getProperty();
        Property customerId = order.addLongProperty("customerId").notNull().getProperty();
        order.addToOne(customer, customerId);


        ToMany customerToOrders = customer.addToMany(order, customerId);
        customerToOrders.setName("orders");
        customerToOrders.orderAsc(orderDate);
    }
}
