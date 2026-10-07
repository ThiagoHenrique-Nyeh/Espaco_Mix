package com.example.espacomix.model;

import jakarta.persistence.*;

@Entity
@Table(name = "administrador")
public class Administrador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "senha_admin", length = 30, nullable = false)
    private String senhaAdmin;

    @Column(unique = true, length = 80, nullable = false)
    private String email;

    public Administrador() {
    }

    public Administrador(Long id, String senhaAdmin, String email) {
        this.id = id;
        this.senhaAdmin = senhaAdmin;
        this.email = email;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSenhaAdmin() {
        return senhaAdmin;
    }

    public void setSenhaAdmin(String senhaAdmin) {
        this.senhaAdmin = senhaAdmin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}