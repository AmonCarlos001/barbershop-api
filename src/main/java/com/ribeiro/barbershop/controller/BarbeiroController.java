package com.ribeiro.barbershop.controller;

import com.ribeiro.barbershop.domain.entity.Barbeiro;
import com.ribeiro.barbershop.repository.BarbeiroRepository;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Controller para gestão dos profissionais e controle de exclusão lógica (Soft Delete).
 */

@RestController
@RequestMapping("/api/v1/barbeiros")
public class BarbeiroController {

    private final BarbeiroRepository barbeiroRepository;

    public BarbeiroController(BarbeiroRepository barbeiroRepository) {
        this.barbeiroRepository = barbeiroRepository;
    }

    @PostMapping
    public ResponseEntity<Barbeiro> cadastrar(@Valid @RequestBody Barbeiro barbeiro){
        barbeiro.setAtivo(true);    //Todo barbeiro recém-cadastrado inicia com o perfil ativo no sistema
        return ResponseEntity.ok(barbeiroRepository.save(barbeiro));
    }

    @GetMapping
    public ResponseEntity<List<Barbeiro>> listarTodos(){
        return ResponseEntity.ok(barbeiroRepository.findAll());
    }

    @GetMapping("/ativos")
    public ResponseEntity<List<Barbeiro>> listarAtivos(){
        return ResponseEntity.ok(barbeiroRepository.findByAtivoTrue());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Barbeiro> atualizar(@PathVariable Long id, @RequestBody Barbeiro dadosAtualizados){
        return barbeiroRepository.findById(id)
                .map(barbeiro -> {
                    barbeiro.setNome(dadosAtualizados.getNome());
                    barbeiro.setEspecialidade(dadosAtualizados.getEspecialidade());
                    if (dadosAtualizados.getAtivo() != null){
                        barbeiro.setAtivo(dadosAtualizados.getAtivo());
                    }
                    return ResponseEntity.ok(barbeiroRepository.save(barbeiro));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id){
        // Estratégia de Soft Delete: Desativa o profissional no sistema em vez de realizar a exclusão física,
        barbeiroRepository.findById(id).ifPresent(barbeiro -> {
            barbeiro.setAtivo(false);
            barbeiroRepository.save(barbeiro);
                });
        return ResponseEntity.noContent().build();
    }
}
