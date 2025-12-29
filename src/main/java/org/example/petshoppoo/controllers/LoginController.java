package org.example.petshoppoo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.UsuarioRepository;
import org.example.petshoppoo.services.AuthService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;

import java.io.IOException;

public class LoginController {

    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;

    private AuthService authService;

    public LoginController() {
        try {
            this.authService = new AuthService();
        } catch (PersistenciaException e) {
            AlertUtils.showError("Erro", "Erro ao inicializar sistema: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        if (email.isEmpty() || senha.isEmpty()) {
            AlertUtils.showError("Erro", "Preencha todos os campos");
            return;
        }

        try {
            if (authService.login(email, senha)) {
                // Configurar sessão
                SessionManager.login(
                        AuthService.getUsuarioLogado(),
                        AuthService.getDonoLogado()
                );

                System.out.println("Sessão configurada. Dono ID: " +
                        (AuthService.getDonoLogado() != null ? AuthService.getDonoLogado().getId() : "null"));

                carregarMainView();
            } else {
                AlertUtils.showError("Erro", "Email ou senha incorretos");
            }
        } catch (PersistenciaException e) {
            AlertUtils.showError("Erro", "Falha na autenticação: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCadastrar() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/CriarContaView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 500));
            stage.setTitle("Criar Conta");
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível carregar a tela de cadastro");
        }
    }

    private void carregarMainView() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/MenuView.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(new Scene(root, 400, 500));
            stage.setTitle("Golden Pet");
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível carregar a tela principal");
        }
    }
}