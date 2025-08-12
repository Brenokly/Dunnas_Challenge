INSERT INTO
  categorias (id, nome, ativo)
VALUES
  (
    gen_random_uuid (),
    'Beleza e Cuidados Pessoais',
    true
  ),
  (gen_random_uuid (), 'Eletrônicos e Gadgets', true),
  (
    gen_random_uuid (),
    'Ferramentas e Construção',
    true
  ),
  (gen_random_uuid (), 'Saúde e Bem-estar', true),
  (gen_random_uuid (), 'Brinquedos e Jogos', true),
  (gen_random_uuid (), 'Pet Shop', true),
  (gen_random_uuid (), 'Jardim e Lazer', true);