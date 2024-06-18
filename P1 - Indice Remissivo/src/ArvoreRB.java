import java.util.*;

public class ArvoreRB implements EstruturaDeDados {
    private final boolean RED = true;
    private final boolean BLACK = false;

    private class No {
        String palavra;
        List<Integer> linhas;
        No esquerda, direita, pai;
        boolean cor;

        No(String palavra, int linha, boolean cor) {
            this.palavra = palavra;
            this.linhas = new ArrayList<>();
            this.linhas.add(linha);
            this.cor = cor;
        }
    }

    private No raiz;

    @Override
    public void inserir(String palavra, int linha) {
        raiz = inserir(raiz, palavra, linha);
        raiz.cor = BLACK;
    }

    private No inserir(No h, String palavra, int linha) {
        if (h == null) {
            return new No(palavra, linha, RED);
        }

        if (palavra.compareTo(h.palavra) < 0) {
            h.esquerda = inserir(h.esquerda, palavra, linha);
            h.esquerda.pai = h;
        } else if (palavra.compareTo(h.palavra) > 0) {
            h.direita = inserir(h.direita, palavra, linha);
            h.direita.pai = h;
        } else {
            h.linhas.add(linha);
        }

        if (isRed(h.direita) && !isRed(h.esquerda)) {
            h = rotacaoEsquerda(h);
        }
        if (isRed(h.esquerda) && isRed(h.esquerda.esquerda)) {
            h = rotacaoDireita(h);
        }
        if (isRed(h.esquerda) && isRed(h.direita)) {
            inverterCores(h);
        }

        return h;
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
        while (no != null) {
            if (palavra.compareTo(no.palavra) < 0) {
                no = no.esquerda;
            } else if (palavra.compareTo(no.palavra) > 0) {
                no = no.direita;
            } else {
                return no;
            }
        }
        return null;
    }

    public void remover(String palavra) {
        raiz = remover(raiz, palavra);
        if (raiz != null) {
            raiz.cor = BLACK;
        }
    }

    private No remover(No h, String palavra) {
        if (h == null) return null;

        if (palavra.compareTo(h.palavra) < 0) {
            if (!isRed(h.esquerda) && !isRed(h.esquerda.esquerda)) {
                h = moverVermelhoEsquerda(h);
            }
            h.esquerda = remover(h.esquerda, palavra);
        } else {
            if (isRed(h.esquerda)) {
                h = rotacaoDireita(h);
            }
            if (palavra.compareTo(h.palavra) == 0 && (h.direita == null)) {
                return null;
            }
            if (!isRed(h.direita) &&  !isRed(h.direita.esquerda)) {
                h = moverVermelhoDireita(h);
            }
            if (palavra.compareTo(h.palavra) == 0) {
                No min = getMinValueNode(h.direita);
                h.palavra = min.palavra;
                h.linhas = min.linhas;
                h.direita = removerMin(h.direita);
            } else {
                h.direita = remover(h.direita, palavra);
            }
        }
        return balancear(h);
    }

    private No removerMin(No h) {
        if (h.esquerda == null) return null;
        if (!isRed(h.esquerda) && !isRed(h.esquerda.esquerda)) {
            h = moverVermelhoEsquerda(h);
        }
        h.esquerda = removerMin(h.esquerda);
        return balancear(h);
    }

    private No getMinValueNode(No no) {
        while (no.esquerda != null) {
            no = no.esquerda;
        }
        return no;
    }

    private No moverVermelhoEsquerda(No h) {
        inverterCores(h);
        if (isRed(h.direita.esquerda)) {
            h.direita = rotacaoDireita(h.direita);
            h = rotacaoEsquerda(h);
            inverterCores(h);
        }
        return h;
    }

    private No moverVermelhoDireita(No h) {
        inverterCores(h);
        if (isRed(h.esquerda.esquerda)) {
            h = rotacaoDireita(h);
            inverterCores(h);
        }
        return h;
    }

    private boolean isRed(No no) {
        return no != null && no.cor == RED;
    }

    private No rotacaoEsquerda(No h) {
        No x = h.direita;
        h.direita = x.esquerda;
        x.esquerda = h;
        x.cor = h.cor;
        h.cor = RED;
        return x;
    }

    private No rotacaoDireita(No h) {
        No x = h.esquerda;
        h.esquerda = x.direita;
        x.direita = h;
        x.cor = h.cor;
        h.cor = RED;
        return x;
    }

    private void inverterCores(No h) {
        h.cor = !h.cor;
        h.esquerda.cor = !h.esquerda.cor;
        h.direita.cor = !h.direita.cor;
    }

    private No balancear(No h) {
        if (isRed(h.direita)) {
            h = rotacaoEsquerda(h);
        }
        if (isRed(h.esquerda) && isRed(h.esquerda.esquerda)) {
            h = rotacaoDireita(h);
        }
        if (isRed(h.esquerda) && isRed(h.direita)) {
            inverterCores(h);
        }
        return h;
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
