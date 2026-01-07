package org.example.petshoppoo.utils;

import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleStringProperty;

public class PetTableData {
    private final Object pet; // Referência ao objeto original
    private final SimpleStringProperty nome;
    private final SimpleStringProperty especie;
    private final SimpleStringProperty raca;
    private final SimpleStringProperty idade;
    private final SimpleDoubleProperty peso;

    public PetTableData(Object pet, String nome, String especie, String raca, String idade, double peso) {
        this.pet = pet;
        this.nome = new SimpleStringProperty(nome);
        this.especie = new SimpleStringProperty(especie);
        this.raca = new SimpleStringProperty(raca);
        this.idade = new SimpleStringProperty(idade);
        this.peso = new SimpleDoubleProperty(peso);
    }

    public Object getPet() { return pet; }
    public String getNome() { return nome.get(); }
    public String getEspecie() { return especie.get(); }
    public String getRaca() { return raca.get(); }
    public String getIdade() { return idade.get(); }
    public double getPeso() { return peso.get(); }

    public SimpleStringProperty nomeProperty() { return nome; }
    public SimpleStringProperty especieProperty() { return especie; }
    public SimpleStringProperty racaProperty() { return raca; }
    public SimpleStringProperty idadeProperty() { return idade; }
    public SimpleDoubleProperty pesoProperty() { return peso; }
}