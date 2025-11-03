package com.demo.iam_demo.service;

import com.demo.iam_demo.model.Role;
import com.demo.iam_demo.model.User;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AllArgsConstructor
@Getter
public class UserDetailsImpl implements UserDetails {

    private final Long id;
    private final String loginName;
    private final String email;

    @JsonIgnore
    private final String password;
    private final boolean accountNonLocked;
    private final Collection<? extends GrantedAuthority> authorities;

    //tạo UserDetailsImpl từ entity User
    public static UserDetailsImpl build(User user){
        Collection<GrantedAuthority> authorities = Stream.concat(
                // lấy roles, thêm tiền tố ROLE_ nếu chưa có
                user.getRoles().stream()
                    .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName())),

                user.getRoles().stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .map(permission -> new SimpleGrantedAuthority(permission.getName()))
        ).distinct().collect(Collectors.toSet());

        return new UserDetailsImpl(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getPassword(),
                !user.isLocked(),
                authorities
        );
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return authorities;
    }

    //Spring Security dùng username để login
    @Override
    public String getUsername(){
        return email; // dùng email làm username
    }

    @Override
    public String getPassword(){
        return password;
    }

    //tạm thời để toàn bộ return true -> account luôn active, không bị khóa, không hết hạn
    @Override
    public boolean isAccountNonExpired(){
        return true;
    }

    @Override
    public boolean isAccountNonLocked(){
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired(){
        return true;
    }

    @Override
    public boolean isEnabled(){
        return true;
    }

    public Long getId(){
        return id;
    }

    public String getEmail(){
        return email;
    }
}
