import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

/**
 * RC1803 - Servidor UDP (eco)
 * Aula 18: Camada de Transporte, TCP e UDP
 *
 * Servidor UDP simples: sem conexao, sem handshake - a cada datagrama
 * recebido, responde com um eco numerado para o remetente. Encerra apos
 * receber "sair".
 *
 * Uso: java ServidorUDP [porta]   (porta padrao: 5001)
 */
class ServidorUDP {

    public static void main(String[] args) throws IOException {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : 5001;

        try (DatagramSocket servidor = new DatagramSocket(porta)) {
            System.out.println("Servidor UDP escutando em 0.0.0.0:" + porta + " (Ctrl+C para parar)");

            byte[] buffer = new byte[1024];
            int contador = 0;
            while (true) {
                DatagramPacket pacoteRecebido = new DatagramPacket(buffer, buffer.length);
                servidor.receive(pacoteRecebido);
                String mensagem = new String(pacoteRecebido.getData(), 0, pacoteRecebido.getLength(), "UTF-8").trim();

                if (mensagem.equalsIgnoreCase("sair")) {
                    System.out.println("Cliente " + pacoteRecebido.getSocketAddress() + " solicitou encerramento.");
                    break;
                }
                contador++;
                String resposta = "Recebido (datagrama #" + contador + "): " + mensagem;
                byte[] dadosResposta = resposta.getBytes("UTF-8");
                DatagramPacket pacoteResposta = new DatagramPacket(
                        dadosResposta, dadosResposta.length,
                        pacoteRecebido.getAddress(), pacoteRecebido.getPort());
                servidor.send(pacoteResposta);
                System.out.println("  " + pacoteRecebido.getSocketAddress() + " -> " + resposta);
            }
        } catch (SocketException e) {
            System.out.println("Erro no socket: " + e.getMessage());
        }
        System.out.println("Servidor encerrado.");
    }
}
