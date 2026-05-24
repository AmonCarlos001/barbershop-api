package com.ribeiro.barbershop.repository;

import com.ribeiro.barbershop.domain.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/** Interface de repositório JPA para gerenciamento e busca de usuários por credenciais de e-mail. */

public interface UsuarioRepository extends JpaRepository<Usuario, Long> {
    Optional<Usuario> findByEmail(String email);

}
