package com.wlld.myjecs.sqlEntity;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Admin {
    private int id;//id
    private String account;//账号
    private String pass_word;//密码
    private int pass;//账号是否通过，0未审核，1通过
    private String name;//账户拥有者姓名
}
