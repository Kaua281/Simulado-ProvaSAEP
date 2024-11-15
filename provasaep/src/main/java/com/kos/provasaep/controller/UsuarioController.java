package com.kos.provasaep.controller;


import com.kos.provasaep.model.Usuario;
import com.kos.provasaep.repository.UsuarioRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/usuarios")
public class UsuarioController {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @PostMapping
    public ResponseEntity<Usuario> criarUsuario(@RequestBody Usuario usuario) {
        return ResponseEntity.ok(usuarioRepository.save(usuario)); // Cria o usuário
    }

    @GetMapping
    public List<Usuario> listarUsuarios() {
        return usuarioRepository.findAll(); // Retorna todos os usuários
    }
}

