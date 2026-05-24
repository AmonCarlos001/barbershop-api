package com.ribeiro.barbershop.config;

import com.ribeiro.barbershop.repository.UsuarioRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/** Configuração global da aplicação para gerenciamento de autenticação, usuários e criptografia. */

@Configuration
public class ApplicationConfiguration {

    private final UsuarioRepository usuarioRepository;

    public ApplicationConfiguration(UsuarioRepository usuarioRepository) {
        this.usuarioRepository = usuarioRepository;
    }

    // Define a estratégia de busca de usuários no banco utilizando o e-mail
    @Bean
    public UserDetailsService userDetailsService(){
        return username -> usuarioRepository.findByEmail(username)
                .orElseThrow(()-> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }

    // Provedor de autenticação padrão que conecta a busca de usuários à criptografia
    @Bean
    public AuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService());
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    //Define o BCrypt como o algoritmo de hash para criptografia de senhas
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }


}
