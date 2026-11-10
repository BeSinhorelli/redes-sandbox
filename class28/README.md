# Projeto Final (A3) — Projeto de Rede Estruturada para Pequena Empresa

Disciplina: Sistemas de Comunicação e Redes | Avaliação A3 (40 pontos)

## Contexto

Cada grupo (2 a 4 pessoas) projeta a rede de uma **pequena empresa
fictícia** com 2 a 3 setores/prédios. O grupo escolhe livremente: nome da
empresa, ramo de atuação, e a quantidade aproximada de funcionários/hosts
por setor.

## Requisitos Obrigatórios

1. **Topologia física e lógica** — diagramas claros mostrando dispositivos,
   cabos, e o fluxo lógico de dados/sub-redes.
2. **Endereçamento IPv4 com VLSM** (Aula 15) — plano de sub-redes a partir
   de um bloco base à escolha do grupo, dimensionado pela quantidade real
   de hosts de cada setor.
3. **Endereçamento IPv6** (Aula 16) — sub-redes IPv6 derivadas de um bloco
   escolhido pelo grupo.
4. **Cabeamento estruturado** (Aula 07) — os 6 subsistemas aplicados ao
   cenário, com categoria de cabo justificada por distância/velocidade.
5. **Segurança de rede** (Aula 22) — pelo menos 2 medidas aplicadas e
   justificadas para o cenário.
6. **Documentação técnica** — relatório completo com todos os itens acima.

## Requisitos Opcionais (bônus, até +3 pontos)

- VPN matriz-filial (Aula 21)
- Rede Wi-Fi segura (Aula 23)
- Componente em nuvem/contêiner (Aula 25)

## Formato de Entrega

- Relatório técnico em PDF ou Markdown
- Diagramas (Mermaid, draw.io, ou ferramenta de preferência)
- Recomendado (não obrigatório): topologia montada em Packet Tracer e/ou
  testada com VMs Ubuntu reais em VirtualBox

## Estrutura Sugerida do Relatório

1. Capa e identificação do grupo
2. Descrição do cenário
3. Topologia física e lógica
4. Endereçamento IPv4 (VLSM) e IPv6
5. Projeto de cabeamento estruturado
6. Medidas de segurança adotadas
7. (Opcional) VPN, Wi-Fi, nuvem
8. Conclusão

## Rubrica de Avaliação (40 pontos)

| Critério | Pontos |
|---|---|
| Topologia e endereçamento (IPv4 VLSM + IPv6) | 12 |
| Cabeamento estruturado | 6 |
| Segurança de rede | 8 |
| Documentação técnica | 8 |
| Apresentação oral | 6 |

Critérios detalhados de cada linha da rubrica estão em
`atividades/Aula_28_Orientacao_Projeto_Final_Atividades_Gabarito.md` na
raiz do curso.

## Cronograma

| Semana | Marco |
|---|---|
| Aula 28 | Formação de grupos, escolha do cenário |
| +1 semana | Topologia + endereçamento IPv4/IPv6 |
| +2 semanas | Cabeamento estruturado + segurança |
| +3 semanas | Relatório técnico consolidado |
| Aula 29 | Apresentação final |

## Ferramentas Úteis do Repositório

- `repository/class15/python/RC1501-CalculadoraVLSM.py` — conferir cálculo
  de sub-redes VLSM
- `repository/class14/python/RC1401-CalculadoraSubRedeSimples.py` —
  conferir sub-redes simples
- `repository/class22/python/RC2201-VerificadorSenhaForte.py` — avaliar
  força de senhas usadas no projeto
