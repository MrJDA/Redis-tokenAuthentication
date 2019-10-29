package com.oocl.web.enums;

import lombok.Getter;

@Getter
public enum RoleEnum {

    CUSTOMER(0, "CUSTOMER"),
    MANAGER(1, "MANAGER"),
    DEVELOPER(2, "DEVELOPER"),
    ;
    private Integer value;
    private String roleName;

    RoleEnum(Integer value, String roleName){
        this.value = value;
        this.roleName = roleName;
    }

    public static String getRole(Integer value){
        for (RoleEnum roleEnum : RoleEnum.values()) {
            if(roleEnum.getValue() == value)return roleEnum.getRoleName();
        }
        return null;
    }

    public static Integer getValue(String roleName){
        for (RoleEnum roleEnum : RoleEnum.values()){
            if(roleEnum.getRoleName().equals(roleName))return  roleEnum.getValue();
        }
        return null;
    }
}
