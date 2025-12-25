package org.example.petshoppoo.model.Servico;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.UUID;

public class Agendamento implements Serializable {
    private UUID id;
    private UUID idPet;
    private UUID idServico;
    private UUID idDono;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private String observacoes;
    private double valorCobrado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataAtualizacao;

    public enum StatusAgendamento {
        AGENDADO("Agendado", "⏰"),
        CONFIRMADO("Confirmado", "✓"),
        EM_ANDAMENTO("Em Andamento", "▶"),
        CONCLUIDO("Concluído", "✔"),
        CANCELADO("Cancelado", "✖"),
        NAO_COMPARECEU("Não Compareceu", "⚠");

        private final String descricao;
        private final String icone;

        StatusAgendamento(String descricao, String icone) {
            this.descricao = descricao;
            this.icone = icone;
        }

        public String getDescricao() { return descricao; }
        public String getIcone() { return icone; }

        public static StatusAgendamento fromDescricao(String descricao) {
            for (StatusAgendamento status : values()) {
                if (status.getDescricao().equals(descricao)) {
                    return status;
                }
            }
            return AGENDADO;
        }
    }

    public Agendamento() {
        this.id = UUID.randomUUID();
        this.status = StatusAgendamento.AGENDADO;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    public Agendamento(UUID id, UUID idPet, UUID idServico, UUID idDono,
                       LocalDateTime dataHora, String observacoes) {
        this.id = id == null ? UUID.randomUUID() : id;
        this.idPet = idPet;
        this.idServico = idServico;
        this.idDono = idDono;
        this.dataHora = dataHora;
        this.status = StatusAgendamento.AGENDADO;
        this.observacoes = observacoes;
        this.dataCriacao = LocalDateTime.now();
        this.dataAtualizacao = LocalDateTime.now();
    }

    // Getters e Setters
    public UUID getId() { return id; }
    public void setId(UUID id) { this.id = id; }

    public UUID getIdPet() { return idPet; }
    public void setIdPet(UUID idPet) { this.idPet = idPet; }

    public UUID getIdServico() { return idServico; }
    public void setIdServico(UUID idServico) { this.idServico = idServico; }

    public UUID getIdDono() { return idDono; }
    public void setIdDono(UUID idDono) { this.idDono = idDono; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) {
        this.status = status;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) {
        this.observacoes = observacoes;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public double getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(double valorCobrado) {
        this.valorCobrado = valorCobrado;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public LocalDateTime getDataCriacao() { return dataCriacao; }
    public LocalDateTime getDataAtualizacao() { return dataAtualizacao; }

    // Métodos auxiliares
    public boolean podeCancelar() {
        LocalDateTime agora = LocalDateTime.now();
        long horasParaAgendamento = java.time.Duration.between(agora, dataHora).toHours();
        return status == StatusAgendamento.AGENDADO && horasParaAgendamento > 2;
    }

    public void confirmar() {
        this.status = StatusAgendamento.CONFIRMADO;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void cancelar() {
        this.status = StatusAgendamento.CANCELADO;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public void concluir() {
        this.status = StatusAgendamento.CONCLUIDO;
        this.dataAtualizacao = LocalDateTime.now();
    }

    public String getDescricaoStatus() {
        return status.getIcone() + " " + status.getDescricao();
    }

    @Override
    public String toString() {
        return "Agendamento{" +
                "id=" + id +
                ", dataHora=" + dataHora +
                ", status=" + status.getDescricao() +
                ", observacoes='" + observacoes + '\'' +
                ", valor=R$" + String.format("%.2f", valorCobrado) +
                '}';
    }
}