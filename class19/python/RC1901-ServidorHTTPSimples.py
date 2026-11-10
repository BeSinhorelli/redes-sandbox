#!/usr/bin/env python3
"""
RC1901 - Servidor HTTP Minimo
Aula 19: Camada de Aplicacao - DNS, DHCP, HTTP

Responde 200 OK com corpo "Hello, Redes!" para qualquer requisicao GET.
Usa apenas a biblioteca padrao (http.server), sem dependencias externas.

Uso:
    python3 RC1901-ServidorHTTPSimples.py [porta]   (padrao: 8080)
    curl -v http://localhost:8080/
"""
import sys
from http.server import BaseHTTPRequestHandler, HTTPServer

CORPO_RESPOSTA = b"Hello, Redes!"


class Handler(BaseHTTPRequestHandler):
    def do_GET(self):
        self.send_response(200)
        self.send_header("Content-Type", "text/plain; charset=utf-8")
        self.send_header("Content-Length", str(len(CORPO_RESPOSTA)))
        self.end_headers()
        self.wfile.write(CORPO_RESPOSTA)

    def log_message(self, formato, *args):
        print(f"[servidor] {self.address_string()} - {formato % args}")


def main():
    porta = int(sys.argv[1]) if len(sys.argv) > 1 else 8080
    servidor = HTTPServer(("0.0.0.0", porta), Handler)
    print(f"Servidor HTTP simples ouvindo em http://localhost:{porta}/")
    print("Pressione Ctrl+C para encerrar.")
    try:
        servidor.serve_forever()
    except KeyboardInterrupt:
        print("\nEncerrando servidor...")
        servidor.shutdown()


if __name__ == "__main__":
    main()
