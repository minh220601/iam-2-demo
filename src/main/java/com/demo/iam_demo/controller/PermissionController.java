package com.demo.iam_demo.controller;

import com.demo.iam_demo.dto.request.PermissionRequest;
import com.demo.iam_demo.dto.response.PermissionResponse;
import com.demo.iam_demo.service.PermissionService;
import jakarta.ws.rs.Path;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/permissions")
@RequiredArgsConstructor
public class PermissionController {
    private final PermissionService permissionService;

    // tạo permission
    @PostMapping
    public ResponseEntity<PermissionResponse> createPermission(@RequestBody PermissionRequest request){
        return ResponseEntity.ok(permissionService.createPermission(request));
    }

    // lấy danh sách permission
    @GetMapping
    public ResponseEntity<List<PermissionResponse>> getAllPermissions(){
        return ResponseEntity.ok(permissionService.getAllPermissions());
    }

    // lấy chi tiết permission
    @GetMapping("/{id}")
    public ResponseEntity<PermissionResponse> getPermissionById(@PathVariable Long id){
        return ResponseEntity.ok(permissionService.getPermissionById(id));
    }

    // cập nhật permission
    @PutMapping("/{id}")
    public ResponseEntity<PermissionResponse> updatePermission(@PathVariable Long id, @RequestBody PermissionRequest request){
        return ResponseEntity.ok(permissionService.updatePermission(id, request));
    }

    // xóa permission (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id){
        permissionService.deletePermission(id);
        return ResponseEntity.noContent().build();
    }
}
