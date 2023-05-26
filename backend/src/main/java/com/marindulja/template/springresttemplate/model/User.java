package com.marindulja.template.springresttemplate.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.Hibernate;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.ArrayList;
import java.util.List;
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
    @Column(name = "full_name", nullable = false)
    private String fullName;

    @NotBlank(message = "Job title is mandatory")
    @Column(name = "job_title", nullable = false)
    private String jobTitle;
    private Role role;

    @OneToMany(
            mappedBy = "user"
    )
    private List<Order> orders = new ArrayList<>();

    @Column(name= "is_deleted")
    private boolean isDeleted;


    public User(String username, String password, String fullName, String jobTitle, Role role) {
        this.username = username;
        this.password = password;
        this.fullName = fullName;
        this.jobTitle = jobTitle;
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
