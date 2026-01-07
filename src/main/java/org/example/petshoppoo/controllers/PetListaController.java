
package org.example.petshoppoo.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.petshoppoo.model.Pet.Pet;

import java.io.IOException;
import java.io.InputStream;


public class PetListaController {

    @FXML private TableView<Pet> tabelaPets;
    @FXML private TableColumn<Pet, String> clNome;
    @FXML private TableColumn<Pet, String> clEspecie;
    @FXML private TableColumn<Pet, String> clRaca;
    @FXML private TableColumn<Pet, String> clIdade;
    @FXML private TableColumn<Pet, Double> clPeso;

    @FXML
    public void initialize() {
        clNome.setCellValueFactory(new PropertyValueFactory<>("nome"));
        clEspecie.setCellValueFactory(new PropertyValueFactory<>("tipo"));
        clRaca.setCellValueFactory(new PropertyValueFactory<>("raca"));
        clIdade.setCellValueFactory(new PropertyValueFactory<>("dataNascimento"));
        clPeso.setCellValueFactory(new PropertyValueFactory<>("peso"));
        carregarPets();
    }

    private void carregarPets() {
        try {
            ObjectMapper mapper = new ObjectMapper();
            InputStream is = getClass().getResourceAsStream("/data/pets.json");

            if (is != null) {

                Pet[] petsArray = mapper.readValue(is, Pet[].class);


                ObservableList<Pet> listaDePets = FXCollections.observableArrayList(petsArray);


                tabelaPets.setItems(listaDePets);

                System.out.println("Tabela de pets carregada com sucesso!");
            } else {
                System.err.println("Arquivo não encontrado!");
            }
        } catch (Exception e) {
            System.err.println("Erro ao processar JSON de pets: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    public void handleVoltar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/MenuView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
