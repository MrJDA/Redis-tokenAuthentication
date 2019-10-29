package com.oocl.web.entities;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Permission implements Serializable {
    private String permissionId;
    private String permissionName;
    private String permissionDescription;
}
