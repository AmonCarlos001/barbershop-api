CREATE TABLE agendamentos (
                              id BIGINT AUTO_INCREMENT PRIMARY KEY,
                              usuario_id BIGINT NOT NULL,
                              barbeiro_id BIGINT NOT NULL,
                              servico_id BIGINT NOT NULL,
                              data_hora DATETIME NOT NULL,
                              status VARCHAR(20) NOT NULL,
                              CONSTRAINT fk_agendamento_usuario FOREIGN KEY (usuario_id) REFERENCES usuarios(id),
                              CONSTRAINT fk_agendamento_barbeiro FOREIGN KEY (barbeiro_id) REFERENCES barbeiros(id),
                              CONSTRAINT fk_agendamento_servico FOREIGN KEY (servico_id) REFERENCES servicos(id)
);