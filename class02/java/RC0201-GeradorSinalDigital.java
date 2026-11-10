/**
 * RC0201 - Gerador de Formas de Onda NRZ e Manchester
 * Aula 02: Sinais Analogicos e Digitais, Modulacao e Codificacao
 *
 * Recebe uma sequencia de bits (String com '0'/'1') e imprime, em arte ASCII,
 * as formas de onda NRZ e Manchester correspondentes.
 *
 * Convencao Manchester adotada (G.E. Thomas):
 *   bit 0 -> transicao baixo->alto no meio do intervalo do bit
 *   bit 1 -> transicao alto->baixo no meio do intervalo do bit
 */
class GeradorSinalDigital {

    static final int LARGURA_BIT = 8; // colunas por bit (deve ser par para a Manchester)
    static final String ALTO = "▔";
    static final String BAIXO = "▁";
    static final String VAZIO = " ";

    static void validarBits(String bits) {
        if (bits == null || bits.isEmpty() || !bits.matches("[01]+")) {
            throw new IllegalArgumentException("A sequencia deve conter apenas os caracteres '0' e '1'.");
        }
    }

    static String repetir(String s, int n) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < n; i++) sb.append(s);
        return sb.toString();
    }

    /** Retorna {linhaAlta, linhaBaixa} para a codificacao NRZ. */
    static String[] gerarNrz(String bits) {
        StringBuilder topo = new StringBuilder();
        StringBuilder base = new StringBuilder();
        for (char bit : bits.toCharArray()) {
            if (bit == '1') {
                topo.append(repetir(ALTO, LARGURA_BIT));
                base.append(repetir(VAZIO, LARGURA_BIT));
            } else {
                topo.append(repetir(VAZIO, LARGURA_BIT));
                base.append(repetir(BAIXO, LARGURA_BIT));
            }
        }
        return new String[]{topo.toString(), base.toString()};
    }

    /** Retorna {linhaAlta, linhaBaixa} para a codificacao Manchester (G.E. Thomas). */
    static String[] gerarManchester(String bits) {
        int metade = LARGURA_BIT / 2;
        StringBuilder topo = new StringBuilder();
        StringBuilder base = new StringBuilder();
        for (char bit : bits.toCharArray()) {
            String primeira, segunda;
            if (bit == '0') {
                primeira = BAIXO;
                segunda = ALTO;
            } else {
                primeira = ALTO;
                segunda = BAIXO;
            }
            for (String simbolo : new String[]{primeira, segunda}) {
                if (simbolo.equals(ALTO)) {
                    topo.append(repetir(ALTO, metade));
                    base.append(repetir(VAZIO, metade));
                } else {
                    topo.append(repetir(VAZIO, metade));
                    base.append(repetir(BAIXO, metade));
                }
            }
        }
        return new String[]{topo.toString(), base.toString()};
    }

    /** Rotulo de cada bit centralizado sob seu bloco de colunas. */
    static String linhaDeRotulos(String bits) {
        StringBuilder rotulo = new StringBuilder();
        for (char bit : bits.toCharArray()) {
            int espacosEsquerda = (LARGURA_BIT - 1) / 2;
            int espacosDireita = LARGURA_BIT - 1 - espacosEsquerda;
            rotulo.append(repetir(" ", espacosEsquerda)).append(bit).append(repetir(" ", espacosDireita));
        }
        return rotulo.toString();
    }

    static String linhaDeFronteiras(String bits) {
        int total = bits.length() * LARGURA_BIT;
        StringBuilder linha = new StringBuilder();
        for (int col = 0; col < total; col++) {
            linha.append(col % LARGURA_BIT == 0 ? "+" : "-");
        }
        return linha.toString();
    }

    static void imprimirFormaDeOnda(String titulo, String bits) {
        String[] onda = titulo.equals("NRZ") ? gerarNrz(bits) : gerarManchester(bits);
        System.out.println();
        System.out.println(titulo + ":");
        System.out.println("  " + linhaDeRotulos(bits));
        System.out.println("  " + linhaDeFronteiras(bits));
        System.out.println("  " + onda[0]);
        System.out.println("  " + onda[1]);
    }

    public static void main(String[] args) {
        String[] exemplos = {"10110010", "00000000"};
        for (String bits : exemplos) {
            validarBits(bits);
            System.out.println("============================================================");
            System.out.println("Sequencia de bits: " + bits);
            imprimirFormaDeOnda("NRZ", bits);
            imprimirFormaDeOnda("Manchester", bits);
        }
        System.out.println("============================================================");
    }
}
