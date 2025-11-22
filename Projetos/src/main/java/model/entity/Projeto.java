package model.entity;

import dto.ProjetoDTO;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name="projeto")
public class Projeto {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Long id;

    @Column
    private String titulo;

    @Column
    private String objetivo;

    @Column
    private String escopo;

    @Column
    private String publicoAlvo;

    @Column
    private LocalDateTime dataSolicitacao;

    @Column
    private LocalDate dataInicioPrevista;

    @Enumerated(EnumType.STRING)
    @Column
    private StatusProjeto status;

    @Column
    private String justificativa;


    //@ManyToOne(fetch = FetchType.LAZY)
   // @JoinColumn(name="solicitante_id")
    //private Professor solicitante;

   // @ManyToOne (fetch = FetchType.LAZY)
   // @JoinColumn (name="grupo_id")
  //  private Grupo grupo;


    public Projeto(ProjetoDTO dto) {
        this.titulo = dto.titulo();
        this.objetivo = dto.objetivo();
        this.escopo = dto.escopo();
        this.publicoAlvo = dto.publicoAlvo();
        this.dataSolicitacao = LocalDateTime.now();
        this.dataInicioPrevista = dto.dataInicioPrevista();
        this.status = StatusProjeto.AGUARDANDO_ANALISE_PRELIMINAR;
        this.justificativa = dto.justificativa();
       // this.solicitante = Professor.fromId(dto.solicitanteId());
       // this.grupo = null;
    }
}
