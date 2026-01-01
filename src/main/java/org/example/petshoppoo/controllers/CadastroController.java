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
    @FXML private TextField txtTelefone;
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
    public void handleCadastrar() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String telefone = txtTelefone.getText();
        String senha = txtSenha.getText();
        String confirmarSenha = txtConfirmarSenha.getText();

        if (nome.isEmpty() || email.isEmpty() || telefone.isEmpty() || senha.isEmpty()) {
            AlertUtils.showWarning("Aviso", "Preencha todos os campos!");
            return;
        }

        try {
            authService.registrar(nome, email, telefone, senha);
            AlertUtils.showInfo("Sucesso", "Cadastro realizado!");
            ViewLoader.changeScene(btnFinalizar, "/views/LoginView.fxml", "Login");
        } catch (Exception e) {
            AlertUtils.showError("Erro", e.getMessage());
        }
    }

    @FXML
    public void handleCancelar() throws IOException {
        ViewLoader.changeScene(btnFinalizar, "/views/LoginView.fxml", "Login");
    }
}