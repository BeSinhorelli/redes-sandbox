#!/usr/bin/env python3
"""
RC1804 - Cliente UDP (eco)
Aula 18: Camada de Transporte, TCP e UDP

Envia datagramas ao servidor UDP (RC1803) sem estabelecer conexao previa
(nenhum handshake) e imprime cada resposta de eco recebida.

Uso: python3 RC1804-ClienteUDP.py [host] [porta]
     (padrao: host=127.0.0.1, porta=5001)
"""
import socket
import sys


def rodar_cliente(host, porta):
    mensagens = ["ola servidor udp", "segundo datagrama", "terceiro datagrama", "sair"]
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as cliente:
        cliente.settimeout(3)
        for msg in mensagens:
            cliente.sendto(msg.encode("utf-8"), (host, porta))
            print(f"Enviado: {msg}")
            if msg.lower() == "sair":
                break
            try:
                resposta, _ = cliente.recvfrom(1024)
                print(f"Resposta: {resposta.decode('utf-8')}")
            except socket.timeout:
                print("Sem resposta (timeout) - datagrama pode ter se perdido.")
    print("Cliente encerrado.")


def main():
    host = sys.argv[1] if len(sys.argv) > 1 else "127.0.0.1"
    porta = int(sys.argv[2]) if len(sys.argv) > 2 else 5001
    rodar_cliente(host, porta)


if __name__ == "__main__":
    main()
