#!/usr/bin/env python3
"""
RC2201 - Verificador de Senha Forte
Aula 22: Fundamentos de Seguranca de Redes

Avalia uma senha segundo criterios basicos de forca (comprimento, maiuscula,
minuscula, numero, caractere especial, ausencia de sequencias obvias) e
classifica como FRACA, MEDIA ou FORTE.

Uso:
    python3 RC2201-VerificadorSenhaForte.py "minhaSenha123"
    python3 RC2201-VerificadorSenhaForte.py   (roda exemplos padrao)
"""
import re
import sys

COMPRIMENTO_MINIMO = 8
SEQUENCIAS_OBVIAS = [
    "123", "234", "345", "456", "567", "678", "789", "012",
    "abc", "bcd", "cde", "qwerty", "senha", "password", "admin",
]


def tem_sequencia_obvia(senha):
    senha_lower = senha.lower()
    return any(seq in senha_lower for seq in SEQUENCIAS_OBVIAS)


def avaliar_senha(senha):
    criterios = {
        "comprimento minimo (8+)": len(senha) >= COMPRIMENTO_MINIMO,
        "letra maiuscula": bool(re.search(r"[A-Z]", senha)),
        "letra minuscula": bool(re.search(r"[a-z]", senha)),
        "numero": bool(re.search(r"[0-9]", senha)),
        "caractere especial": bool(re.search(r"[^A-Za-z0-9]", senha)),
        "sem sequencia obvia": not tem_sequencia_obvia(senha),
    }
    atendidos = sum(1 for ok in criterios.values() if ok)
    nao_atendidos = [nome for nome, ok in criterios.items() if not ok]

    if atendidos <= 2:
        classificacao = "FRACA"
    elif atendidos <= 4:
        classificacao = "MEDIA"
    else:
        classificacao = "FORTE"

    return classificacao, nao_atendidos


def imprimir_resultado(senha):
    classificacao, nao_atendidos = avaliar_senha(senha)
    print(f"Senha: {senha}")
    print(f"Classificacao: {classificacao}")
    if nao_atendidos:
        print(f"Criterios nao atendidos: {', '.join(nao_atendidos)}")
    else:
        print("Criterios nao atendidos: nenhum")


def main():
    senhas = sys.argv[1:] if len(sys.argv) > 1 else [
        "123456",
        "Fernando1990",
        "C@valo-Azul#Corre77!",
    ]
    for i, senha in enumerate(senhas):
        if i > 0:
            print("-" * 50)
        imprimir_resultado(senha)


if __name__ == "__main__":
    main()
