package io.morosanu.demographql.domain;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Set;

public enum Role {
    USER(Set.of(Permission.READ)),
    MODERATOR(Set.of(Permission.READ, Permission.UPDATE)),
    ADMIN(Set.of(Permission.READ, Permission.UPDATE, Permission.CREATE, Permission.DELETE));
    private Set<Permission> permissions;

    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

    public Set<Permission> getPermissions() {
        return permissions;
    }

    public List<SimpleGrantedAuthority> getAuthorities() {
        return permissions.stream()
                .map(permission -> new SimpleGrantedAuthority(permission.name()))
                .toList();
    }
}
