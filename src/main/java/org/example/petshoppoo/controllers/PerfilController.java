package org.example.petshoppoo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

import java.awt.*;
import java.io.IOException;

public class PerfilController {


    @FXML private Button btnVoltar;
    @FXML private TextArea txtNome;
    @FXML private TextArea txtEmail;
    @FXML private TextArea txtTelefone;
    @FXML private TableView<String> tableViewAnimais;
    @FXML private TableColumn<String,String> colNome;
    @FXML private TableColumn<String,String> colEspecie;
    @FXML private TableColumn<String,String> colRaca;
    @FXML private TableColumn<Integer,Integer> colIdade;





    public void voltar(ActionEvent event) {
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
