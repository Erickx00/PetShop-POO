package org.example.petshoppoo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.petshoppoo.model.Login.Usuario;
import org.example.petshoppoo.services.AuthService;
import org.example.petshoppoo.services.UsuarioService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;

import java.io.IOException;

public class PerfilController extends BaseController {

    @FXML private TextField nome;
    @FXML private TextField email;
    @FXML private TextField telefone;

    private UsuarioService usuarioService;
    private AuthService authService;

    @FXML
    public void initialize() {
        try {
            validarSessao();
            Usuario u = session.getUsuarioLogado();

            this.usuarioService = new UsuarioService();
            this.authService = new AuthService();

            carregarDadosPerfil();
        } catch (Exception e) {
            AlertUtils.showError("Erro", "Erro ao inicializar: " + e.getMessage());
        }
    }

    private void carregarDadosPerfil() {
        if (!authService.temUsuarioLogado()) {
            AlertUtils.showError("Erro", "Nenhum usuário logado!");
            return;
        }

        nome.setText(usuarioService.obterNomeUsuarioLogado());
        email.setText(usuarioService.obterEmailUsuarioLogado());
        telefone.setText(usuarioService.obterTelefoneUsuarioLogado());
    }

    @FXML
    private void handleSalvar() {
        try {
            String novoNome = nome.getText().trim();
            String novoEmail = email.getText().trim();
            String novoTelefone = telefone.getText().trim();

            if (novoNome.isEmpty() || novoEmail.isEmpty() || novoTelefone.isEmpty()) {
                AlertUtils.showError("Campos vazios", "Preencha todos os campos!");
                return;
            }

            usuarioService.atualizarPerfil(
                    SessionManager.getUsuarioId(),
                    novoNome,
                    novoEmail,
                    novoTelefone
            );

            AlertUtils.showInfo("Sucesso", "Perfil atualizado com sucesso!");

        } catch (Exception e) {
            AlertUtils.showError("Erro ao salvar", e.getMessage());
        }
    }

    @FXML
    private void handleVoltar(ActionEvent event) {
        try {
            Parent root = FXMLLoader.load(getClass().getResource("/views/MenuView.fxml"));
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(new Scene(root));
            stage.show();
        } catch (IOException e) {
            AlertUtils.showError("Erro", "Não foi possível voltar ao menu.");
        }
    }
}