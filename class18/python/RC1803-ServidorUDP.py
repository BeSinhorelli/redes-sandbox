#!/usr/bin/env python3
"""
RC1803 - Servidor UDP (eco)
Aula 18: Camada de Transporte, TCP e UDP

Servidor UDP simples: sem conexao, sem handshake - a cada datagrama
recebido, responde com um eco numerado para o remetente. Encerra apos
receber "sair" ou apos um numero maximo de datagramas.

Uso: python3 RC1803-ServidorUDP.py [porta]   (porta padrao: 5001)
"""
import socket
import sys

HOST = "0.0.0.0"


def rodar_servidor(porta):
    with socket.socket(socket.AF_INET, socket.SOCK_DGRAM) as servidor:
        servidor.bind((HOST, porta))
        print(f"Servidor UDP escutando em {HOST}:{porta} (Ctrl+C para parar)")

        contador = 0
        while True:
            dados, endereco_cliente = servidor.recvfrom(1024)
            mensagem = dados.decode("utf-8").strip()
            if mensagem.lower() == "sair":
                print(f"Cliente {endereco_cliente} solicitou encerramento.")
                break
            contador += 1
            resposta = f"Recebido (datagrama #{contador}): {mensagem}"
            servidor.sendto(resposta.encode("utf-8"), endereco_cliente)
            print(f"  {endereco_cliente} -> {resposta}")
    print("Servidor encerrado.")


def main():
    porta = int(sys.argv[1]) if len(sys.argv) > 1 else 5001
    rodar_servidor(porta)


if __name__ == "__main__":
    main()
