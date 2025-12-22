package org.example.petshoppoo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.net.PasswordAuthentication;

public class CadastroController {

    @FXML
    private TextField txtNome;

    @FXML
    private TextField TxtEmail;

    @FXML
    private PasswordField txtSenha;

    @FXML
    private PasswordField txtSenhaConfirmada;

    public void handleCadastrar(ActionEvent actionEvent) {

    }
}
