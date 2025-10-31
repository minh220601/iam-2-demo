package com.demo.iam_demo.model;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "permissions")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(callSuper = true, exclude = {"roles"})
@ToString(callSuper = true, exclude = {"roles"})
@SQLDelete(sql = "UPDATE permissions SET is_deleted = true, updated_at = NOW() WHERE id = ?")
@Where(clause = "is_deleted = false")
public class Permission extends BaseEntity{

    @Column(nullable = false, unique = true, length = 100)
    private String name;

    @Column(length = 255)
    private String description;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.LAZY)
    @Builder.Default
    private Set<Role> roles = new HashSet<>();
}
