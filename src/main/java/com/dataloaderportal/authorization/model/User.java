package com.dataloaderportal.authorization.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;


@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@ToString
public class User {
    @Id
    @Column(name="username")
    public String username;

    @Column(name="password")
    public String password;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
    }
}