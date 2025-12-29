package org.example.petshoppoo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.petshoppoo.services.AuthService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.ViewLoader;

import java.io.IOException;

public class CadastroController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtConfirmarSenha;
    @FXML private Button btnFinalizar;

    private AuthService authService;

    public CadastroController() {
        try {
            this.authService = new AuthService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCadastrar() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = txtSenha.getText();
        String confirmarSenha = txtConfirmarSenha.getText();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            AlertUtils.showWarning("Aviso", "Preencha todos os campos!");
            return;
        }

        if (!senha.equals(confirmarSenha)) {
            AlertUtils.showWarning("Aviso", "As senhas não coincidem!");
            return;
        }

        try {
            authService.registrar(nome, email, senha);
            AlertUtils.showInfo("Sucesso", "Cadastro realizado com sucesso!");


            ViewLoader.changeScene(btnFinalizar, "/views/LoginView.fxml", "Login");
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Falha ao cadastrar: " + e.getMessage());
        }
    }

    @FXML
    private void handleCancelar() throws IOException {

        ViewLoader.changeScene(btnFinalizar, "/views/LoginView.fxml", "Login");
    }
}