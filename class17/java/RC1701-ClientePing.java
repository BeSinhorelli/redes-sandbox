import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * RC1701 - Cliente de Ping (via comando do sistema)
 * Aula 17: ICMP, Ping e Traceroute
 *
 * Dispara ICMP Echo Request para um host usando o comando `ping` do sistema
 * operacional e reporta RTT medio e taxa de perda de pacotes a partir da
 * saida parseada.
 *
 * Nota: um socket ICMP "cru" (raw socket) exige privilegio de root/administrador
 * no Linux para ser criado. Para manter o exemplo executavel sem privilegios
 * especiais, delegamos o envio real dos pacotes ICMP ao utilitario `ping` do
 * sistema (que ja roda com as permissoes necessarias) e apenas interpretamos
 * a saida em texto - a mesma abordagem adotada na versao Python.
 */
class ClientePing {

    static String pingar(String host, int quantidade, int timeoutSegundos) throws Exception {
        ProcessBuilder pb = new ProcessBuilder(
                "ping", "-c", String.valueOf(quantidade), "-W", String.valueOf(timeoutSegundos), host);
        pb.redirectErrorStream(true);
        Process processo = pb.start();
        StringBuilder saida = new StringBuilder();
        try (BufferedReader leitor = new BufferedReader(new InputStreamReader(processo.getInputStream()))) {
            String linha;
            while ((linha = leitor.readLine()) != null) {
                saida.append(linha).append("\n");
            }
        }
        processo.waitFor();
        return saida.toString();
    }

    static class Estatisticas {
        Integer enviados;
        Integer recebidos;
        Double perdaPercentual;
        Double rttMin;
        Double rttAvg;
        Double rttMax;
    }

    static Estatisticas parsearEstatisticas(String saida) {
        Estatisticas stats = new Estatisticas();

        Matcher m1 = Pattern.compile("(\\d+) packets transmitted, (\\d+) (?:packets )?received").matcher(saida);
        if (m1.find()) {
            stats.enviados = Integer.parseInt(m1.group(1));
            stats.recebidos = Integer.parseInt(m1.group(2));
        }

        Matcher m2 = Pattern.compile("([\\d.]+)% packet loss").matcher(saida);
        if (m2.find()) {
            stats.perdaPercentual = Double.parseDouble(m2.group(1));
        }

        Matcher m3 = Pattern.compile("= ([\\d.]+)/([\\d.]+)/([\\d.]+)").matcher(saida);
        if (m3.find()) {
            stats.rttMin = Double.parseDouble(m3.group(1));
            stats.rttAvg = Double.parseDouble(m3.group(2));
            stats.rttMax = Double.parseDouble(m3.group(3));
        }

        return stats;
    }

    static void relatorio(String host, Estatisticas stats) {
        System.out.println("--- Ping para " + host + " ---");
        if (stats.enviados == null) {
            System.out.println("  Nao foi possivel interpretar a saida (host pode estar inacessivel).");
            return;
        }
        System.out.println("  Pacotes enviados : " + stats.enviados);
        System.out.println("  Pacotes recebidos: " + stats.recebidos);
        System.out.println("  Perda de pacotes : " + stats.perdaPercentual + "%");
        if (stats.rttAvg != null) {
            System.out.println("  RTT min/avg/max  : " + stats.rttMin + "/" + stats.rttAvg + "/" + stats.rttMax + " ms");
        } else {
            System.out.println("  RTT nao disponivel (sem respostas).");
        }
    }

    public static void main(String[] args) throws Exception {
        String[] hosts = args.length > 0 ? args : new String[]{"127.0.0.1", "8.8.8.8"};
        for (String host : hosts) {
            String saida = pingar(host, 4, 2);
            Estatisticas stats = parsearEstatisticas(saida);
            relatorio(host, stats);
            System.out.println();
        }
    }
}
