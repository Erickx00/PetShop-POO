package org.example.petshoppoo.model.Dono;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

public class Dono  {
    private UUID id;
    private String nome;
    private String telefone;
    private String email;
    private List<UUID> idsPets;
    private boolean ativo;

    public Dono(){
        this.id = UUID.randomUUID();
        this.idsPets = new ArrayList<>();
        this.ativo = true;
    }

    public Dono(UUID id, String nome, String cpf, String telefone, String email) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.nome = nome;
        this.telefone = telefone;
        this.email = email;
        this.idsPets = new ArrayList<>();
        this.ativo = true;
    }

    public void adicionarPet(UUID idPet) {
        if (idPet != null && !idsPets.contains(idPet)) {
            idsPets.add(idPet);
        }
    }

    public void removerPet(UUID idPet) {
        idsPets.remove(idPet);
    }

    public int getQuantidadePets() {
        return idsPets.size();
    }

    public boolean temPets() {
        return !idsPets.isEmpty();
    }

    public String getNomeCompleto() {
        return nome;
    }

    public String getPrimeiroNome() {
        return nome != null && nome.contains(" ") ?
                nome.substring(0, nome.indexOf(" ")) : nome;
    }

    public UUID getId() {
        return id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }


    public List<UUID> getIdsPets() {
        return new ArrayList<>(idsPets); }

    public void setIdsPets(List<UUID> idsPets) {
        this.idsPets = idsPets != null ? idsPets : new ArrayList<>();
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Dono dono = (Dono) o;
        return Objects.equals(id, dono.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Dono{" +
                "nome='" + nome + '\'' +
                ", telefone='" + telefone + '\'' +
                ", quantidadePets=" + getQuantidadePets() +
                ", ativo=" + ativo +
                '}';
    }
}
