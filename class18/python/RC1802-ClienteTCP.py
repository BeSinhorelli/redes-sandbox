#!/usr/bin/env python3
"""
RC1802 - Cliente TCP (eco)
Aula 18: Camada de Transporte, TCP e UDP

Conecta ao servidor TCP (RC1801), envia algumas mensagens de teste e
imprime cada resposta de eco recebida. Encerra enviando "sair".

Uso: python3 RC1802-ClienteTCP.py [host] [porta]
     (padrao: host=127.0.0.1, porta=5000)
"""
import socket
import sys


def rodar_cliente(host, porta):
    mensagens = ["ola servidor", "segunda mensagem", "terceira mensagem", "sair"]
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as cliente:
        cliente.connect((host, porta))
        print(f"Conectado a {host}:{porta}")
        entrada = cliente.makefile("r", encoding="utf-8", newline="\n")
        saida = cliente.makefile("w", encoding="utf-8", newline="\n")
        for msg in mensagens:
            saida.write(msg + "\n")
            saida.flush()
            print(f"Enviado: {msg}")
            if msg.lower() == "sair":
                break
            resposta = entrada.readline().rstrip("\n")
            print(f"Resposta: {resposta}")
    print("Cliente encerrado.")


def main():
    host = sys.argv[1] if len(sys.argv) > 1 else "127.0.0.1"
    porta = int(sys.argv[2]) if len(sys.argv) > 2 else 5000
    rodar_cliente(host, porta)


if __name__ == "__main__":
    main()
