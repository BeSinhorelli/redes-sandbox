import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;

/**
 * RC1804 - Cliente UDP (eco)
 * Aula 18: Camada de Transporte, TCP e UDP
 *
 * Envia datagramas ao servidor UDP (RC1803) sem estabelecer conexao previa
 * (nenhum handshake) e imprime cada resposta de eco recebida.
 *
 * Uso: java ClienteUDP [host] [porta]
 *      (padrao: host=127.0.0.1, porta=5001)
 */
class ClienteUDP {

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int porta = args.length > 1 ? Integer.parseInt(args[1]) : 5001;
        String[] mensagens = {"ola servidor udp", "segundo datagrama", "terceiro datagrama", "sair"};

        InetAddress endereco = InetAddress.getByName(host);
        try (DatagramSocket cliente = new DatagramSocket()) {
            cliente.setSoTimeout(3000);
            for (String msg : mensagens) {
                byte[] dadosEnvio = msg.getBytes("UTF-8");
                DatagramPacket pacoteEnvio = new DatagramPacket(dadosEnvio, dadosEnvio.length, endereco, porta);
                cliente.send(pacoteEnvio);
                System.out.println("Enviado: " + msg);
                if (msg.equalsIgnoreCase("sair")) {
                    break;
                }
                try {
                    byte[] buffer = new byte[1024];
                    DatagramPacket pacoteResposta = new DatagramPacket(buffer, buffer.length);
                    cliente.receive(pacoteResposta);
                    String resposta = new String(pacoteResposta.getData(), 0, pacoteResposta.getLength(), "UTF-8");
                    System.out.println("Resposta: " + resposta);
                } catch (SocketTimeoutException e) {
                    System.out.println("Sem resposta (timeout) - datagrama pode ter se perdido.");
                }
            }
        }
        System.out.println("Cliente encerrado.");
    }
}
