package org.example.petshoppoo.controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.services.ServiceFactory;
import org.example.petshoppoo.services.interfaces.IAuthService;
import org.example.petshoppoo.services.interfaces.IUsuarioService;
import org.example.petshoppoo.utils.AlertUtils;
import org.example.petshoppoo.utils.SessionManager;

import java.io.IOException;

public class PerfilController  {

    @FXML private TextField nome;
    @FXML private TextField email;
    @FXML private TextField telefone;
    @FXML private PasswordField senhaAtual;
    @FXML private PasswordField novaSenha;
    @FXML private PasswordField confirmarSenha;

    private IUsuarioService usuarioService;
    private IAuthService authService;

    @FXML
    public void initialize() {
        try {
            this.usuarioService = ServiceFactory.getUsuarioService();
            this.authService = ServiceFactory.getAuthService();

            carregarDadosPerfil();
        }
        catch (PersistenciaException e) {
            e.printStackTrace();
            AlertUtils.showError(
                    "Erro de Persistência",
                    "Não foi possível carregar os dados do sistema.\n" + e.getMessage()
            );
        }
        catch (Exception e) {
            AlertUtils.showError("Erro", "Erro ao inicializar: " + e.getMessage());
        }
    }

    private void carregarDadosPerfil() {
        if (!authService.temUsuarioLogado()) {
            AlertUtils.showError("Erro", "Nenhum usuário logado!");
            return;
        }

        nome.setText(SessionManager.getInstance().getUsuarioLogado().getNome());
        email.setText(SessionManager.getInstance().getUsuarioLogado().getEmail());
        telefone.setText(SessionManager.getInstance().getUsuarioLogado().getTelefone());

        // Limpar campos de senha
        limparCamposSenha();
    }

    private void limparCamposSenha() {
        senhaAtual.clear();
        novaSenha.clear();
        confirmarSenha.clear();
    }

    @FXML
    private void handleSalvar() {
        try {
            // Salvar dados do perfil
            String novoNome = nome.getText().trim();
            String novoEmail = email.getText().trim();
            String novoTelefone = telefone.getText().trim();

            if (novoNome.isEmpty() || novoEmail.isEmpty() || novoTelefone.isEmpty()) {
                AlertUtils.showError("Campos vazios", "Preencha todos os campos obrigatórios!");
                return;
            }

            usuarioService.atualizarPerfil(
                    SessionManager.getUsuarioId(),
                    novoNome,
                    novoEmail,
                    novoTelefone
            );

            // Verificar se o usuário quer alterar a senha
            if (!senhaAtual.getText().isEmpty() || !novaSenha.getText().isEmpty() || !confirmarSenha.getText().isEmpty()) {
                // Se algum campo de senha foi preenchido, tentar alterar a senha
                alterarSenha();
            }

            AlertUtils.showInfo("Sucesso", "Perfil atualizado com sucesso!");
            limparCamposSenha();

        } catch (Exception e) {
            AlertUtils.showError("Erro ao salvar", e.getMessage());
        }
    }

    private void alterarSenha() throws Exception {
        String senhaAtualText = senhaAtual.getText().trim();
        String novaSenhaText = novaSenha.getText().trim();
        String confirmarSenhaText = confirmarSenha.getText().trim();

        // Verificar se todos os campos de senha estão preenchidos
        if (senhaAtualText.isEmpty() || novaSenhaText.isEmpty() || confirmarSenhaText.isEmpty()) {
            throw new Exception("Para alterar a senha, preencha todos os campos de senha!");
        }

        // Verificar se as novas senhas coincidem
        if (!novaSenhaText.equals(confirmarSenhaText)) {
            throw new Exception("As novas senhas não coincidem!");
        }

        // Verificar se a nova senha é diferente da atual
        if (senhaAtualText.equals(novaSenhaText)) {
            throw new Exception("A nova senha deve ser diferente da senha atual!");
        }

        // Verificar tamanho da nova senha
        if (novaSenhaText.length() < 6) {
            throw new Exception("A nova senha deve ter pelo menos 6 caracteres!");
        }

        // Chamar o serviço para alterar a senha
        usuarioService.alterarSenha(
                SessionManager.getUsuarioId(),
                senhaAtualText,
                novaSenhaText
        );
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