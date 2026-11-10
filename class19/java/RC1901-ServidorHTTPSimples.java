import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.StandardCharsets;

/**
 * RC1901 - Servidor HTTP Minimo
 * Aula 19: Camada de Aplicacao - DNS, DHCP, HTTP
 *
 * Responde 200 OK com corpo "Hello, Redes!" para qualquer requisicao GET.
 * Usa apenas a biblioteca padrao (com.sun.net.httpserver), sem dependencias externas.
 *
 * Uso:
 *   javac RC1901-ServidorHTTPSimples.java
 *   java ServidorHTTPSimples [porta]   (padrao: 8080)
 *   curl -v http://localhost:8080/
 */
class ServidorHTTPSimples {

    static final byte[] CORPO_RESPOSTA = "Hello, Redes!".getBytes(StandardCharsets.UTF_8);

    static class Handler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            exchange.getResponseHeaders().add("Content-Type", "text/plain; charset=utf-8");
            exchange.sendResponseHeaders(200, CORPO_RESPOSTA.length);
            try (OutputStream os = exchange.getResponseBody()) {
                os.write(CORPO_RESPOSTA);
            }
            System.out.println("[servidor] " + exchange.getRemoteAddress() + " - "
                    + exchange.getRequestMethod() + " " + exchange.getRequestURI());
        }
    }

    public static void main(String[] args) throws IOException {
        int porta = args.length > 0 ? Integer.parseInt(args[0]) : 8080;
        HttpServer servidor = HttpServer.create(new InetSocketAddress(porta), 0);
        servidor.createContext("/", new Handler());
        servidor.setExecutor(null);
        servidor.start();
        System.out.println("Servidor HTTP simples ouvindo em http://localhost:" + porta + "/");
        System.out.println("Pressione Ctrl+C para encerrar.");
    }
}
