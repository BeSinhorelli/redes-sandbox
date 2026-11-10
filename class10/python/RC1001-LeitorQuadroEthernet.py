#!/usr/bin/env python3
"""
RC1001 - Leitor de Quadro Ethernet II
Aula 10: Switching Ethernet

Recebe uma sequencia de bytes (os primeiros 14 bytes de um quadro Ethernet
II) e extrai: MAC de destino, MAC de origem e EtherType.

Layout dos primeiros 14 bytes de um quadro Ethernet II:
  bytes 0-5   : MAC de destino
  bytes 6-11  : MAC de origem
  bytes 12-13 : EtherType (ex: 0x0800 = IPv4, 0x0806 = ARP, 0x86DD = IPv6)
"""

ETHERTYPES = {
    0x0800: "IPv4",
    0x0806: "ARP",
    0x86DD: "IPv6",
}


def formatar_mac(seis_bytes):
    return ":".join(f"{b:02X}" for b in seis_bytes)


def classificar_mac(seis_bytes):
    if all(b == 0xFF for b in seis_bytes):
        return "broadcast"
    if seis_bytes[0] & 0x01:
        return "multicast"
    return "unicast"


def ler_quadro_ethernet(dados):
    if len(dados) < 14:
        raise ValueError("Quadro precisa ter pelo menos 14 bytes (cabecalho Ethernet II)")

    mac_destino = dados[0:6]
    mac_origem = dados[6:12]
    ethertype = (dados[12] << 8) | dados[13]

    return {
        "mac_destino": formatar_mac(mac_destino),
        "tipo_destino": classificar_mac(mac_destino),
        "mac_origem": formatar_mac(mac_origem),
        "tipo_origem": classificar_mac(mac_origem),
        "ethertype": f"0x{ethertype:04X}",
        "protocolo": ETHERTYPES.get(ethertype, "Desconhecido"),
    }


def imprimir_resultado(nome_quadro, dados):
    print("=" * 60)
    print(f"Quadro: {nome_quadro}")
    info = ler_quadro_ethernet(dados)
    print(f"  MAC destino  : {info['mac_destino']}  ({info['tipo_destino']})")
    print(f"  MAC origem   : {info['mac_origem']}  ({info['tipo_origem']})")
    print(f"  EtherType    : {info['ethertype']}  ({info['protocolo']})")


def main():
    # Exemplo 1: quadro unicast carregando um pacote IPv4
    quadro_unicast = bytes([
        0x3C, 0x22, 0xFB, 0x11, 0x22, 0x33,  # MAC destino (unicast)
        0x08, 0x00, 0x27, 0xA4, 0x5F, 0x2C,  # MAC origem (OUI VirtualBox 08:00:27)
        0x08, 0x00,                          # EtherType = IPv4
    ])

    # Exemplo 2: quadro broadcast (ex: um ARP request)
    quadro_broadcast = bytes([
        0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF,  # MAC destino (broadcast)
        0x08, 0x00, 0x27, 0xA4, 0x5F, 0x2C,  # MAC origem
        0x08, 0x06,                          # EtherType = ARP
    ])

    imprimir_resultado("Unicast / IPv4", quadro_unicast)
    imprimir_resultado("Broadcast / ARP", quadro_broadcast)
    print("=" * 60)


if __name__ == "__main__":
    main()
