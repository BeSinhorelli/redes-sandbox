"""
RC1701 - Cliente de Ping (via comando do sistema)
Aula 17: ICMP, Ping e Traceroute

Dispara ICMP Echo Request para um host usando o comando `ping` do sistema
operacional e reporta RTT medio e taxa de perda de pacotes a partir da
saida parseada.

Nota: um socket ICMP "cru" (raw socket) exige privilegio de root/administrador
no Linux para ser criado. Para manter o exemplo executavel sem privilegios
especiais, delegamos o envio real dos pacotes ICMP ao utilitario `ping` do
sistema (que ja roda com as permissoes necessarias) e apenas interpretamos
a saida em texto - a mesma abordagem adotada na versao Java.
"""
import re
import subprocess
import sys


def pingar(host, quantidade=4, timeout_segundos=2):
    comando = ["ping", "-c", str(quantidade), "-W", str(timeout_segundos), host]
    resultado = subprocess.run(comando, capture_output=True, text=True)
    return resultado.stdout


def parsear_estatisticas(saida):
    enviados = recebidos = None
    perda_percentual = None
    rtt_min = rtt_avg = rtt_max = None

    m = re.search(r"(\d+) packets transmitted, (\d+) (?:packets )?received", saida)
    if m:
        enviados, recebidos = int(m.group(1)), int(m.group(2))

    m = re.search(r"([\d.]+)% packet loss", saida)
    if m:
        perda_percentual = float(m.group(1))

    m = re.search(r"= ([\d.]+)/([\d.]+)/([\d.]+)", saida)
    if m:
        rtt_min, rtt_avg, rtt_max = (float(m.group(1)), float(m.group(2)), float(m.group(3)))

    return {
        "enviados": enviados,
        "recebidos": recebidos,
        "perda_percentual": perda_percentual,
        "rtt_min_ms": rtt_min,
        "rtt_avg_ms": rtt_avg,
        "rtt_max_ms": rtt_max,
    }


def relatorio(host, stats):
    print(f"--- Ping para {host} ---")
    if stats["enviados"] is None:
        print("  Nao foi possivel interpretar a saida (host pode estar inacessivel).")
        return
    print(f"  Pacotes enviados : {stats['enviados']}")
    print(f"  Pacotes recebidos: {stats['recebidos']}")
    print(f"  Perda de pacotes : {stats['perda_percentual']}%")
    if stats["rtt_avg_ms"] is not None:
        print(f"  RTT min/avg/max  : {stats['rtt_min_ms']}/{stats['rtt_avg_ms']}/{stats['rtt_max_ms']} ms")
    else:
        print("  RTT nao disponivel (sem respostas).")


def main():
    hosts = sys.argv[1:] if len(sys.argv) > 1 else ["127.0.0.1", "8.8.8.8"]
    for host in hosts:
        saida = pingar(host)
        stats = parsear_estatisticas(saida)
        relatorio(host, stats)
        print()


if __name__ == "__main__":
    main()
