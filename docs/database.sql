DROP DATABASE IF EXISTS "java-web";
CREATE DATABASE "java-web";

CREATE TABLE IF NOT EXISTS costumers (
    cpf character varying (14) PRIMARY KEY,
    name character varying (255),
    phone character varying (16),
    email character varying (255),
    address character varying (255),
    number character varying (255),
    complement character varying (255),
    reference character varying (255),
    zip_code character varying (255),
    district character varying (255),
    city character varying (255),
    state character varying (255),
    country character varying (255)
);

CREATE TABLE IF NOT EXISTS services (
    id BIGSERIAL PRIMARY KEY,
    created_at timestamp DEFAULT current_timestamp,
    scheduling_date timestamp,
    cancelled boolean DEFAULT false,
    completed boolean DEFAULT false,
    installations integer DEFAULT 0,
    tube_length numeric(12, 5) DEFAULT 0.0,
    rappel_required boolean DEFAULT false,
    removal integer DEFAULT 0,
    maintenance integer DEFAULT 0,
    value numeric(12, 5) DEFAULT 0.0
);

INSERT INTO public.costumers (cpf, name, phone, email, address, number, complement, reference, zip_code, district,
                              city, state, country) VALUES
('000.000.000-01', 'costumer 1', '(48) 00000-0000', 'costumer_1@email.com', 'Rua A', '100', null, null, '00000-000', 'BAIRRO #1', 'CIDADE #1', 'ESTADO #1', 'BRASIL'),
('000.000.000-02', 'costumer 2', '(48) 00000-0000', 'costumer_2@email.com', 'Rua B', '200', null, null, '00000-000', 'BAIRRO #2', 'CIDADE #2', 'ESTADO #2', 'BRASIL'),
('000.000.000-03', 'costumer 3', '(48) 00000-0000', 'costumer_3@email.com', 'Rua C', '300', null, null, '00000-000', 'BAIRRO #3', 'CIDADE #3', 'ESTADO #3', 'BRASIL'),
('000.000.000-04', 'costumer 4', '(48) 00000-0000', 'costumer_4@email.com', 'Rua D', '400', null, null, '00000-000', 'BAIRRO #4', 'CIDADE #4', 'ESTADO #4', 'BRASIL');

INSERT INTO public.services (created_at, scheduling_date, cancelled, completed, installations, tube_length,
                             rappel_required, removal, maintenance, value) VALUES
(localtimestamp, '2021-11-30 13:00:00.000', false, false, 1, 3.50000, false, 0, 0, 320.0000);
