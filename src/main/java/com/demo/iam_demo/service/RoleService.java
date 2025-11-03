package com.demo.iam_demo.service;

import com.demo.iam_demo.dto.request.RoleRequest;
import com.demo.iam_demo.dto.response.RoleResponse;
import com.demo.iam_demo.exception.AppException;
import com.demo.iam_demo.exception.ErrorCode;
import com.demo.iam_demo.model.Permission;
import com.demo.iam_demo.model.Role;
import com.demo.iam_demo.repository.PermissionRepository;
import com.demo.iam_demo.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    // tạo role
    public RoleResponse createRole(RoleRequest request){
        if(roleRepository.findByName(request.getName()).isPresent()){
            throw new AppException(ErrorCode.ROLE_ALREADY_EXISTS, "Role already exists: " + request.getName());
        }

        Role role = Role.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();

        // gán quyền nếu có
        if(request.getPermissions() != null && !request.getPermissions().isEmpty()){
            Set<Permission> permissions = request.getPermissions().stream()
                    .map(permissionName -> permissionRepository.findByName(permissionName)
                            .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND, "Permission not found: " + permissionName)))
                    .collect(Collectors.toSet());
            role.setPermissions(permissions);
        }

        Role saved = roleRepository.save(role);
        return mapToResponse(saved);
    }

    // lấy danh sách role
    public List<RoleResponse> getAllRoles(){
        return roleRepository.findAll()
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    // lấy role theo ID
    public RoleResponse getRoleById(Long id){
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND, "Role not found"));
        return mapToResponse(role);
    }

    // cập nhật role
    public RoleResponse updateRole(Long id, RoleRequest request){
        Role existing = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND, "Role not found"));
        existing.setName(request.getName());
        existing.setDescription(request.getDescription());

        if(request.getPermissions() != null && !request.getPermissions().isEmpty()){
            Set<Permission> permissions = request.getPermissions().stream()
                    .map(permissionName -> permissionRepository.findByName(permissionName)
                            .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND, "Permission not found: " + permissionName)))
                    .collect(Collectors.toSet());
            existing.setPermissions(permissions);
        }

        return mapToResponse(roleRepository.save(existing));
    }

    // xóa mềm role
    public void deleteRole(Long id){
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));
        role.setDeleted(true);
        roleRepository.save(role);
    }

    // gán permission cho role
    public RoleResponse assignPermissions(Long roleId, Set<String> permissionNames){
        Role role = roleRepository.findById(roleId)
                .orElseThrow(() -> new AppException(ErrorCode.ROLE_NOT_FOUND));

        Set<Permission> permissions = permissionNames.stream()
                .map(name -> permissionRepository.findByName(name)
                        .orElseThrow(() -> new AppException(ErrorCode.PERMISSION_NOT_FOUND, "Permission not found: " + name)))
                .collect(Collectors.toSet());

        role.setPermissions(permissions);
        return mapToResponse(roleRepository.save(role));
    }

    // helper: map entity -> response
    private RoleResponse mapToResponse(Role role){
        RoleResponse response = new RoleResponse();
        response.setId(role.getId());
        response.setName(role.getName());
        response.setDescription(role.getDescription());
        response.setPermissions(
                role.getPermissions().stream()
                        .map(Permission::getName)
                        .collect(Collectors.toSet())
        );
        return response;
    }
}
