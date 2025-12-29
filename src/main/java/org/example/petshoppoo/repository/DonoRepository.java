package org.example.petshoppoo.repository;

import org.example.petshoppoo.exceptions.PersistenciaException;
import org.example.petshoppoo.model.Dono.Dono;
import org.example.petshoppoo.utils.FilePaths;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class DonoRepository {
    private List<Dono> donos;

    public DonoRepository() {
        try {
            carregarDonos();
        } catch (PersistenciaException e) {
            System.err.println("Erro ao carregar donos, iniciando lista vazia: " + e.getMessage());
            this.donos = new ArrayList<>();
        }
    }

    private void carregarDonos() throws PersistenciaException {
        System.out.println("Carregando donos de: " + FilePaths.DONOS_JSON);
        this.donos = JsonFileManager.carregar(FilePaths.DONOS_JSON, Dono.class);
        System.out.println("Donos carregados: " + donos.size());
    }

    public void adicionar(Dono dono) throws PersistenciaException {
        // Verificar se o dono já existe (por email)
        if (buscarPorEmail(dono.getEmail()) != null) {
            throw new PersistenciaException("Já existe um dono com este email");
        }

        donos.add(dono);
        salvarDonos();
        System.out.println("Dono adicionado: " + dono.getNome());
    }

    public void salvarDono(Dono dono) throws PersistenciaException {
        // Se o dono já existe, atualiza; se não, adiciona
        boolean encontrado = false;
        for (int i = 0; i < donos.size(); i++) {
            if (donos.get(i).getId().equals(dono.getId())) {
                donos.set(i, dono);
                encontrado = true;
                break;
            }
        }

        if (!encontrado) {
            donos.add(dono);
        }

        salvarDonos();
    }

    public Dono buscarPorId(UUID id) {
        if (id == null) return null;

        return donos.stream()
                .filter(dono -> dono.getId().equals(id))
                .findFirst()
                .orElse(null);
    }

    public Dono buscarPorEmail(String email) {
        if (email == null || email.trim().isEmpty()) return null;

        return donos.stream()
                .filter(dono -> email.equalsIgnoreCase(dono.getEmail()))
                .findFirst()
                .orElse(null);
    }

    public List<Dono> buscarPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()) return new ArrayList<>();

        return donos.stream()
                .filter(dono -> dono.getNome().toLowerCase().contains(nome.toLowerCase()))
                .collect(Collectors.toList());
    }

    public List<Dono> listarTodos() {
        return new ArrayList<>(donos);
    }

    public void atualizarDono(Dono donoAtualizado) throws PersistenciaException {
        for (int i = 0; i < donos.size(); i++) {
            if (donos.get(i).getId().equals(donoAtualizado.getId())) {
                donos.set(i, donoAtualizado);
                salvarDonos();
                return;
            }
        }
        throw new PersistenciaException("Dono não encontrado para atualização");
    }

    private void salvarDonos() throws PersistenciaException {
        try {
            JsonFileManager.salvar(FilePaths.DONOS_JSON, donos);
        } catch (PersistenciaException e) {
            System.err.println("Erro ao salvar donos: " + e.getMessage());
            throw e;
        }
    }
}