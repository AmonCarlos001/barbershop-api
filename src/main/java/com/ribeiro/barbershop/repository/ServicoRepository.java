package com.ribeiro.barbershop.repository;
import com.ribeiro.barbershop.domain.entity.Servico;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

/** Interface de repositório JPA para gerenciamento e consultas filtradas do catálogo de serviços. */

public interface ServicoRepository extends JpaRepository<Servico, Long> {
    List<Servico> findByAtivoTrue();

}
