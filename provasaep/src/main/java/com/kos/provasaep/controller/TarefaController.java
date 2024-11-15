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
                    tarefa.setStatus(tarefaAtualizada.getStatus());
                    tarefa.setPrioridade(tarefaAtualizada.getPrioridade());
                    Tarefa tarefaSalva = tarefaRepository.save(tarefa);
                    return ResponseEntity.ok(tarefaSalva);
                }).orElseThrow(() -> new RuntimeException("Tarefa não encontrada"));
    }
}

