package com.demo.iam_demo.controller;

import com.demo.iam_demo.dto.request.RoleRequest;
import com.demo.iam_demo.dto.response.RoleResponse;
import com.demo.iam_demo.service.RoleService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/api/roles")
@RequiredArgsConstructor
public class RoleController {
    private final RoleService roleService;

    // tạo role
    @PostMapping
    public ResponseEntity<RoleResponse> createRole(@RequestBody RoleRequest request){
        return ResponseEntity.ok(roleService.createRole(request));
    }

    // lấy danh sách role
    @GetMapping
    public ResponseEntity<List<RoleResponse>> getAllRoles(){
        return ResponseEntity.ok(roleService.getAllRoles());
    }

    // lấy chi tiết role theo id
    @GetMapping("/{id}")
    public ResponseEntity<RoleResponse> getRoleById(@PathVariable Long id){
        return ResponseEntity.ok(roleService.getRoleById(id));
    }

    // cập nhật role
    @PutMapping("/{id}")
    public ResponseEntity<RoleResponse> updateRole(
            @PathVariable Long id,
            @RequestBody RoleRequest request
    ){
        return ResponseEntity.ok(roleService.updateRole(id, request));
    }

    // xóa role (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteRole(@PathVariable Long id){
        roleService.deleteRole(id);
        return ResponseEntity.noContent().build();
    }

    // gán danh sách permission cho role
    @PostMapping("/{roleId}/permissions")
    public ResponseEntity<RoleResponse> assignPermissions(@PathVariable Long roleId, @RequestBody Set<String> permissionNames){
        return ResponseEntity.ok(roleService.assignPermissions(roleId, permissionNames));
    }
}
