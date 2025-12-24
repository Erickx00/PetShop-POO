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
import org.example.petshoppoo.model.Login.exceptions.EmailInvalidoException;
import org.example.petshoppoo.repository.UsuarioRepository;
import java.io.IOException;

public class CadastroController {
    @FXML private TextField txtNome;
    @FXML private TextField txtEmail;
    @FXML private PasswordField txtSenha;
    @FXML private PasswordField txtSenhaConfirmada;

    private final UsuarioRepository usuarioRepository = new UsuarioRepository();

    @FXML
    private void handleCadastrar() {
        String nome = txtNome.getText();
        String email = txtEmail.getText();
        String senha = txtSenha.getText();
        String confirmacao = txtSenhaConfirmada.getText();

        if (nome.isEmpty() || email.isEmpty() || senha.isEmpty()) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Preencha todos os campos!");
            return;
        }

        if (!senha.equals(confirmacao)) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "As senhas nao coincidem!");
            return;
        }

        try {
            Usuario novoUsuario = new Usuario(nome, email, senha);
            novoUsuario.validarEmail(email);

            usuarioRepository.adicionar(novoUsuario);
            exibirAlerta(Alert.AlertType.INFORMATION, "Sucesso", "Cadastro realizado!");
            irParaLogin();

        } catch (EmailInvalidoException e) {
            exibirAlerta(Alert.AlertType.WARNING, "E-mail Invalido", e.getMessage());
        } catch (Exception e) {
            exibirAlerta(Alert.AlertType.ERROR, "Erro", "Falha ao processar cadastro.");
            e.printStackTrace();
        }
    }

    private void irParaLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/org/example/petshoppoo/resources/views/login.fxml"));
            Parent root = loader.load();
            Stage stage = (Stage) txtNome.getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.setTitle("Golden Pet");
            stage.show();
        } catch (IOException e) {
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