package com.demo.iam_demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"permissions"})
@ToString(callSuper = true, exclude = {"permissions"})
@SQLDelete(sql = "UPDATE roles SET is_deleted = true, updated_at = NOW() WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Role extends BaseEntity{

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")

    )
    @Builder.Default
    private Set<Permission> permissions = new HashSet<>();
}
