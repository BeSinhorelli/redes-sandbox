#!/usr/bin/env python3
"""
RC0201 - Gerador de Formas de Onda NRZ e Manchester
Aula 02: Sinais Analogicos e Digitais, Modulacao e Codificacao

Recebe uma sequencia de bits (string com '0'/'1') e imprime, em arte ASCII,
as formas de onda NRZ e Manchester correspondentes.

Convencao Manchester adotada (G.E. Thomas):
  bit 0 -> transicao baixo->alto no meio do intervalo do bit
  bit 1 -> transicao alto->baixo no meio do intervalo do bit
"""

LARGURA_BIT = 8  # colunas por bit (deve ser par para a Manchester)
ALTO = "▔"
BAIXO = "▁"
VAZIO = " "


def validar_bits(bits):
    if not bits or any(c not in "01" for c in bits):
        raise ValueError("A sequencia deve conter apenas os caracteres '0' e '1'.")


def gerar_nrz(bits):
    """Retorna (linha_alta, linha_baixa) para a codificacao NRZ."""
    topo = []
    base = []
    for bit in bits:
        if bit == "1":
            topo.append(ALTO * LARGURA_BIT)
            base.append(VAZIO * LARGURA_BIT)
        else:
            topo.append(VAZIO * LARGURA_BIT)
            base.append(BAIXO * LARGURA_BIT)
    return "".join(topo), "".join(base)


def gerar_manchester(bits):
    """Retorna (linha_alta, linha_baixa) para a codificacao Manchester (G.E. Thomas)."""
    metade = LARGURA_BIT // 2
    topo = []
    base = []
    for bit in bits:
        if bit == "0":
            primeira, segunda = BAIXO, ALTO
        else:
            primeira, segunda = ALTO, BAIXO

        for simbolo, tamanho in ((primeira, metade), (segunda, metade)):
            if simbolo == ALTO:
                topo.append(ALTO * tamanho)
                base.append(VAZIO * tamanho)
            else:
                topo.append(VAZIO * tamanho)
                base.append(BAIXO * tamanho)
    return "".join(topo), "".join(base)


def linha_de_rotulos(bits):
    """Rotulo de cada bit centralizado sob seu bloco de colunas."""
    rotulo = []
    for bit in bits:
        centro = LARGURA_BIT // 2
        rotulo.append(bit.center(LARGURA_BIT) if LARGURA_BIT > 1 else bit)
    return "".join(rotulo)


def linha_de_fronteiras(bits):
    total = len(bits) * LARGURA_BIT
    return "".join("+" if col % LARGURA_BIT == 0 else "-" for col in range(total))


def imprimir_forma_de_onda(titulo, bits):
    topo, base = (gerar_nrz(bits) if titulo == "NRZ" else gerar_manchester(bits))
    print(f"\n{titulo}:")
    print("  " + linha_de_rotulos(bits))
    print("  " + linha_de_fronteiras(bits))
    print("  " + topo)
    print("  " + base)


def main():
    exemplos = ["10110010", "00000000"]
    for bits in exemplos:
        validar_bits(bits)
        print("=" * 60)
        print(f"Sequencia de bits: {bits}")
        imprimir_forma_de_onda("NRZ", bits)
        imprimir_forma_de_onda("Manchester", bits)
    print("=" * 60)


if __name__ == "__main__":
    main()
