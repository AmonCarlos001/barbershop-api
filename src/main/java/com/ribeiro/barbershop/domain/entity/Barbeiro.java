package com.ribeiro.barbershop.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Entidade JPA que mapeia a tabela de barbeiros e seu status de atividade. */

@Entity
@Table(name = "barbeiros")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Barbeiro {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "O nome do barbeiro é obrigatório.")
    private String nome;

    @Column(nullable = false)
    @NotBlank(message = "A especialidade do barbeiro deve ser informada.")
    private String especialidade;

    @Column(nullable = false)
    private Boolean ativo = true;
}
