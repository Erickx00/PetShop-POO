package org.example.petshoppoo.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.repository.UsuarioRepository;
import java.io.IOException;

public class LoginController {
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    @FXML
    private void HandleAcessar() {
        String email = txtEmail.getText();
        String senha = txtSenha.getText();

        Usuario usuario = usuarioRepository.buscarPorEmail(email);

        if (usuario != null && usuario.getSenha().equals(senha)) {
            irParaMenu();
        } else {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "E-mail ou senha incorretos.");
        }
    }

    @FXML
    private void HandleCadastrar() {
        carregarTela("/org/example/petshoppoo/resources/views/cadastro.fxml", "Golden Pet");
    }

    private void irParaMenu() {
        carregarTela("/org/example/petshoppoo/resources/views/menu.fxml", "Golden Pet");
    }

    private void carregarTela(String fxmlPath, String titulo) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Parent root = loader.load();
            Stage stage = (Stage) txtEmail.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle(titulo);
        } catch (IOException e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Nao encontrei: " + fxmlPath);
            e.printStackTrace();
        }
    }

    private void exibirAlerta(Alert.AlertType tipo, String titulo, String msg) {
        Alert alert = new Alert(tipo);
        alert.setTitle(titulo);
        alert.setHeaderText(null);
        alert.setContentText(msg);
        alert.showAndWait();
    }
}