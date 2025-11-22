package model.entity.service;

import dto.ProjetoDTO;
import dto.ProjetoResponseDTO;
import jakarta.persistence.EntityNotFoundException;
import model.entity.repository.ProjetoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.data.jpa.domain.AbstractPersistable_.id;


@Service
@Transactional
public class ProjetoService {

    @Autowired
    private ProjetoRepository projetoRepository;

    @Autowired
    private ProfessorRepository professorRepository;

    @Autowired
    private GrupoRepository grupoRepository;

    public ProjetoResponseDTO professorSolicitar(ProjetoDTO dto) {

        //Professor solicitante = professorRepository.findById(dto.solicitanteId())
              //  .orElseThrow(() -> new EntityNotFoundException("Professor solicitante com ID " + dto.solicitanteId() + " não encontrado."));


        if (projetoRepository.existsByTituloIgnoreCase(dto.titulo())) {
            throw new IllegalArgumentException("Já existe projeto com este título.");
        }

        boolean possuiPendente = projetoRepository.findBySolicitanteId(solicitante.getId())
                .stream()
                .anyMatch(p -> p.getStatus() == StatusProjeto.AGUARDANDO_ANALISE_PRELIMINAR
                        || p.getStatus() == StatusProjeto.EM_ANALISE);
        if (possuiPendente) {
            throw new IllegalStateException("Já existe solicitação em andamento para este professor.");
        }


        Projeto p = new Projeto(dto);
        p.setSolicitante(solicitante);
        p.setGrupo(null); // aqui coloquei pro professor não escolher grupo testar.

        Projeto projetoSalvo = projetoRepository.save(p);

        return ProjetoResponseDTO.fromEntity(projetoSalvo);
    }


    public ProjetoResponseDTO adminIniciarAnalise(Long projetoId) {
        Projeto p = projetoRepository.findById(projetoId).orElseThrow(() -> new EntityNotFoundException("Projeto com ID " + projetoId + " não encontrado."));
        if (p.getStatus() != StatusProjeto.AGUARDANDO_ANALISE_PRELIMINAR) {
            throw new IllegalStateException("Projeto não está 'aguardando análise preliminar'.");
        }
        p.setStatus(StatusProjeto.EM_ANALISE);
        Projeto projetoSalvo = projetoRepository.save(p);
        return ProjetoResponseDTO.fromEntity(projetoSalvo);
    }

    public ProjetoResponseDTO adminConcluirAnalise(Long projetoId, boolean aprovado, Long grupoIdSeAprovado, String justificativa) {
        Projeto p = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto com ID " + projetoId + " não encontrado."));


        if (p.getStatus() != StatusProjeto.EM_ANALISE) {
            throw new IllegalStateException("Projeto precisa estar 'EM_ANALISE' para ser concluído.");
        }

        if (aprovado) {
            if (grupoIdSeAprovado == null) {
                throw new IllegalArgumentException("Para aprovar, informe o grupo responsável.");
            }
            Grupo g = grupoRepository.findById(grupoIdSeAprovado)
                    .orElseThrow(() -> new EntityNotFoundException("Grupo com ID " + grupoIdSeAprovado + " não encontrado."));
            if (g.getDisponibilidade() == null || !g.getDisponibilidade()) {
                throw new IllegalStateException("Grupo selecionado está indisponível para novos projetos.");
            }
            p.setGrupo(g);
            p.setStatus(StatusProjeto.EM_ANDAMENTO);
            p.setJustificativa(null);
        } else {
            if (justificativa == null || justificativa.isBlank()) {
                throw new IllegalArgumentException("Justificativa é obrigatória para recusar o projeto.");
            }
            p.setStatus(StatusProjeto.PROJETO_RECUSADO);
            p.setJustificativa(justificativa);
            p.setGrupo(null);
        }
        Projeto projetoSalvo = projetoRepository.save(p);
        return ProjetoResponseDTO.fromEntity(projetoSalvo);
    }


    public ProjetoResponseDTO atualizarInformacoes(Long projetoId, ProjetoDTO dto) {
        Projeto p = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto com ID " + projetoId + " não encontrado."));
        if (p.getStatus() == StatusProjeto.PROJETO_RECUSADO || p.getStatus() == StatusProjeto.FINALIZADO) {
            throw new IllegalStateException("Projeto recusado ou finalizado não pode ser alterado.");
        }
        if (dto.objetivo() != null && !dto.objetivo().isBlank()) p.setObjetivo(dto.objetivo());
        if (dto.escopo() != null && !dto.escopo().isBlank()) p.setEscopo(dto.escopo());
        if (dto.publicoAlvo() != null && !dto.publicoAlvo().isBlank()) p.setPublicoAlvo(dto.publicoAlvo());
        if (dto.dataInicioPrevista() != null) p.setDataInicioPrevista(dto.dataInicioPrevista());


        if (dto.titulo() != null && !dto.titulo().isBlank()) {

            if (!p.getTitulo().equalsIgnoreCase(dto.titulo())) {


                if (projetoRepository.existsByTituloIgnoreCase(dto.titulo())) {
                    throw new IllegalArgumentException("O título '" + dto.titulo() + "' já está em uso por outro projeto.");
                }

                p.setTitulo(dto.titulo());
            }
        }

        Projeto projetoSalvo = projetoRepository.save(p);
        return ProjetoResponseDTO.fromEntity(projetoSalvo);
    }


    public ProjetoResponseDTO adminFinalizar(Long projetoId) {
        Projeto p = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto com ID " + id + " não encontrado."));

        if (p.getStatus() != StatusProjeto.EM_ANDAMENTO) {
            throw new IllegalStateException("Apenas projetos 'EM_ANDAMENTO' podem ser finalizados.");
        }
        p.setStatus(StatusProjeto.FINALIZADO);
        Projeto projetoSalvo = projetoRepository.save(p);
        return ProjetoResponseDTO.fromEntity(projetoSalvo);

    }



    public ProjetoResponseDTO findById(Long id) {
        Projeto projeto = projetoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Projeto com ID " + id + " não encontrado."));
        return ProjetoResponseDTO.fromEntity(projeto);
    }


    public List<ProjetoResponseDTO> listarPorStatus(StatusProjeto status) {
        List<Projeto> projetos = status == null ? projetoRepository.findAll() : projetoRepository.findByStatus(status);
        return projetos.stream().map(ProjetoResponseDTO::fromEntity).collect(Collectors.toList());
    }


    public List<ProjetoResponseDTO> listarDoProfessor(Long professorId) {
        return projetoRepository.findBySolicitanteId(professorId)
                .stream().map(ProjetoResponseDTO::fromEntity).collect(Collectors.toList());
    }


    public List<ProjetoResponseDTO> listarDoGrupo(Long grupoId) {
        return projetoRepository.findByGrupoId(grupoId)
                .stream().map(ProjetoResponseDTO::fromEntity).collect(Collectors.toList());
    }


    public void deletar(Long projetoId) {
        Projeto projetoParaDeletar = projetoRepository.findById(projetoId)
                .orElseThrow(() -> new EntityNotFoundException("Projeto com ID " + projetoId + " não encontrado."));
        projetoRepository.delete(projetoParaDeletar);
    }
}