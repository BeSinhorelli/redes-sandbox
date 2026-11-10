import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * RC1802 - Cliente TCP (eco)
 * Aula 18: Camada de Transporte, TCP e UDP
 *
 * Conecta ao servidor TCP (RC1801), envia algumas mensagens de teste e
 * imprime cada resposta de eco recebida. Encerra enviando "sair".
 *
 * Uso: java ClienteTCP [host] [porta]
 *      (padrao: host=127.0.0.1, porta=5000)
 */
class ClienteTCP {

    public static void main(String[] args) throws IOException {
        String host = args.length > 0 ? args[0] : "127.0.0.1";
        int porta = args.length > 1 ? Integer.parseInt(args[1]) : 5000;
        String[] mensagens = {"ola servidor", "segunda mensagem", "terceira mensagem", "sair"};

        try (Socket cliente = new Socket(host, porta)) {
            System.out.println("Conectado a " + host + ":" + porta);
            BufferedReader entrada = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
            PrintWriter saida = new PrintWriter(cliente.getOutputStream(), true);

            for (String msg : mensagens) {
                saida.println(msg);
                System.out.println("Enviado: " + msg);
                if (msg.equalsIgnoreCase("sair")) {
                    break;
                }
                String resposta = entrada.readLine();
                System.out.println("Resposta: " + resposta);
            }
        }
        System.out.println("Cliente encerrado.");
    }
}
