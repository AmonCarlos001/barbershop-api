package com.ribeiro.barbershop.repository;

import com.ribeiro.barbershop.domain.entity.Barbeiro;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/** Interface de repositório JPA para gerenciamento e consultas filtradas dos barbeiros. */

public interface BarbeiroRepository extends JpaRepository <Barbeiro, Long> {
    List<Barbeiro> findByAtivoTrue();

}
