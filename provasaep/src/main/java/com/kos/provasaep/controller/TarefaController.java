package com.kos.provasaep.controller;

import com.kos.provasaep.model.Tarefa;
import com.kos.provasaep.model.Usuario;
import com.kos.provasaep.repository.TarefaRepository;
import com.kos.provasaep.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/tarefas")
public class TarefaController {

    @Autowired
    private TarefaRepository tarefaRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<Tarefa> criarTarefa(@RequestBody Tarefa tarefa) {
        // Busca o usuário pelo ID
        Usuario usuario = usuarioRepository.findById(tarefa.getUsuario().getId())
                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
        
        tarefa.setUsuario(usuario); // Associa o usuário à tarefa
        Tarefa tarefaCriada = tarefaRepository.save(tarefa); // Salva a tarefa no banco
        return ResponseEntity.ok(tarefaCriada); // Retorna a tarefa criada
    }

    @GetMapping
    public List<Tarefa> listarTarefas() {
        return tarefaRepository.findAll(); // Retorna todas as tarefas
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tarefa> atualizarTarefa(@PathVariable Long id, @RequestBody Tarefa tarefaAtualizada) {
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    // Atualiza os campos da tarefa
                    if (tarefaAtualizada.getDescricao() != null) {
                        tarefa.setDescricao(tarefaAtualizada.getDescricao());
                    }
                    if (tarefaAtualizada.getNomeSetor() != null) {
                        tarefa.setNomeSetor(tarefaAtualizada.getNomeSetor());
                    }
                    if (tarefaAtualizada.getStatus() != null) {
                        tarefa.setStatus(tarefaAtualizada.getStatus());
                    }
                    if (tarefaAtualizada.getPrioridade() != null) {
                        tarefa.setPrioridade(tarefaAtualizada.getPrioridade());
                    }

                    // Verifica se o usuário foi alterado
                    if (tarefaAtualizada.getUsuario() != null && tarefaAtualizada.getUsuario().getId() != null) {
                        Usuario usuario = usuarioRepository.findById(tarefaAtualizada.getUsuario().getId())
                                .orElseThrow(() -> new RuntimeException("Usuário não encontrado"));
                        tarefa.setUsuario(usuario); // Associa o usuário à tarefa
                    }

                    // Salva as alterações da tarefa
                    Tarefa tarefaSalva = tarefaRepository.save(tarefa);
                    return ResponseEntity.ok(tarefaSalva); // Retorna a tarefa atualizada
                })
                .orElseGet(() -> ResponseEntity.status(404).body(null)); // Retorna erro 404 se não encontrar a tarefa
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tarefa> obterTarefa(@PathVariable Long id) {
        Tarefa tarefa = tarefaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
        return ResponseEntity.ok(tarefa); // Retorna a tarefa encontrada
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> excluirTarefa(@PathVariable Long id) {
        return tarefaRepository.findById(id)
                .map(tarefa -> {
                    tarefaRepository.delete(tarefa); // Deleta a tarefa do banco de dados
                    return ResponseEntity.noContent().build(); // Retorna status 204 (sem conteúdo)
                })
                .orElseGet(() -> ResponseEntity.notFound().build()); // Retorna status 404 se não encontrar a tarefa
}

}
