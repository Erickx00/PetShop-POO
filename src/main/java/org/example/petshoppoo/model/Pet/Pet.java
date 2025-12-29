package org.example.petshoppoo.model.Pet;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import java.time.LocalDate;
import java.time.Period;
import java.util.UUID;

@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Cao.class, name = "Cao"),
        @JsonSubTypes.Type(value = Gato.class, name = "Gato")
})
public abstract class Pet {
    protected UUID id;
    protected String nome;
    protected String raca;
    protected LocalDate dataNascimento;
    protected double peso;
    protected UUID idUsuario;
    protected String tipo;

    public Pet() {
        this.id = UUID.randomUUID();
    }

    public Pet(UUID id, String nome, LocalDate dataNascimento, String raca,
               double peso, UUID idUsuario, String tipo) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.nome = nome;
        this.dataNascimento = dataNascimento;
        this.raca = raca;
        this.peso = peso;
        this.idUsuario = idUsuario;
        this.tipo = tipo;
    }

    public int calcularIdade() {
        if (dataNascimento == null) return 0;
        return Period.between(dataNascimento, LocalDate.now()).getYears();
    }

    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getRaca() { return raca; }
    public void setRaca(String raca) { this.raca = raca; }

    public LocalDate getDataNascimento() { return dataNascimento; }
    public void setDataNascimento(LocalDate dataNascimento) { this.dataNascimento = dataNascimento; }

    public double getPeso() { return peso; }
    public void setPeso(double peso) { this.peso = peso; }

    public UUID getIdUsuario() { return idUsuario; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }
}