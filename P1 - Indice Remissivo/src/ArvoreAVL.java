import java.util.*;

public class ArvoreAVL implements EstruturaDeDados {
    private No raiz;

    private class No {
        String palavra;
        List<Integer> linhas;
        int altura;
        No esquerda;
        No direita;

        No(String palavra, int linha) {
            this.palavra = palavra;
            this.linhas = new ArrayList<>();
            this.linhas.add(linha);
            this.altura = 1;
        }
    }

    @Override
    public void inserir(String palavra, int linha) {
        raiz = inserir(raiz, palavra, linha);
    }

    private No inserir(No no, String palavra, int linha) {
        if (no == null) {
            return new No(palavra, linha);
        }

        if (palavra.compareTo(no.palavra) < 0) {
            no.esquerda = inserir(no.esquerda, palavra, linha);
        } else if (palavra.compareTo(no.palavra) > 0) {
            no.direita = inserir(no.direita, palavra, linha);
        } else {
            no.linhas.add(linha);
            return no;
        }

        no.altura = 1 + Math.max(altura(no.esquerda), altura(no.direita));
        return balancear(no);
    }

    @Override
    public void buscar(String palavra) {
        long inicio = System.nanoTime();
        No resultado = buscar(raiz, palavra);
        long fim = System.nanoTime();
        if (resultado != null) {
            System.out.println("Palavra encontrada nas linhas: " + resultado.linhas);
            System.out.println("Tempo de busca: " + (fim - inicio) + " ns");
        } else {
            System.out.println("Palavra não encontrada.");
        }
    }

    private No buscar(No no, String palavra) {
        if (no == null) {
            return null;
        }

        if (palavra.compareTo(no.palavra) < 0) {
            return buscar(no.esquerda, palavra);
        } else if (palavra.compareTo(no.palavra) > 0) {
            return buscar(no.direita, palavra);
        } else {
            return no;
        }
    }

    public void remover(String palavra) {
        raiz = remover(raiz, palavra);
    }

    private No remover(No no, String palavra) {
        if (no == null) {
            return null;
        }

        if (palavra.compareTo(no.palavra) < 0) {
            no.esquerda = remover(no.esquerda, palavra);
        } else if (palavra.compareTo(no.palavra) > 0) {
            no.direita = remover(no.direita, palavra);
        } else {
            if ((no.esquerda == null) || (no.direita == null)) {
                No temp = no.esquerda != null ? no.esquerda : no.direita;
                if (temp == null) {
                    no = null;
                } else {
                    no = temp;
                }
            } else {
                No temp = getMinValueNode(no.direita);
                no.palavra = temp.palavra;
                no.linhas = temp.linhas;
                no.direita = remover(no.direita, temp.palavra);
            }
        }

        if (no == null) {
            return no;
        }

        no.altura = Math.max(altura(no.esquerda), altura(no.direita)) + 1;
        return balancear(no);
    }

    private No getMinValueNode(No no) {
        No atual = no;
        while (atual.esquerda != null) {
            atual = atual.esquerda;
        }
        return atual;
    }

    private No balancear(No no) {
        int balanceamento = fatorBalanceamento(no);
        if (balanceamento > 1) {
            if (fatorBalanceamento(no.esquerda) < 0) {
                no.esquerda = rotacaoEsquerda(no.esquerda);
            }
            no = rotacaoDireita(no);
        } else if (balanceamento < -1) {
            if (fatorBalanceamento(no.direita) > 0) {
                no.direita = rotacaoDireita(no.direita);
            }
            no = rotacaoEsquerda(no);
        }
        return no;
    }

    private int altura(No no) {
        return no == null ? 0 : no.altura;
    }

    private int fatorBalanceamento(No no) {
        return no == null ? 0 : altura(no.esquerda) - altura(no.direita);
    }

    private No rotacaoDireita(No y) {
        No x = y.esquerda;
        No T2 = x.direita;
        x.direita = y;
        y.esquerda = T2;
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;
        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;
        return x;
    }

    private No rotacaoEsquerda(No x) {
        No y = x.direita;
        No T2 = y.esquerda;
        y.esquerda = x;
        x.direita = T2;
        x.altura = Math.max(altura(x.esquerda), altura(x.direita)) + 1;
        y.altura = Math.max(altura(y.esquerda), altura(y.direita)) + 1;
        return y;
    }

    public void imprimir() {
        imprimir(raiz);
    }

    private void imprimir(No no) {
        if (no != null) {
            imprimir(no.esquerda);
            System.out.println(no.palavra + ": " + no.linhas);
            imprimir(no.direita);
        }
    }
}