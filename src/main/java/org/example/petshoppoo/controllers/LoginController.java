package org.example.petshoppoo.controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import org.example.petshoppoo.services.AuthService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.ViewLoader;
import java.io.IOException;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private Button btnEntrar;
    @FXML private Hyperlink btnCadastrar;

    private AuthService authService;

    public LoginController() {
        try {
            this.authService = new AuthService();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            AlertUtils.showWarning("Aviso", "Preencha tudo!");
            return;
        }

        try {
            authService.login(email, senha);
            // Isso vai chamar a tela de menu com o tamanho automático
            ViewLoader.changeScene(btnEntrar, "/views/MenuView.fxml", "Menu Principal");
        } catch (Exception e) {
            AlertUtils.showError("Erro", e.getMessage());
        }
    }

    @FXML
    private void handleCadastrar() throws IOException {
        ViewLoader.changeScene(btnCadastrar, "/views/CriarContaView.fxml", "Cadastro");
    }
}