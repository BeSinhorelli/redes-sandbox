import java.util.HashMap;
import java.util.Map;

/**
 * RC1001 - Leitor de Quadro Ethernet II
 * Aula 10: Switching Ethernet
 *
 * Recebe uma sequencia de bytes (os primeiros 14 bytes de um quadro Ethernet
 * II) e extrai: MAC de destino, MAC de origem e EtherType.
 *
 * Layout dos primeiros 14 bytes de um quadro Ethernet II:
 *   bytes 0-5   : MAC de destino
 *   bytes 6-11  : MAC de origem
 *   bytes 12-13 : EtherType (ex: 0x0800 = IPv4, 0x0806 = ARP, 0x86DD = IPv6)
 */
class LeitorQuadroEthernet {

    static final Map<Integer, String> ETHERTYPES = new HashMap<>();
    static {
        ETHERTYPES.put(0x0800, "IPv4");
        ETHERTYPES.put(0x0806, "ARP");
        ETHERTYPES.put(0x86DD, "IPv6");
    }

    static String formatarMac(int[] seisBytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < seisBytes.length; i++) {
            if (i > 0) sb.append(":");
            sb.append(String.format("%02X", seisBytes[i]));
        }
        return sb.toString();
    }

    static String classificarMac(int[] seisBytes) {
        boolean todosFF = true;
        for (int b : seisBytes) {
            if (b != 0xFF) { todosFF = false; break; }
        }
        if (todosFF) return "broadcast";
        if ((seisBytes[0] & 0x01) != 0) return "multicast";
        return "unicast";
    }

    static int[] fatiar(int[] dados, int inicio, int fim) {
        int[] r = new int[fim - inicio];
        System.arraycopy(dados, inicio, r, 0, fim - inicio);
        return r;
    }

    static Map<String, String> lerQuadroEthernet(int[] dados) {
        if (dados.length < 14) {
            throw new IllegalArgumentException("Quadro precisa ter pelo menos 14 bytes (cabecalho Ethernet II)");
        }

        int[] macDestino = fatiar(dados, 0, 6);
        int[] macOrigem = fatiar(dados, 6, 12);
        int ethertype = (dados[12] << 8) | dados[13];

        Map<String, String> info = new HashMap<>();
        info.put("mac_destino", formatarMac(macDestino));
        info.put("tipo_destino", classificarMac(macDestino));
        info.put("mac_origem", formatarMac(macOrigem));
        info.put("tipo_origem", classificarMac(macOrigem));
        info.put("ethertype", String.format("0x%04X", ethertype));
        info.put("protocolo", ETHERTYPES.getOrDefault(ethertype, "Desconhecido"));
        return info;
    }

    static void imprimirResultado(String nomeQuadro, int[] dados) {
        System.out.println("=".repeat(60));
        System.out.println("Quadro: " + nomeQuadro);
        Map<String, String> info = lerQuadroEthernet(dados);
        System.out.println("  MAC destino  : " + info.get("mac_destino") + "  (" + info.get("tipo_destino") + ")");
        System.out.println("  MAC origem   : " + info.get("mac_origem") + "  (" + info.get("tipo_origem") + ")");
        System.out.println("  EtherType    : " + info.get("ethertype") + "  (" + info.get("protocolo") + ")");
    }

    public static void main(String[] args) {
        // Exemplo 1: quadro unicast carregando um pacote IPv4
        int[] quadroUnicast = {
            0x3C, 0x22, 0xFB, 0x11, 0x22, 0x33, // MAC destino (unicast)
            0x08, 0x00, 0x27, 0xA4, 0x5F, 0x2C, // MAC origem (OUI VirtualBox 08:00:27)
            0x08, 0x00,                          // EtherType = IPv4
        };

        // Exemplo 2: quadro broadcast (ex: um ARP request)
        int[] quadroBroadcast = {
            0xFF, 0xFF, 0xFF, 0xFF, 0xFF, 0xFF, // MAC destino (broadcast)
            0x08, 0x00, 0x27, 0xA4, 0x5F, 0x2C, // MAC origem
            0x08, 0x06,                          // EtherType = ARP
        };

        imprimirResultado("Unicast / IPv4", quadroUnicast);
        imprimirResultado("Broadcast / ARP", quadroBroadcast);
        System.out.println("=".repeat(60));
    }
}
