package com.solovev.model;

import com.solovev.dto.DTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import javax.persistence.*;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor(force = true)
@AllArgsConstructor
public class User implements DTO {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @NonNull
    @Column(unique = true, nullable = false)
    private String login;

    private String password;

    private String name;

    public User(@NonNull String login, String password, String name) {
        this.login = login;
        this.password = password;
        this.name = name;
    }
}
