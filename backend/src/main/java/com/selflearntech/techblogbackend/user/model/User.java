package com.selflearntech.techblogbackend.user.model;

import com.selflearntech.techblogbackend.article.model.Article;
import com.selflearntech.techblogbackend.article.model.Draft;
import com.selflearntech.techblogbackend.token.model.Token;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @NotBlank
    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @NotBlank
    @Column(name = "last_name", length = 50)
    private String lastName;

    @Email
    @Column(unique = true, nullable = false, length = 50)
    private String email;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(name = "profile_img")
    private String profileImg;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private Token token;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> authorities = new HashSet<>();

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Draft> drafts;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Article> articles;


    @Override
    public String getUsername() {
        return this.email;
    }

}
