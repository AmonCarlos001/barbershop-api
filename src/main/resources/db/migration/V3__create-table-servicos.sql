CREATE TABLE servicos (
                          id BIGINT AUTO_INCREMENT PRIMARY KEY,
                          nome VARCHAR(100) NOT NULL,
                          descricao VARCHAR(255),
                          duracao_minutos INT NOT NULL,
                          preco DECIMAL(10,2) NOT NULL,
                          ativo BOOLEAN NOT NULL DEFAULT TRUE
);