import java.util.*;

/**
 * RC1501 - Calculadora VLSM (Variable Length Subnet Mask)
 * Aula 15: VLSM e Projeto Estruturado de Sub-redes
 *
 * Mesma logica do RC1501-CalculadoraVLSM.py: recebe um bloco base e uma
 * lista de quantidades de hosts necessarios por sub-rede, ordena do maior
 * para o menor requisito, e gera o plano VLSM (rede, prefixo, faixa de
 * hosts utilizaveis e broadcast de cada sub-rede).
 */
class CalculadoraVLSM {

    static class Requisito {
        String nome;
        int hosts;
        Requisito(String nome, int hosts) { this.nome = nome; this.hosts = hosts; }
    }

    static class SubRedeVLSM {
        String nome;
        int hostsNecessarios;
        int prefixo;
        String cidr;
        String primeiroHost;
        String ultimoHost;
        String broadcast;
    }

    static int menorPrefixoPara(int hostsNecessarios) {
        int bitsHost = 2;
        while ((1L << bitsHost) - 2 < hostsNecessarios) {
            bitsHost++;
        }
        return 32 - bitsHost;
    }

    static String intParaIp(long endereco) {
        return String.format("%d.%d.%d.%d",
                (endereco >> 24) & 0xFF, (endereco >> 16) & 0xFF,
                (endereco >> 8) & 0xFF, endereco & 0xFF);
    }

    static long ipParaInt(String ip) {
        String[] partes = ip.split("\\.");
        long resultado = 0;
        for (String p : partes) {
            resultado = (resultado << 8) | Long.parseLong(p);
        }
        return resultado;
    }

    static List<SubRedeVLSM> calcularVlsm(String blocoBase, List<Requisito> requisitos) {
        String[] partesBloco = blocoBase.split("/");
        long enderecoBase = ipParaInt(partesBloco[0]);
        int prefixoBase = Integer.parseInt(partesBloco[1]);
        long tamanhoBlocoBase = 1L << (32 - prefixoBase);
        long limiteSuperior = enderecoBase + tamanhoBlocoBase - 1;

        List<Requisito> ordenados = new ArrayList<>(requisitos);
        ordenados.sort((a, b) -> Integer.compare(b.hosts, a.hosts));

        List<SubRedeVLSM> plano = new ArrayList<>();
        long proximoEndereco = enderecoBase;

        for (Requisito r : ordenados) {
            int prefixo = menorPrefixoPara(r.hosts);
            long tamanhoBloco = 1L << (32 - prefixo);

            if (proximoEndereco % tamanhoBloco != 0) {
                proximoEndereco += tamanhoBloco - (proximoEndereco % tamanhoBloco);
            }

            if (proximoEndereco + tamanhoBloco - 1 > limiteSuperior) {
                throw new IllegalArgumentException(
                        "Bloco base " + blocoBase + " nao comporta todos os requisitos "
                        + "(faltou espaco para '" + r.nome + "' com " + r.hosts + " hosts).");
            }

            SubRedeVLSM sub = new SubRedeVLSM();
            sub.nome = r.nome;
            sub.hostsNecessarios = r.hosts;
            sub.prefixo = prefixo;
            sub.cidr = intParaIp(proximoEndereco) + "/" + prefixo;
            sub.broadcast = intParaIp(proximoEndereco + tamanhoBloco - 1);
            if (tamanhoBloco > 2) {
                sub.primeiroHost = intParaIp(proximoEndereco + 1);
                sub.ultimoHost = intParaIp(proximoEndereco + tamanhoBloco - 2);
            } else {
                sub.primeiroHost = intParaIp(proximoEndereco);
                sub.ultimoHost = intParaIp(proximoEndereco + tamanhoBloco - 1);
            }

            plano.add(sub);
            proximoEndereco += tamanhoBloco;
        }

        return plano;
    }

    static void imprimirPlano(String blocoBase, List<SubRedeVLSM> plano) {
        System.out.println("=".repeat(100));
        System.out.println("Plano VLSM para o bloco base: " + blocoBase);
        System.out.println("=".repeat(100));
        System.out.printf("%-18s%-12s%-20s%-28s%-16s%n",
                "Sub-rede", "Hosts nec.", "CIDR", "Faixa de hosts", "Broadcast");
        System.out.println("-".repeat(100));
        for (SubRedeVLSM item : plano) {
            String faixa = item.primeiroHost + " - " + item.ultimoHost;
            System.out.printf("%-18s%-12d%-20s%-28s%-16s%n",
                    item.nome, item.hostsNecessarios, item.cidr, faixa, item.broadcast);
        }
        System.out.println("=".repeat(100));
    }

    public static void main(String[] args) {
        String blocoBase = "172.16.0.0/24";
        List<Requisito> requisitos = Arrays.asList(
                new Requisito("Edificio D", 28),
                new Requisito("Edificio A", 15),
                new Requisito("Edificio B", 12),
                new Requisito("Edificio C", 6),
                new Requisito("WAN 1", 2),
                new Requisito("WAN 2", 2),
                new Requisito("WAN 3", 2)
        );
        List<SubRedeVLSM> plano = calcularVlsm(blocoBase, requisitos);
        imprimirPlano(blocoBase, plano);

        System.out.println();
        String blocoBase2 = "192.168.5.0/24";
        List<Requisito> requisitos2 = Arrays.asList(
                new Requisito("LAN 1", 60),
                new Requisito("LAN 2", 25),
                new Requisito("LAN 3", 12),
                new Requisito("WAN 1", 2),
                new Requisito("WAN 2", 2)
        );
        List<SubRedeVLSM> plano2 = calcularVlsm(blocoBase2, requisitos2);
        imprimirPlano(blocoBase2, plano2);
    }
}
