import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * RC2201 - Verificador de Senha Forte
 * Aula 22: Fundamentos de Seguranca de Redes
 *
 * Avalia uma senha segundo criterios basicos de forca (comprimento, maiuscula,
 * minuscula, numero, caractere especial, ausencia de sequencias obvias) e
 * classifica como FRACA, MEDIA ou FORTE.
 *
 * Uso:
 *   javac RC2201-VerificadorSenhaForte.java
 *   java VerificadorSenhaForte "minhaSenha123"
 *   java VerificadorSenhaForte   (roda exemplos padrao)
 */
class VerificadorSenhaForte {

    static final int COMPRIMENTO_MINIMO = 8;
    static final String[] SEQUENCIAS_OBVIAS = {
            "123", "234", "345", "456", "567", "678", "789", "012",
            "abc", "bcd", "cde", "qwerty", "senha", "password", "admin",
    };

    static boolean temSequenciaObvia(String senha) {
        String senhaLower = senha.toLowerCase();
        for (String seq : SEQUENCIAS_OBVIAS) {
            if (senhaLower.contains(seq)) {
                return true;
            }
        }
        return false;
    }

    static class Avaliacao {
        String classificacao;
        List<String> naoAtendidos;
    }

    static Avaliacao avaliarSenha(String senha) {
        Map<String, Boolean> criterios = new LinkedHashMap<>();
        criterios.put("comprimento minimo (8+)", senha.length() >= COMPRIMENTO_MINIMO);
        criterios.put("letra maiuscula", Pattern.compile("[A-Z]").matcher(senha).find());
        criterios.put("letra minuscula", Pattern.compile("[a-z]").matcher(senha).find());
        criterios.put("numero", Pattern.compile("[0-9]").matcher(senha).find());
        criterios.put("caractere especial", Pattern.compile("[^A-Za-z0-9]").matcher(senha).find());
        criterios.put("sem sequencia obvia", !temSequenciaObvia(senha));

        int atendidos = 0;
        List<String> naoAtendidos = new ArrayList<>();
        for (Map.Entry<String, Boolean> e : criterios.entrySet()) {
            if (e.getValue()) {
                atendidos++;
            } else {
                naoAtendidos.add(e.getKey());
            }
        }

        Avaliacao a = new Avaliacao();
        a.naoAtendidos = naoAtendidos;
        if (atendidos <= 2) {
            a.classificacao = "FRACA";
        } else if (atendidos <= 4) {
            a.classificacao = "MEDIA";
        } else {
            a.classificacao = "FORTE";
        }
        return a;
    }

    static void imprimirResultado(String senha) {
        Avaliacao a = avaliarSenha(senha);
        System.out.println("Senha: " + senha);
        System.out.println("Classificacao: " + a.classificacao);
        if (!a.naoAtendidos.isEmpty()) {
            System.out.println("Criterios nao atendidos: " + String.join(", ", a.naoAtendidos));
        } else {
            System.out.println("Criterios nao atendidos: nenhum");
        }
    }

    public static void main(String[] args) {
        String[] senhas = args.length > 0 ? args : new String[]{
                "123456",
                "Fernando1990",
                "C@valo-Azul#Corre77!",
        };
        for (int i = 0; i < senhas.length; i++) {
            if (i > 0) {
                System.out.println("-".repeat(50));
            }
            imprimirResultado(senhas[i]);
        }
    }
}
