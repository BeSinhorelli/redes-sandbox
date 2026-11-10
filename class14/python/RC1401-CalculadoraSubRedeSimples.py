#!/usr/bin/env python3
"""
RC1401 - Calculadora de Sub-rede Simples
Aula 14: Enderecamento IPv4 e Sub-redes

Recebe um IPv4 no formato "A.B.C.D/CIDR" (ex: 192.168.1.50/24) e calcula:
endereco de rede, endereco de broadcast, primeiro/ultimo host valido e a
quantidade de hosts utilizaveis.

Uso:
    python3 RC1401-CalculadoraSubRedeSimples.py 192.168.1.50/24
    python3 RC1401-CalculadoraSubRedeSimples.py   (roda exemplos padrao)
"""
import sys


def validar_octeto(octeto):
    if not octeto.isdigit():
        raise ValueError(f"Octeto invalido: '{octeto}' (nao numerico)")
    valor = int(octeto)
    if valor < 0 or valor > 255:
        raise ValueError(f"Octeto invalido: {valor} (fora de 0-255)")
    return valor


def ip_para_inteiro(ip):
    partes = ip.split(".")
    if len(partes) != 4:
        raise ValueError(f"Endereco IPv4 invalido: '{ip}' (deve ter 4 octetos)")
    octetos = [validar_octeto(p) for p in partes]
    return (octetos[0] << 24) | (octetos[1] << 16) | (octetos[2] << 8) | octetos[3]


def inteiro_para_ip(valor):
    return ".".join(str((valor >> deslocamento) & 0xFF) for deslocamento in (24, 16, 8, 0))


def calcular_sub_rede(entrada):
    if "/" not in entrada:
        raise ValueError(f"Formato invalido: '{entrada}' (esperado A.B.C.D/CIDR)")
    ip_str, cidr_str = entrada.split("/")
    if not cidr_str.isdigit():
        raise ValueError(f"CIDR invalido: '{cidr_str}'")
    cidr = int(cidr_str)
    if cidr < 0 or cidr > 32:
        raise ValueError(f"CIDR invalido: {cidr} (deve estar entre 0 e 32)")

    ip_int = ip_para_inteiro(ip_str)
    mascara_int = (0xFFFFFFFF << (32 - cidr)) & 0xFFFFFFFF if cidr > 0 else 0

    rede_int = ip_int & mascara_int
    broadcast_int = rede_int | (~mascara_int & 0xFFFFFFFF)

    bits_host = 32 - cidr
    total_enderecos = 1 << bits_host
    hosts_utilizaveis = max(total_enderecos - 2, 0)

    if hosts_utilizaveis > 0:
        primeiro_host = inteiro_para_ip(rede_int + 1)
        ultimo_host = inteiro_para_ip(broadcast_int - 1)
    else:
        primeiro_host = ultimo_host = "N/A (bloco muito pequeno)"

    return {
        "entrada": entrada,
        "mascara": inteiro_para_ip(mascara_int),
        "rede": inteiro_para_ip(rede_int),
        "broadcast": inteiro_para_ip(broadcast_int),
        "primeiro_host": primeiro_host,
        "ultimo_host": ultimo_host,
        "hosts_utilizaveis": hosts_utilizaveis,
    }


def imprimir_resultado(resultado):
    print(f"Entrada:             {resultado['entrada']}")
    print(f"Mascara:              {resultado['mascara']}")
    print(f"Endereco de rede:     {resultado['rede']}")
    print(f"Endereco de broadcast: {resultado['broadcast']}")
    print(f"Primeiro host valido: {resultado['primeiro_host']}")
    print(f"Ultimo host valido:   {resultado['ultimo_host']}")
    print(f"Hosts utilizaveis:    {resultado['hosts_utilizaveis']}")


def main():
    entradas = sys.argv[1:] if len(sys.argv) > 1 else [
        "192.168.1.50/24",
        "10.5.20.130/25",
        "172.16.32.10/20",
        "192.168.5.70/26",
        "200.10.10.200/30",
    ]
    for i, entrada in enumerate(entradas):
        if i > 0:
            print("-" * 50)
        try:
            resultado = calcular_sub_rede(entrada)
            imprimir_resultado(resultado)
        except ValueError as erro:
            print(f"Erro em '{entrada}': {erro}")


if __name__ == "__main__":
    main()
