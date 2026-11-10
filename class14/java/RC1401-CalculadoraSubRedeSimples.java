/**
 * RC1401 - Calculadora de Sub-rede Simples
 * Aula 14: Enderecamento IPv4 e Sub-redes
 *
 * Recebe um IPv4 no formato "A.B.C.D/CIDR" (ex: 192.168.1.50/24) e calcula:
 * endereco de rede, endereco de broadcast, primeiro/ultimo host valido e a
 * quantidade de hosts utilizaveis.
 *
 * Uso:
 *   javac RC1401-CalculadoraSubRedeSimples.java
 *   java CalculadoraSubRedeSimples 192.168.1.50/24
 *   java CalculadoraSubRedeSimples   (roda exemplos padrao)
 */
class CalculadoraSubRedeSimples {

    static int validarOcteto(String octeto) {
        if (!octeto.matches("\\d+")) {
            throw new IllegalArgumentException("Octeto invalido: '" + octeto + "' (nao numerico)");
        }
        int valor = Integer.parseInt(octeto);
        if (valor < 0 || valor > 255) {
            throw new IllegalArgumentException("Octeto invalido: " + valor + " (fora de 0-255)");
        }
        return valor;
    }

    static long ipParaInteiro(String ip) {
        String[] partes = ip.split("\\.");
        if (partes.length != 4) {
            throw new IllegalArgumentException("Endereco IPv4 invalido: '" + ip + "' (deve ter 4 octetos)");
        }
        long resultado = 0;
        for (String parte : partes) {
            resultado = (resultado << 8) | validarOcteto(parte);
        }
        return resultado;
    }

    static String inteiroParaIp(long valor) {
        return String.format("%d.%d.%d.%d",
                (valor >> 24) & 0xFF, (valor >> 16) & 0xFF, (valor >> 8) & 0xFF, valor & 0xFF);
    }

    static class ResultadoSubRede {
        String entrada, mascara, rede, broadcast, primeiroHost, ultimoHost;
        long hostsUtilizaveis;
    }

    static ResultadoSubRede calcularSubRede(String entrada) {
        if (!entrada.contains("/")) {
            throw new IllegalArgumentException("Formato invalido: '" + entrada + "' (esperado A.B.C.D/CIDR)");
        }
        String[] partesEntrada = entrada.split("/");
        String ipStr = partesEntrada[0];
        String cidrStr = partesEntrada[1];
        if (!cidrStr.matches("\\d+")) {
            throw new IllegalArgumentException("CIDR invalido: '" + cidrStr + "'");
        }
        int cidr = Integer.parseInt(cidrStr);
        if (cidr < 0 || cidr > 32) {
            throw new IllegalArgumentException("CIDR invalido: " + cidr + " (deve estar entre 0 e 32)");
        }

        long ipInt = ipParaInteiro(ipStr);
        long mascaraInt = cidr > 0 ? (0xFFFFFFFFL << (32 - cidr)) & 0xFFFFFFFFL : 0L;

        long redeInt = ipInt & mascaraInt;
        long broadcastInt = redeInt | (~mascaraInt & 0xFFFFFFFFL);

        int bitsHost = 32 - cidr;
        long totalEnderecos = 1L << bitsHost;
        long hostsUtilizaveis = Math.max(totalEnderecos - 2, 0);

        ResultadoSubRede r = new ResultadoSubRede();
        r.entrada = entrada;
        r.mascara = inteiroParaIp(mascaraInt);
        r.rede = inteiroParaIp(redeInt);
        r.broadcast = inteiroParaIp(broadcastInt);
        r.hostsUtilizaveis = hostsUtilizaveis;

        if (hostsUtilizaveis > 0) {
            r.primeiroHost = inteiroParaIp(redeInt + 1);
            r.ultimoHost = inteiroParaIp(broadcastInt - 1);
        } else {
            r.primeiroHost = r.ultimoHost = "N/A (bloco muito pequeno)";
        }
        return r;
    }

    static void imprimirResultado(ResultadoSubRede r) {
        System.out.println("Entrada:             " + r.entrada);
        System.out.println("Mascara:              " + r.mascara);
        System.out.println("Endereco de rede:     " + r.rede);
        System.out.println("Endereco de broadcast: " + r.broadcast);
        System.out.println("Primeiro host valido: " + r.primeiroHost);
        System.out.println("Ultimo host valido:   " + r.ultimoHost);
        System.out.println("Hosts utilizaveis:    " + r.hostsUtilizaveis);
    }

    public static void main(String[] args) {
        String[] entradas = args.length > 0 ? args : new String[]{
                "192.168.1.50/24",
                "10.5.20.130/25",
                "172.16.32.10/20",
                "192.168.5.70/26",
                "200.10.10.200/30",
        };
        for (int i = 0; i < entradas.length; i++) {
            if (i > 0) {
                System.out.println("-".repeat(50));
            }
            try {
                imprimirResultado(calcularSubRede(entradas[i]));
            } catch (IllegalArgumentException erro) {
                System.out.println("Erro em '" + entradas[i] + "': " + erro.getMessage());
            }
        }
    }
}
