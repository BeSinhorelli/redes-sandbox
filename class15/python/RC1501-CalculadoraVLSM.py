#!/usr/bin/env python3
"""
RC1501 - Calculadora VLSM (Variable Length Subnet Mask)
Aula 15: VLSM e Projeto Estruturado de Sub-redes

Recebe um bloco base (ex: "172.16.0.0/24") e uma lista de quantidades de
hosts necessarios por sub-rede. Ordena do maior para o menor requisito e
gera o plano de sub-redes VLSM: endereco de rede, prefixo, faixa de hosts
utilizaveis e broadcast de cada uma.
"""
import ipaddress


def menor_prefixo_para(hosts_necessarios):
    """Retorna o menor prefixo (maior numero) que comporta hosts_necessarios,
    considerando o endereco de rede e o de broadcast reservados."""
    bits_host = 2
    while (2 ** bits_host) - 2 < hosts_necessarios:
        bits_host += 1
    return 32 - bits_host


def calcular_vlsm(bloco_base, requisitos):
    """requisitos: lista de (nome, hosts_necessarios). Retorna lista de dicts
    com o plano VLSM, ordenado do maior para o menor requisito."""
    rede_base = ipaddress.ip_network(bloco_base, strict=True)
    requisitos_ordenados = sorted(requisitos, key=lambda r: r[1], reverse=True)

    plano = []
    proximo_endereco = int(rede_base.network_address)
    limite_superior = int(rede_base.broadcast_address)

    for nome, hosts in requisitos_ordenados:
        prefixo = menor_prefixo_para(hosts)
        tamanho_bloco = 2 ** (32 - prefixo)

        if proximo_endereco % tamanho_bloco != 0:
            proximo_endereco += tamanho_bloco - (proximo_endereco % tamanho_bloco)

        if proximo_endereco + tamanho_bloco - 1 > limite_superior:
            raise ValueError(
                f"Bloco base {bloco_base} nao comporta todos os requisitos "
                f"(faltou espaco para '{nome}' com {hosts} hosts)."
            )

        sub_rede = ipaddress.ip_network((proximo_endereco, prefixo), strict=True)
        hosts_utilizaveis = list(sub_rede.hosts())

        plano.append({
            "nome": nome,
            "hosts_necessarios": hosts,
            "prefixo": prefixo,
            "rede": str(sub_rede.network_address),
            "mascara": str(sub_rede.netmask),
            "cidr": f"{sub_rede.network_address}/{prefixo}",
            "primeiro_host": str(hosts_utilizaveis[0]) if hosts_utilizaveis else None,
            "ultimo_host": str(hosts_utilizaveis[-1]) if hosts_utilizaveis else None,
            "broadcast": str(sub_rede.broadcast_address),
            "hosts_disponiveis": len(hosts_utilizaveis),
        })

        proximo_endereco += tamanho_bloco

    return plano


def imprimir_plano(bloco_base, plano):
    print("=" * 100)
    print(f"Plano VLSM para o bloco base: {bloco_base}")
    print("=" * 100)
    cabecalho = f"{'Sub-rede':<18}{'Hosts nec.':<12}{'CIDR':<20}{'Faixa de hosts':<28}{'Broadcast':<16}"
    print(cabecalho)
    print("-" * 100)
    for item in plano:
        faixa = f"{item['primeiro_host']} - {item['ultimo_host']}"
        print(
            f"{item['nome']:<18}{item['hosts_necessarios']:<12}{item['cidr']:<20}"
            f"{faixa:<28}{item['broadcast']:<16}"
        )
    print("=" * 100)


def main():
    bloco_base = "172.16.0.0/24"
    requisitos = [
        ("Edificio D", 28),
        ("Edificio A", 15),
        ("Edificio B", 12),
        ("Edificio C", 6),
        ("WAN 1", 2),
        ("WAN 2", 2),
        ("WAN 3", 2),
    ]
    plano = calcular_vlsm(bloco_base, requisitos)
    imprimir_plano(bloco_base, plano)

    print()
    bloco_base2 = "192.168.5.0/24"
    requisitos2 = [
        ("LAN 1", 60),
        ("LAN 2", 25),
        ("LAN 3", 12),
        ("WAN 1", 2),
        ("WAN 2", 2),
    ]
    plano2 = calcular_vlsm(bloco_base2, requisitos2)
    imprimir_plano(bloco_base2, plano2)


if __name__ == "__main__":
    main()
