package com.ribeiro.barbershop.repository;

import com.ribeiro.barbershop.domain.entity.Agendamento;
import com.ribeiro.barbershop.domain.entity.Usuario;
import com.ribeiro.barbershop.domain.enums.StatusAgendamentoEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

/** Interface de repositório JPA para operações de banco de dados e consultas de agendamentos. */

public interface AgendamentoRepository extends JpaRepository<Agendamento, Long> {
    List<Agendamento> findByUsuario(Usuario usuario);
    boolean existsByBarbeiroIdAndDataHoraAndStatusNot(
            Long barbeiroId,
            LocalDateTime dataHora,
            StatusAgendamentoEnum status);

    List<Agendamento> findAllByOrderByDataHoraAsc();

    List<Agendamento> findByBarbeiroIdOrderByDataHoraAsc(Long barbeiroId);
}
