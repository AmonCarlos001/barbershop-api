package com.ribeiro.barbershop.controller;

import com.ribeiro.barbershop.domain.entity.Servico;
import com.ribeiro.barbershop.repository.ServicoRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/** Controller para gestão e listagem geral de usuários cadastrados no sistema. */

@RestController
@RequestMapping("/api/v1/servicos")
public class ServicoController {

    private final ServicoRepository servicoRepository;

    public ServicoController(ServicoRepository servicoRepository) {
        this.servicoRepository = servicoRepository;
    }

    @PostMapping
    public ResponseEntity<Servico> cadastrar(@RequestBody Servico servico){
        servico.setAtivo(true);
        return ResponseEntity.ok(servicoRepository.save(servico));
    }

    @GetMapping
    public ResponseEntity<List<Servico>> listarTodos(){
        return ResponseEntity.ok(servicoRepository.findAll());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Servico> atualizar(@PathVariable Long id, @RequestBody Servico dadosAtualizados){
        return servicoRepository.findById(id)
                .map(servico -> {
                    servico.setNome(dadosAtualizados.getNome());
                    servico.setDescricao(dadosAtualizados.getDescricao());
                    servico.setDuracaoMinutos(dadosAtualizados.getDuracaoMinutos());
                    servico.setPreco(dadosAtualizados.getPreco());
                    if (dadosAtualizados.getAtivo() != null){
                        servico.setAtivo(dadosAtualizados.getAtivo());
                    }
                    return ResponseEntity.ok(servicoRepository.save(servico));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> remover(@PathVariable Long id){
        servicoRepository.findById(id).ifPresent(servico -> {
            servico.setAtivo(false);
            servicoRepository.save(servico);
        });
        return ResponseEntity.noContent().build();
    }
}
