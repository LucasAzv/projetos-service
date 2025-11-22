package controller;


import dto.ProjetoDTO;
import dto.ProjetoResponseDTO;
import model.entity.StatusProjeto;
import model.entity.service.ProjetoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;


import java.util.List;

@RestController
@RequestMapping("/api/projetos")
public class ProjetoController {

    @Autowired
    private ProjetoService projetoService;


    @PreAuthorize("hasRole('PROFESSOR')")
    @PostMapping("/professor-solicitar")
    public ProjetoResponseDTO professorSolicitar(@RequestBody ProjetoDTO dto) {
        return projetoService.professorSolicitar(dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/iniciar-analise/{id}")
    public ProjetoResponseDTO adminIniciarAnalise(@PathVariable Long id) {
        return projetoService.adminIniciarAnalise(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/concluir-analise/{id}")
    public ProjetoResponseDTO adminConcluirAnalise(@PathVariable Long id, @RequestParam boolean aprovado, @RequestParam(required = false) Long grupoIdSeAprovado, @RequestParam(required = false) String justificativa) {
        return projetoService.adminConcluirAnalise(id, aprovado, grupoIdSeAprovado, justificativa);
    }


    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    @PutMapping("/atualizar-informacoes/{id}")
    public ProjetoResponseDTO atualizarInformacoes(@PathVariable Long id, @RequestBody ProjetoDTO dto) {
        return projetoService.atualizarInformacoes(id, dto);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PutMapping("/finalizar/{id}")
    public ProjetoResponseDTO adminFinalizar(@PathVariable Long id) {
        return projetoService.adminFinalizar(id);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    @GetMapping("/listar-um/{id}")
    public ProjetoResponseDTO findById(@PathVariable Long id) {
        return projetoService.findById(id);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar-status")
    public List<ProjetoResponseDTO> listarPorStatus(@RequestParam(required = false) StatusProjeto status) {
        return projetoService.listarPorStatus(status);
    }


    @PreAuthorize("hasRole('PROFESSOR')")
    @GetMapping("/listar-professor/{professorId}")
    public List<ProjetoResponseDTO> listarDoProfessor(@PathVariable Long professorId) {
        return projetoService.listarDoProfessor(professorId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/listar-grupo/{grupoId}")
    public List<ProjetoResponseDTO> listarDoGrupo(@PathVariable Long grupoId) {
        return projetoService.listarDoGrupo(grupoId);
    }

    @PreAuthorize("hasAnyRole('ADMIN', 'PROFESSOR')")
    @DeleteMapping("/excluir/{id}")
    public void deletar(@PathVariable Long id) {
        projetoService.deletar(id);
    }


}
