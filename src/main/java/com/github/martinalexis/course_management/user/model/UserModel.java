package com.github.martinalexis.course_management.user.model;

import jakarta.persistence.*;
import lombok.*;

/**
 * JPA entity representing an application user.
 *
 * <p>Stores identity and contact information. Credentials may be absent for
 * accounts created via OAuth2 providers.</p>
 *
 * @since 1.0
 */
@Entity
@Table(name = "users")
@Getter
@Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_users")
    @EqualsAndHashCode.Include
    private Long idUser;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String lastname;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Password hash for local accounts; may be null for OAuth2 users where
     * credentials are managed by the external identity provider.
     */
    @Column(nullable = true)
    private String password;
}
