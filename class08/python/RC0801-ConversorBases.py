#!/usr/bin/env python3
"""
RC0801 - Conversor de Bases (Binario / Decimal / Hexadecimal)
Aula 08: Sistemas de Numeracao

Converte um numero decimal (0-255) para binario de 8 bits e hexadecimal
de 2 digitos, e vice-versa, usando o metodo da subtracao (potencias de 2)
ensinado na aula - o mesmo processo manual, so que automatizado.
"""

POTENCIAS_DE_2 = [128, 64, 32, 16, 8, 4, 2, 1]


def validar_decimal(n):
    if not (0 <= n <= 255):
        raise ValueError(f"Decimal fora do intervalo de um octeto (0-255): {n}")


def validar_binario(bits):
    if len(bits) != 8 or any(c not in "01" for c in bits):
        raise ValueError(f"Binario invalido (precisa ter 8 digitos '0'/'1'): {bits}")


def validar_hexadecimal(hexa):
    if len(hexa) != 2 or any(c.upper() not in "0123456789ABCDEF" for c in hexa):
        raise ValueError(f"Hexadecimal invalido (precisa ter 2 digitos 0-9/A-F): {hexa}")


def decimal_para_binario(n):
    """Metodo da subtracao: percorre as potencias de 2 da maior para a menor."""
    validar_decimal(n)
    bits = ""
    resto = n
    for valor in POTENCIAS_DE_2:
        if resto >= valor:
            bits += "1"
            resto -= valor
        else:
            bits += "0"
    return bits


def binario_para_decimal(bits):
    """Soma as posicoes onde o bit e 1."""
    validar_binario(bits)
    return sum(v for v, b in zip(POTENCIAS_DE_2, bits) if b == "1")


def decimal_para_hexadecimal(n):
    """Passa pelo binario: converte para 8 bits, depois cada nibble para hex."""
    bits = decimal_para_binario(n)
    nibble_alto, nibble_baixo = bits[:4], bits[4:]
    return f"{int(nibble_alto, 2):X}{int(nibble_baixo, 2):X}"


def hexadecimal_para_decimal(hexa):
    """Passa pelo binario: cada digito hex vira um nibble de 4 bits."""
    validar_hexadecimal(hexa)
    bits = "".join(f"{int(c, 16):04b}" for c in hexa)
    return binario_para_decimal(bits)


def formatar_linha(decimal, binario, hexadecimal):
    return f"  Decimal: {decimal:>3}   |   Binario: {binario}   |   Hexadecimal: {hexadecimal}"


def demonstracao():
    print("=" * 60)
    print("CONVERSOR DE BASES - Decimal / Binario / Hexadecimal")
    print("=" * 60)
    exemplos = [200, 45, 127, 255, 168, 210, 0, 16]
    for n in exemplos:
        bits = decimal_para_binario(n)
        hexa = decimal_para_hexadecimal(n)
        print(formatar_linha(n, bits, hexa))

    print("\nConferindo o caminho inverso (binario/hex -> decimal):")
    for bits in ["11001000", "01111111", "10101000"]:
        n = binario_para_decimal(bits)
        print(f"  Binario {bits} -> Decimal {n}")
    for hexa in ["A8", "D2", "FF"]:
        n = hexadecimal_para_decimal(hexa)
        print(f"  Hexadecimal {hexa} -> Decimal {n}")


if __name__ == "__main__":
    demonstracao()
