import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * RC1801 - Servidor TCP (eco)
 * Aula 18: Camada de Transporte, TCP e UDP
 *
 * Servidor TCP simples: aceita uma conexao, recebe mensagens de texto e
 * responde com um eco numerado ("Recebido (mensagem #N): <mensagem>").
 * Encerra a conexao quando o cliente envia "sair" ou fecha o socket.
 *
 * Uso: java ServidorTCP [porta]   (porta padrao: 5000)
 */
class ServidorTCP {

    public static void main(String[] args) throws IOException {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : 5000;

        try (ServerSocket servidor = new ServerSocket(porta)) {
            System.out.println("Servidor TCP escutando em 0.0.0.0:" + porta + " (Ctrl+C para parar)");

            try (Socket conexao = servidor.accept()) {
                System.out.println("Conexao aceita de " + conexao.getRemoteSocketAddress());
                BufferedReader entrada = new BufferedReader(new InputStreamReader(conexao.getInputStream()));
                PrintWriter saida = new PrintWriter(conexao.getOutputStream(), true);

                int contador = 0;
                String mensagem;
                while ((mensagem = entrada.readLine()) != null) {
                    mensagem = mensagem.trim();
                    if (mensagem.equalsIgnoreCase("sair")) {
                        System.out.println("Cliente solicitou encerramento.");
                        break;
                    }
                    contador++;
                    String resposta = "Recebido (mensagem #" + contador + "): " + mensagem;
                    saida.println(resposta);
                    System.out.println("  -> " + resposta);
                }
            }
        }
        System.out.println("Servidor encerrado.");
    }
}
