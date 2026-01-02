package org.example.petshoppoo.model.Login;

import org.example.petshoppoo.model.Login.exceptions.EmailInvalidoException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Usuario {
    private UUID id;
    private String nome;
    private String email;
    private String senha;
    private String telefone;
    private Perfil perfil;
    private List<UUID> idsPets;

    public Usuario() {
        this.id = UUID.randomUUID();
        this.perfil = Perfil.CLIENTE;
        this.idsPets = new ArrayList<>();
    }

    public Usuario(String nome, String email, String telefone, String senha) throws EmailInvalidoException {
        this();
        this.nome = nome;
        this.telefone = telefone;
        setEmail(email);
        this.senha = senha;
    }

    public void setEmail(String email) throws EmailInvalidoException {
        if (email == null) throw new EmailInvalidoException("Email nao pode ser nulo");
        email = email.trim().toLowerCase();
        if (!email.endsWith("@gmail.com")) {
            throw new EmailInvalidoException("Email deve terminar com @gmail.com");
        }
        this.email = email;
    }

    public void adicionarPet(UUID idPet) {
        if (this.idsPets == null) {
            this.idsPets = new ArrayList<>();
        }
        if (!this.idsPets.contains(idPet)) {
            this.idsPets.add(idPet);
        }
    }

    public void removerPet(UUID idPet) {
        idsPets.remove(idPet);
    }

    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getEmail() { return email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public Perfil getPerfil() { return perfil; }
    public void setPerfil(Perfil perfil) { this.perfil = perfil; }
    public List<UUID> getIdsPets() { return idsPets; }
    public void setIdsPets(List<UUID> idsPets) { this.idsPets = idsPets; }

    public boolean verificarSenha(String senha) {
        return Objects.equals(this.senha, senha);
    }
}