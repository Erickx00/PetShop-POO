package org.example.petshoppoo.repository.interfaces;

import org.example.petshoppoo.exceptions.PersistenciaException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IRepository<T> {
    List<T> listarTodos();
    Optional<T> buscarPorId(UUID id);
    void salvar(T entidade) throws PersistenciaException;
    void atualizar(T entidade) throws PersistenciaException;
    void deletar(UUID id) throws PersistenciaException;
}