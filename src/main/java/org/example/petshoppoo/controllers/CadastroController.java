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
import org.example.petshoppoo.model.Login.exceptions.EmailInvalidoException;
import org.example.petshoppoo.repository.UsuarioRepository;
import org.example.petshoppoo.services.AuthService;
import org.example.petshoppoo.services.DonoService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;
import org.example.petshoppoo.utils.ViewLoader;

import java.io.IOException;

public class CadastroController {

    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtConfirmarSenha;

    private AuthService authService;
    private DonoService donoService;

    public CadastroController() {
        try {
            this.authService = new AuthService();
            this.donoService = new DonoService();
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Erro ao inicializar serviços: " + e.getMessage());
        }
    }

    @FXML
    private void handleCadastrar() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = txtSenha.getText();
        String confirmarSenha = txtConfirmarSenha.getText();

        if (!validarCampos(nome, email, senha, confirmarSenha)) {
            return;
        }

        try {
            boolean sucesso = authService.registrar(nome, email, senha);

            if (sucesso) {
                AlertUtils.showInfo("Sucesso", "Cadastro realizado com sucesso!");

                // Configurar sessão
                SessionManager.login(
                        AuthService.getUsuarioLogado(),
                        AuthService.getDonoLogado()
                );

                // Ir para tela principal
                irParaMainView();
            }
        } catch (EmailInvalidoException e) {
            AlertUtils.showError("Email Inválido", e.getMessage());
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Falha no cadastro: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void irParaMainView() {
        try {
            Stage stage = (Stage) txtNome.getScene().getWindow();
            ViewLoader.loadView(stage, "/views/MenuView.fxml", "Golden Pet", 936, 516);
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível carregar a tela principal");
        }
    }

    @FXML
    private void handleCancelar() {
        voltarParaLogin();
    }

    private boolean validarCampos(String nome, String email, String senha, String confirmarSenha) {
        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty() || confirmarSenha.isEmpty()) {
            AlertUtils.showError("Erro", "Preencha todos os campos");
            return false;
        }

        if (!senha.equals(confirmarSenha)) {
            AlertUtils.showError("Erro", "As senhas não coincidem");
            return false;
        }

        if (senha.length() < 6) {
            AlertUtils.showError("Erro", "A senha deve ter pelo menos 6 caracteres");
            return false;
        }

        return true;
    }

    private void voltarParaLogin() {
        try {
            Stage stage = (Stage) txtNome.getScene().getWindow();
            ViewLoader.loadView(stage, "views/LoginView.fxml", "Login", 400, 500);
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Não foi possível voltar para login");
        }
    }
}