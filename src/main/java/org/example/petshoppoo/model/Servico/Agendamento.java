package org.example.petshoppoo.model.Servico;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.time.LocalDateTime;
import java.util.UUID;

public class Agendamento {
    private UUID id;
    private UUID idPet;
    private UUID idServico;
    private UUID idUsuario;
    private LocalDateTime dataHora;
    private StatusAgendamento status;
    private String observacoes;
    private double valorCobrado;
    private LocalDateTime dataCriacao;
    private LocalDateTime dataConclusao;
    private int duracaoMinutos;

    public enum StatusAgendamento {
        AGENDADO("Agendado", "⏰", "#FFE0B2"),
        CONFIRMADO("Confirmado", "✓", "#B3E5FC"),
        EM_ANDAMENTO("Em Andamento", "▶", "#FFF9C4"),
        CONCLUIDO("Concluído", "✔", "#C8E6C9"),
        CANCELADO("Cancelado", "✖", "#FFCDD2");

        private final String descricao;
        private final String icone;
        private final String cor;

        StatusAgendamento(String descricao, String icone, String cor) {
            this.descricao = descricao;
            this.icone = icone;
            this.cor = cor;
        }

        public String getDescricao() { return descricao; }
        public String getIcone() { return icone; }
        public String getCor() { return cor; }
    }

    // ========== CONSTRUTORES ==========

    /**
     * Construtor vazio (para JSON, novos agendamentos)
     */
    public Agendamento() {
        this.id = UUID.randomUUID();
        this.status = StatusAgendamento.AGENDADO;
        this.dataCriacao = LocalDateTime.now();
        this.valorCobrado = 0.0;
        this.duracaoMinutos = 0;
    }

    /**
     * Construtor completo para novo agendamento
     */
    public Agendamento(UUID idPet, UUID idServico, UUID idUsuario,
                       LocalDateTime dataHora, String observacoes, int duracaoMinutos) {
        this();
        this.idPet = idPet;
        this.idServico = idServico;
        this.idUsuario = idUsuario;
        this.dataHora = dataHora;
        this.observacoes = observacoes;
        this.duracaoMinutos = duracaoMinutos;
    }

    /**
     * Construtor para carregar do JSON (com todos os campos)
     */
    public Agendamento(UUID id, UUID idPet, UUID idServico, UUID idUsuario,
                       LocalDateTime dataHora, StatusAgendamento status,
                       String observacoes, double valorCobrado,
                       LocalDateTime dataCriacao, LocalDateTime dataConclusao,
                       int duracaoMinutos) {
        this.id = id != null ? id : UUID.randomUUID();
        this.idPet = idPet;
        this.idServico = idServico;
        this.idUsuario = idUsuario;
        this.dataHora = dataHora;
        this.status = status != null ? status : StatusAgendamento.AGENDADO;
        this.observacoes = observacoes;
        this.valorCobrado = valorCobrado;
        this.dataCriacao = dataCriacao != null ? dataCriacao : LocalDateTime.now();
        this.dataConclusao = dataConclusao;
        this.duracaoMinutos = duracaoMinutos;
    }

    // ========== GETTERS E SETTERS ==========

    public UUID getId() { return id; }

    public UUID getIdPet() { return idPet; }
    public void setIdPet(UUID idPet) { this.idPet = idPet; }

    public UUID getIdServico() { return idServico; }
    public void setIdServico(UUID idServico) { this.idServico = idServico; }

    public UUID getIdUsuario() { return idUsuario; }
    public void setIdUsuario(UUID idUsuario) { this.idUsuario = idUsuario; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public StatusAgendamento getStatus() { return status; }
    public void setStatus(StatusAgendamento status) { this.status = status; }

    public String getObservacoes() { return observacoes; }
    public void setObservacoes(String observacoes) { this.observacoes = observacoes; }

    public double getValorCobrado() { return valorCobrado; }
    public void setValorCobrado(double valorCobrado) { this.valorCobrado = valorCobrado; }

    public LocalDateTime getDataCriacao() { return dataCriacao; }

    public LocalDateTime getDataConclusao() { return dataConclusao; }
    public void setDataConclusao(LocalDateTime dataConclusao) { this.dataConclusao = dataConclusao; }

    public int getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(int duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    // ========== MÉTODOS ÚTEIS ==========
    @JsonIgnore
    public String getDescricaoStatus() {
        return status.getIcone() + " " + status.getDescricao();
    }
    @JsonIgnore
    public String getCorStatus() {
        return status.getCor();
    }
    @JsonIgnore
    public boolean isConcluido() {
        return status == StatusAgendamento.CONCLUIDO;
    }
    @JsonIgnore
    public boolean isCancelado() {
        return status == StatusAgendamento.CANCELADO;
    }
    @JsonIgnore
    public boolean isAtivo() {
        return status != StatusAgendamento.CANCELADO && status != StatusAgendamento.CONCLUIDO;
    }
    @JsonIgnore
    public LocalDateTime getDataHoraFim() {
        return dataHora.plusMinutes(duracaoMinutos);
    }
    @JsonIgnore
    public String getDataFormatada() {
        if (dataHora == null) return "";
        return dataHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy"));
    }
    @JsonIgnore
    public String getHoraFormatada() {
        if (dataHora == null) return "";
        return dataHora.format(java.time.format.DateTimeFormatter.ofPattern("HH:mm"));
    }
    @JsonIgnore
    public String getDataHoraFormatada() {
        if (dataHora == null) return "";
        return dataHora.format(java.time.format.DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"));
    }

    @Override
    public String toString() {
        return String.format("Agendamento [ID: %s, Pet: %s, Data: %s, Status: %s]",
                id.toString().substring(0, 8),
                idPet != null ? idPet.toString().substring(0, 8) : "null",
                getDataHoraFormatada(),
                status.getDescricao()
        );
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Agendamento that = (Agendamento) obj;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}