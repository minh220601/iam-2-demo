package com.demo.iam_demo.dto.request;

import lombok.Data;

@Data
public class PermissionRequest {
    private String name;
    private String description;
}
