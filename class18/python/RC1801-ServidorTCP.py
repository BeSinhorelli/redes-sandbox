#!/usr/bin/env python3
"""
RC1801 - Servidor TCP (eco)
Aula 18: Camada de Transporte, TCP e UDP

Servidor TCP simples: aceita uma conexao, recebe mensagens de texto e
responde com um eco numerado ("Recebido (mensagem #N): <mensagem>").
Encerra a conexao quando o cliente envia "sair" ou fecha o socket.

Uso: python3 RC1801-ServidorTCP.py [porta]   (porta padrao: 5000)
"""
import socket
import sys

HOST = "0.0.0.0"


def rodar_servidor(porta):
    with socket.socket(socket.AF_INET, socket.SOCK_STREAM) as servidor:
        servidor.setsockopt(socket.SOL_SOCKET, socket.SO_REUSEADDR, 1)
        servidor.bind((HOST, porta))
        servidor.listen(1)
        print(f"Servidor TCP escutando em {HOST}:{porta} (Ctrl+C para parar)")

        conexao, endereco = servidor.accept()
        print(f"Conexao aceita de {endereco}")
        with conexao:
            entrada = conexao.makefile("r", encoding="utf-8", newline="\n")
            saida = conexao.makefile("w", encoding="utf-8", newline="\n")
            contador = 0
            for linha in entrada:
                mensagem = linha.strip()
                if mensagem.lower() == "sair":
                    print("Cliente solicitou encerramento.")
                    break
                contador += 1
                resposta = f"Recebido (mensagem #{contador}): {mensagem}"
                saida.write(resposta + "\n")
                saida.flush()
                print(f"  -> {resposta}")
    print("Servidor encerrado.")


def main():
    porta = int(sys.argv[1]) if len(sys.argv) > 1 else 5000
    rodar_servidor(porta)


if __name__ == "__main__":
    main()
