package com.ribeiro.barbershop.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/** Entidade JPA que representa o catálogo de serviços, preços e durações da barbearia. */

@Entity
@Table(name = "servicos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Servico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @NotBlank(message = "O nome do serviço é obrigatório.")
    private String nome;

    @NotBlank(message = "A descrição do serviço é obrigatória.")
    private String descricao;

    @Column(nullable = false)
    @NotNull(message = "A duração do serviço deve ser informada.")
    private Integer duracaoMinutos;

    @Column(nullable = false)
    @NotNull(message = "O preço do serviço deve ser informado.")
    @Positive(message = "O preço deve ser um valor positivo.")
    private Double preco;

    @Column(nullable = false)
    private Boolean ativo = true;
}
