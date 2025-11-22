package model.entity.repository;

import model.entity.Projeto;
import model.entity.StatusProjeto;
import org.springframework.data.jpa.repository.JpaRepository;


import java.util.List;
import java.util.Optional;

public interface ProjetoRepository extends JpaRepository<Projeto,Long> {

    Optional<Projeto> findByTituloIgnoreCase(String titulo);
    boolean existsByTituloIgnoreCase(String titulo);
    List<Projeto> findByStatus(StatusProjeto status);
    List<Projeto> findBySolicitanteId(Long professorId);
    List<Projeto> findByGrupoId(Long grupoId);
    boolean existsBySolicitanteId(Long solicitanteId);
    boolean existsBySolicitanteIdAndStatusIn(Long solicitanteId, List<StatusProjeto> status);
    boolean existsByGrupoIdAndStatus(Long grupoId, StatusProjeto status);
    boolean existsByGrupoIdAndStatusIn(Long grupoId, List<StatusProjeto> status);


}
