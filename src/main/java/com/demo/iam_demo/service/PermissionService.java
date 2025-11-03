package com.demo.iam_demo.service;

import com.demo.iam_demo.dto.request.PermissionRequest;
import com.demo.iam_demo.dto.response.PermissionResponse;
import com.demo.iam_demo.exception.AppException;
import com.demo.iam_demo.exception.ErrorCode;
import com.demo.iam_demo.model.Permission;
import com.demo.iam_demo.repository.PermissionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PermissionService {
    private final PermissionRepository permissionRepository;

    // tạo permission
    public PermissionResponse createPermission(PermissionRequest request){
        if(permissionRepository.findByName(request.getName()).isPresent()){
            throw new AppException(ErrorCode.PERMISSION_ALREADY_EXISTS, "Permission already exists" + request.getName());
        }

        Permission permission = Permission.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        return mapToResponse(permissionRepository.save(permission));
    }

    // lấy danh sách permission
    public List<PermissionResponse> getAllPermissions(){
        return permissionRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // lấy permission theo id
    public PermissionResponse getPermissionById(Long id){
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND, "Permission not found"));
        return mapToResponse(permission);
    }

    // cập nhật permission
    public PermissionResponse updatePermission(Long id, PermissionRequest request){
        Permission existing = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND, "Permission not found"));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());
        return mapToResponse(permissionRepository.save(existing));
    }

    // xóa mềm
    public void deletePermission(Long id){
        Permission permission = permissionRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND));
        permission.setDeleted(true);
        permissionRepository.save(permission);
    }

    // helper: map entity -> response
    private PermissionResponse mapToResponse(Permission permission){
        PermissionResponse response = new PermissionResponse();
        response.setId(permission.getId());
        response.setName(permission.getName());
        response.setDescription(permission.getDescription());
        return response;
    }
}
