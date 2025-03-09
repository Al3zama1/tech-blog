package com.selflearntech.techblogbackend.user.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.security.core.GrantedAuthority;

@Document
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Role implements GrantedAuthority {
    @Id
    private String id;
    @Indexed(unique = true)
    private RoleType authority;

    public String getAuthority() {
        return authority.name();
    }

    public void setAuthority(RoleType authorityf) {
        this.authority = authority;
    }


}
