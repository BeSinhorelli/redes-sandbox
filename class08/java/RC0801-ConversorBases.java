/**
 * RC0801 - Conversor de Bases (Binario / Decimal / Hexadecimal)
 * Aula 08: Sistemas de Numeracao
 *
 * Converte um numero decimal (0-255) para binario de 8 bits e hexadecimal
 * de 2 digitos, e vice-versa, usando o metodo da subtracao (potencias de 2)
 * ensinado na aula - o mesmo processo manual, so que automatizado.
 */
class ConversorBases {

    static final int[] POTENCIAS_DE_2 = {128, 64, 32, 16, 8, 4, 2, 1};

    static void validarDecimal(int n) {
        if (n < 0 || n > 255) {
            throw new IllegalArgumentException("Decimal fora do intervalo de um octeto (0-255): " + n);
        }
    }

    static void validarBinario(String bits) {
        if (bits.length() != 8 || !bits.matches("[01]{8}")) {
            throw new IllegalArgumentException("Binario invalido (precisa ter 8 digitos '0'/'1'): " + bits);
        }
    }

    static void validarHexadecimal(String hexa) {
        if (hexa.length() != 2 || !hexa.matches("(?i)[0-9A-F]{2}")) {
            throw new IllegalArgumentException("Hexadecimal invalido (precisa ter 2 digitos 0-9/A-F): " + hexa);
        }
    }

    /** Metodo da subtracao: percorre as potencias de 2 da maior para a menor. */
    static String decimalParaBinario(int n) {
        validarDecimal(n);
        StringBuilder bits = new StringBuilder();
        int resto = n;
        for (int valor : POTENCIAS_DE_2) {
            if (resto >= valor) {
                bits.append('1');
                resto -= valor;
            } else {
                bits.append('0');
            }
        }
        return bits.toString();
    }

    /** Soma as posicoes onde o bit e 1. */
    static int binarioParaDecimal(String bits) {
        validarBinario(bits);
        int total = 0;
        for (int i = 0; i < 8; i++) {
            if (bits.charAt(i) == '1') {
                total += POTENCIAS_DE_2[i];
            }
        }
        return total;
    }

    /** Passa pelo binario: converte para 8 bits, depois cada nibble para hex. */
    static String decimalParaHexadecimal(int n) {
        String bits = decimalParaBinario(n);
        String nibbleAlto = bits.substring(0, 4);
        String nibbleBaixo = bits.substring(4, 8);
        int alto = Integer.parseInt(nibbleAlto, 2);
        int baixo = Integer.parseInt(nibbleBaixo, 2);
        return String.format("%X%X", alto, baixo);
    }

    /** Passa pelo binario: cada digito hex vira um nibble de 4 bits. */
    static int hexadecimalParaDecimal(String hexa) {
        validarHexadecimal(hexa);
        StringBuilder bits = new StringBuilder();
        for (char c : hexa.toCharArray()) {
            int valor = Integer.parseInt(String.valueOf(c), 16);
            bits.append(String.format("%4s", Integer.toBinaryString(valor)).replace(' ', '0'));
        }
        return binarioParaDecimal(bits.toString());
    }

    static String formatarLinha(int decimal, String binario, String hexadecimal) {
        return String.format("  Decimal: %3d   |   Binario: %s   |   Hexadecimal: %s", decimal, binario, hexadecimal);
    }

    public static void main(String[] args) {
        System.out.println("=".repeat(60));
        System.out.println("CONVERSOR DE BASES - Decimal / Binario / Hexadecimal");
        System.out.println("=".repeat(60));

        int[] exemplos = {200, 45, 127, 255, 168, 210, 0, 16};
        for (int n : exemplos) {
            String bits = decimalParaBinario(n);
            String hexa = decimalParaHexadecimal(n);
            System.out.println(formatarLinha(n, bits, hexa));
        }

        System.out.println("\nConferindo o caminho inverso (binario/hex -> decimal):");
        String[] binarios = {"11001000", "01111111", "10101000"};
        for (String bits : binarios) {
            int n = binarioParaDecimal(bits);
            System.out.println("  Binario " + bits + " -> Decimal " + n);
        }
        String[] hexas = {"A8", "D2", "FF"};
        for (String hexa : hexas) {
            int n = hexadecimalParaDecimal(hexa);
            System.out.println("  Hexadecimal " + hexa + " -> Decimal " + n);
        }
    }
}
