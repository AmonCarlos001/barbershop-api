package com.ribeiro.barbershop.controller;

import com.ribeiro.barbershop.domain.entity.Usuario;
import com.ribeiro.barbershop.dto.request.AuthenticationRequest;
import com.ribeiro.barbershop.dto.request.RegisterRequest;
import com.ribeiro.barbershop.dto.response.AuthenticationResponse;
import com.ribeiro.barbershop.repository.UsuarioRepository;
import com.ribeiro.barbershop.service.AuthenticationService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller para endpoints públicos de autenticação, cadastro e emissão de tokens JWT. */

@RestController
@RequestMapping("/api/v1/auth")
public class AuthenticationController {

    private final AuthenticationService authenticationService;
    private final UsuarioRepository usuarioRepository;

    public AuthenticationController(AuthenticationService authenticationService, UsuarioRepository usuarioRepository) {
        this.authenticationService = authenticationService;
        this.usuarioRepository = usuarioRepository;
    }

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authenticationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> authenticate(@RequestBody AuthenticationRequest request) {
        return ResponseEntity.ok(authenticationService.authenticate(request));
    }

    @GetMapping("/listar")
    public ResponseEntity<List<Usuario>> listarTodos() {
        // Mapeia a lista para retornar apenas os nomes/emails, blindando o hash das senhas
        List<Usuario> usuarios = usuarioRepository.findAll();
        return ResponseEntity.ok(usuarios);
    }
}
