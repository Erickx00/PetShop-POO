package org.example.petshoppoo.model.Login;

import org.example.petshoppoo.model.Login.exceptions.EmailInvalidoException;
import org.example.petshoppoo.model.Usuario.Perfil;

import java.util.UUID;

public class Usuario {
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private Perfil perfil;
    private UUID idDono;


    public Usuario() {
        this.id = UUID.randomUUID();
        this.perfil = Perfil.CLIENTE;
    }

    public Usuario(String nome, String email, String senha) throws EmailInvalidoException {
        this();
        this.nome = nome;
        setEmail(email);
        this.senha = senha;
    }

    public void setEmail(String email) throws EmailInvalidoException {
        if (email == null || !email.endsWith("@gmail.com")) {
            throw new EmailInvalidoException("Email Invalido");
        }
        this.email = email;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
    public UUID getIdDono() { return idDono; }
    public void setIdDono(UUID idDono) { this.idDono = idDono; }
}