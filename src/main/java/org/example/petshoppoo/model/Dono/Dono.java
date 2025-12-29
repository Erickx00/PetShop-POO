package org.example.petshoppoo.model.Dono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Dono {
    private UUID id;
    private String nome;
    private String telefone;
    private String email;


    public Dono() {
        this.id = UUID.randomUUID();
    }

    public Dono(UUID id, String nome, String telefone, String email) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public void setId(UUID id) {this.id = id;}
}
