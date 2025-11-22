package dto;

import model.entity.StatusProjeto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProjetoDTO(Long id, String titulo, String objetivo, String escopo, String publicoAlvo, LocalDateTime dataSolicitacao, LocalDate dataInicioPrevista, StatusProjeto status, String justificativa, Long solicitanteId, Long grupoId) {
}