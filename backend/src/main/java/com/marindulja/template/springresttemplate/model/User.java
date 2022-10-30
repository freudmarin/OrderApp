package com.marindulja.template.springresttemplate.model;

import lombok.*;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.Objects;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "user_detail")
public class User extends BaseEntity<Long> {

    @NotBlank(message = "Username is mandatory")
    @Column(nullable = false, unique = true)
    private String username;

    @NotBlank(message = "Password is mandatory")
    @Column(nullable = false)
    private String password;

    @NotBlank(message = "Full name is mandatory")
    @Column(name = "full_name",nullable = false)
    private String fullName;

    @NotBlank(message = "Job title is mandatory")
    @Column(name = "job_title",nullable = false)
    private String jobTitle;

    @ManyToOne()
    @JoinTable(name = "user_roles",
            joinColumns =
                    { @JoinColumn(name = "user_id", referencedColumnName = "id") },
            inverseJoinColumns =
                    { @JoinColumn(name = "role_id", referencedColumnName = "id") })
    private Role role;

    public User(Long id,  @NonNull String username, Role role) {
        super(id);
        this.username = username;
        this.role = role;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        User user = (User) o;
        return getId() != null && Objects.equals(getId(), user.getId());
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
