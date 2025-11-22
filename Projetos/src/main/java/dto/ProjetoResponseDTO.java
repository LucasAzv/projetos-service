package dto;



import model.entity.Projeto;
import model.entity.StatusProjeto;

import java.time.LocalDate;
import java.time.LocalDateTime;

public record ProjetoResponseDTO(
        Long id,
        String titulo,
        String objetivo,
        StatusProjeto status,
        String justificativa, String escopo,
        String publicoAlvo,
        LocalDate dataInicioPrevista,
        LocalDateTime dataSolicitacao//,
        //ProfessorSimplificadoDTO solicitante, // <-- Usando o DTO simplificado
        //GrupoSimplificadoDTO grupo          // <-- Usando o DTO simplificado
) {
    public static ProjetoResponseDTO fromEntity(Projeto projeto) {
        return new ProjetoResponseDTO(
                projeto.getId(),
                projeto.getTitulo(),
                projeto.getObjetivo(),
                projeto.getStatus(),
                projeto.getJustificativa(),
                projeto.getEscopo(),
                projeto.getPublicoAlvo(),
                projeto.getDataInicioPrevista(),
                projeto.getDataSolicitacao()//,
               // ProfessorSimplificadoDTO.fromEntity(projeto.getSolicitante()),
              //  GrupoSimplificadoDTO.fromEntity(projeto.getGrupo())
        );
    }
}