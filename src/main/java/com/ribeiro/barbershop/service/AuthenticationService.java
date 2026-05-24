package com.ribeiro.barbershop.service;

import com.ribeiro.barbershop.domain.entity.Usuario;
import com.ribeiro.barbershop.domain.enums.RoleEnum;
import com.ribeiro.barbershop.dto.request.AuthenticationRequest;
import com.ribeiro.barbershop.dto.request.RegisterRequest;
import com.ribeiro.barbershop.dto.response.AuthenticationResponse;
import com.ribeiro.barbershop.repository.UsuarioRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/** Service responsável pelas regras de negócio de registro de novos usuários e autenticação via JWT. */

@Service
public class AuthenticationService {

    private final UsuarioRepository usuarioRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UsuarioRepository usuarioRepository, PasswordEncoder passwordEncoder, JwtService jwtService, AuthenticationManager authenticationManager) {
        this.usuarioRepository = usuarioRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }
    public AuthenticationResponse register(RegisterRequest request) {
        Usuario usuario = new Usuario();
        usuario.setNome(request.nome());
        usuario.setEmail(request.email());
        usuario.setSenha(passwordEncoder.encode(request.senha()));
        usuario.setRole(RoleEnum.ROLE_CLIENTE);

        usuarioRepository.save(usuario);

        var jwtToken = jwtService.generateToken(usuario);
        return new AuthenticationResponse(jwtToken);
    }

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.senha()
                )
        );
        var usuario = usuarioRepository.findByEmail(request.email())
                .orElseThrow(() -> new IllegalArgumentException("Usuário ou senha inválidos."));

        var jwtToken = jwtService.generateToken(usuario);
        return new AuthenticationResponse(jwtToken);
    }

}
