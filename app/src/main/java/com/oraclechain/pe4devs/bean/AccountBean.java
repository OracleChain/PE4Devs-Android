package com.oraclechain.pe4devs.bean;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by pocketEos on 2018/6/26.
 *
 * @Entity：告诉GreenDao该对象为实体，只有被@Entity注释的Bean类才能被dao类操作
 * @Id：对象的Id，使用Long类型作为EntityId，否则会报错。(autoincrement = true)表示主键会自增，如果false就会使用旧值
 * @Property：可以自定义字段名，注意外键不能使用该属性
 * @NotNull：属性不能为空
 * @Transient：使用该注释的属性不会被存入数据库的字段中
 * @Unique：该属性值必须在数据库中是唯一值
 * @Generated：编译后自动生成的构造函数、方法等的注释，提示构造函数、方法等不能被修改
 */

@Entity
public class AccountBean {
    @Id(autoincrement = true)
    private Long id;
    private String account;
    private String owner_public;
    private String owner_pravite;
    private String active_public;
    private String active_pravite;
    private String sha_pwd;
    private String image;
    private String name;
    private String uid;
    private String phone;
    @Generated(hash = 1392766597)
    public AccountBean(Long id, String account, String owner_public,
            String owner_pravite, String active_public, String active_pravite,
            String sha_pwd, String image, String name, String uid, String phone) {
        this.id = id;
        this.account = account;
        this.owner_public = owner_public;
        this.owner_pravite = owner_pravite;
        this.active_public = active_public;
        this.active_pravite = active_pravite;
        this.sha_pwd = sha_pwd;
        this.image = image;
        this.name = name;
        this.uid = uid;
        this.phone = phone;
    }
    @Generated(hash = 1267506976)
    public AccountBean() {
    }
    public Long getId() {
        return this.id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public String getAccount() {
        return this.account;
    }
    public void setAccount(String account) {
        this.account = account;
    }
    public String getOwner_public() {
        return this.owner_public;
    }
    public void setOwner_public(String owner_public) {
        this.owner_public = owner_public;
    }
    public String getOwner_pravite() {
        return this.owner_pravite;
    }
    public void setOwner_pravite(String owner_pravite) {
        this.owner_pravite = owner_pravite;
    }
    public String getActive_public() {
        return this.active_public;
    }
    public void setActive_public(String active_public) {
        this.active_public = active_public;
    }
    public String getActive_pravite() {
        return this.active_pravite;
    }
    public void setActive_pravite(String active_pravite) {
        this.active_pravite = active_pravite;
    }
    public String getSha_pwd() {
        return this.sha_pwd;
    }
    public void setSha_pwd(String sha_pwd) {
        this.sha_pwd = sha_pwd;
    }
    public String getImage() {
        return this.image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public String getName() {
        return this.name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getUid() {
        return this.uid;
    }
    public void setUid(String uid) {
        this.uid = uid;
    }
    public String getPhone() {
        return this.phone;
    }
    public void setPhone(String phone) {
        this.phone = phone;
    }

}
