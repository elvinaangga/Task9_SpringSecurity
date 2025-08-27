package com.task9_springsecurity.model;

import org.springframework.security.core.GrantedAuthority;
import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Access(AccessType.FIELD)
public class Role implements GrantedAuthority {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String name; // contoh: "ROLE_ADMIN", "ROLE_USER"

    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();


    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    @Override
    public String getAuthority() {
        return name;
    }
}