package com.demo.iam_demo.dto.request;

import lombok.Data;

import java.util.Set;

@Data
public class RoleRequest {
    private String name;
    private String description;
    private Set<String> permissions;
}
