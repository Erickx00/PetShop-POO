package org.example.petshoppoo.model.Login;

import org.example.petshoppoo.model.Login.exceptions.EmailInvalidoException;
import java.util.UUID;

public class Usuario {
    private UUID id;
    private String usuario;
    private String email;
    private String senha;

    public Usuario() {
    }

    public Usuario(String nome, String email, String senha) {
        this.id = UUID.randomUUID();
        this.usuario = nome;
        this.email = email;
        this.senha = senha;
    }

    public Usuario(UUID id, String usuario, String email, String senha) {
        validarEmail(email);
        this.id = id == null ? UUID.randomUUID() : id;
        this.usuario = usuario;
        this.email = email;
        this.senha = senha;
    }

    public void validarEmail(String email) throws EmailInvalidoException {
        if (email == null || !email.endsWith("@gmail.com")) {
            throw new EmailInvalidoException("Email Invalido");
        }
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }
    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}