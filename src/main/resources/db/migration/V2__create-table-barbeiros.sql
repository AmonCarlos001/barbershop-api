CREATE TABLE barbeiros (
                           id BIGINT AUTO_INCREMENT PRIMARY KEY,
                           nome VARCHAR(100) NOT NULL,
                           especialidade VARCHAR(100) NOT NULL,
                           ativo BOOLEAN NOT NULL DEFAULT TRUE
);