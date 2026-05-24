package com.ribeiro.barbershop.controller;

import com.ribeiro.barbershop.domain.entity.Agendamento;
import com.ribeiro.barbershop.domain.enums.StatusAgendamentoEnum;
import com.ribeiro.barbershop.dto.request.AgendamentoRequest;
import com.ribeiro.barbershop.dto.response.AgendamentoResponse;
import com.ribeiro.barbershop.exception.BusinessException;
import com.ribeiro.barbershop.repository.AgendamentoRepository;
import com.ribeiro.barbershop.repository.BarbeiroRepository;
import com.ribeiro.barbershop.repository.ServicoRepository;
import com.ribeiro.barbershop.repository.UsuarioRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/** Controller para gestão de agendamentos, validações de horários e listagem paginada. */

@RestController
@RequestMapping("/api/v1/agendamentos")
public class AgendamentoController {

    private final AgendamentoRepository agendamentoRepository;
    private final UsuarioRepository usuarioRepository;
    private final BarbeiroRepository barbeiroRepository;
    private final ServicoRepository servicoRepository;

    public AgendamentoController(AgendamentoRepository agendamentoRepository, UsuarioRepository usuarioRepository, BarbeiroRepository barbeiroRepository, ServicoRepository servicoRepository) {
        this.agendamentoRepository = agendamentoRepository;
        this.usuarioRepository = usuarioRepository;
        this.barbeiroRepository = barbeiroRepository;
        this.servicoRepository = servicoRepository;
    }

    @PostMapping
    public ResponseEntity<?> criarAgendamento(@RequestBody AgendamentoRequest request){
        var usuario = usuarioRepository.findById(request.usuarioId())
                .orElseThrow(()-> new IllegalArgumentException("Usuário não encontrado"));
        var barbeiro = barbeiroRepository.findById(request.barbeiroId())
                .orElseThrow(()-> new IllegalArgumentException("Barbeiro não encontrado"));
        var servico = servicoRepository.findById(request.servicoId())
                .orElseThrow(()-> new IllegalArgumentException("Serviço não encontrado"));

        // Garante que o barbeiro não receba dois agendamentos confirmados no mesmo instante
        boolean horarioOcupado = agendamentoRepository.existsByBarbeiroIdAndDataHoraAndStatusNot(
                request.barbeiroId(),
                request.dataHora(),
                StatusAgendamentoEnum.CANCELADO
        );

        if (horarioOcupado) {
            throw new BusinessException("Este barbeiro já possui um agendamento confirmado para este dia e horário.");
        }

        // Validação da política de funcionamento: Terça a Domingo, das 10:00 às 21:00
        var dataHoraAgendamento = request.dataHora();
        if (dataHoraAgendamento.getDayOfWeek() == DayOfWeek.MONDAY) {
            throw new BusinessException("A barbearia não funciona às segundas-feiras.");
        }
        int hora = dataHoraAgendamento.getHour();
        if (hora < 10 || hora >= 21) {
            throw new BusinessException("Horário inválido. A barbearia funciona apenas das 10:00 às 21:00.");
        }

        Agendamento agendamento = Agendamento.builder()
                .usuario(usuario)
                .barbeiro(barbeiro)
                .servico(servico)
                .dataHora(request.dataHora())
                .status(StatusAgendamentoEnum.PENDENTE)
                .build();

        return ResponseEntity.ok(agendamentoRepository.save(agendamento));
    }

    @GetMapping
    public ResponseEntity<Page<AgendamentoResponse>> listarTodos(@PageableDefault(page = 0, size = 10, sort = "dataHora", direction =Sort.Direction.ASC) Pageable pageable){
        Page<Agendamento> agendamentosPage = agendamentoRepository.findAll(pageable);
        Page<AgendamentoResponse> dtoPage = agendamentosPage.map(a -> new AgendamentoResponse(
                a.getId(),
                a.getUsuario().getNome(),
                a.getBarbeiro().getNome(),
                a.getServico().getNome(),
                a.getServico().getPreco(),
                a.getDataHora(),
                a.getStatus().name()
        ));

        return ResponseEntity.ok(dtoPage);
    }

    @GetMapping("/barbeiro/{barbeiroId}")
    public ResponseEntity<List<AgendamentoResponse>> listarPorBarbeiro(@PathVariable Long barbeiroId){
        if (!barbeiroRepository.existsById(barbeiroId)) {
            throw new IllegalArgumentException("Barbeiro não encontrado");
        }

        List<Agendamento> agendamentos = agendamentoRepository.findByBarbeiroIdOrderByDataHoraAsc(barbeiroId);

        List<AgendamentoResponse> dtos = agendamentos.stream()
                .map(a -> new AgendamentoResponse(
                        a.getId(),
                        a.getUsuario().getNome(),     // Pega só o nome do cliente (Não expoõe a senha)
                        a.getBarbeiro().getNome(),
                        a.getServico().getNome(),
                        a.getServico().getPreco(),
                        a.getDataHora(),
                        a.getStatus().name()
                ))
                .toList();

        return ResponseEntity.ok(dtos);
    }

    @PutMapping("/{id}/remarcar")
    public ResponseEntity<?> remarcarAgendamento(@PathVariable Long id, @RequestBody Map<String, String> body) {
        if (!body.containsKey("novaDataHora")){
            return ResponseEntity.badRequest().body("O campo 'novaDataHora' é obrigatório");
        }

        LocalDateTime novaDataHora = LocalDateTime.parse(body.get("novaDataHora"));

        return agendamentoRepository.findById(id)
                .map(agendamento -> {
                    if (agendamento.getStatus() == StatusAgendamentoEnum.CANCELADO){
                        return ResponseEntity.badRequest().body("Não é possível um agendamento cancelado.");
                    }

                    //Altera o status para pendente para nova análise e aceite do Barbeiro
                    agendamento.setDataHora(novaDataHora);
                    agendamento.setStatus(StatusAgendamentoEnum.PENDENTE);

                    return ResponseEntity.ok(agendamentoRepository.save(agendamento));
                })
                .orElse(ResponseEntity.notFound().build());

    }

    @PutMapping("/{id}/cancelar")
    public ResponseEntity<?> cancelarAgendamento(@PathVariable Long id){
        return agendamentoRepository.findById(id)
                .map(agendamento -> {
                    agendamento.setStatus(StatusAgendamentoEnum.CANCELADO);
                    agendamentoRepository.save(agendamento);

                    return ResponseEntity.ok().build();
                }).orElse(ResponseEntity.notFound().build());
    }


}





























