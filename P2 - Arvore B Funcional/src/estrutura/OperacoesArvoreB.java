package estrutura;

import documentos.GerenciadorArquivo;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Serializable;

public class OperacoesArvoreB<T extends Comparable<T> & Serializable> {
    private ArvoreB<T> arvoreB;
    private String nomeArquivo = "arvore_b_dados.ser";
    private String nomeArquivoTexto = "arvore_b_dados.txt";

    public OperacoesArvoreB(ArvoreB<T> arvoreB) {
        this.arvoreB = arvoreB;
    }

    public void inserir(T chave) {
        NoArvoreB<T> r = arvoreB.getRaiz();
        if (r.getN() == 2 * NoArvoreB.t - 1) {
            NoArvoreB<T> s = new NoArvoreB<>(false);
            arvoreB.setRaiz(s);
            s.getFilhos().add(r);
            dividirFilho(s, 0, r);
            inserirNaoCheio(s, chave);
        } else {
            inserirNaoCheio(r, chave);
        }
        salvarDados();
        salvarDadosTexto();
    }

    private void inserirNaoCheio(NoArvoreB<T> x, T chave) {
        int i = x.getN() - 1;
        if (x.isFolha()) {
            x.getChaves().add(null);
            while (i >= 0 && chave.compareTo(x.getChaves().get(i)) < 0) {
                x.getChaves().set(i + 1, x.getChaves().get(i));
                i--;
            }
            x.getChaves().set(i + 1, chave);
            x.setN(x.getN() + 1);
        } else {
            while (i >= 0 && chave.compareTo(x.getChaves().get(i)) < 0) {
                i--;
            }
            i++;
            if (x.getFilhos().get(i).getN() == 2 * NoArvoreB.t - 1) {
                dividirFilho(x, i, x.getFilhos().get(i));
                if (chave.compareTo(x.getChaves().get(i)) > 0) {
                    i++;
                }
            }
            inserirNaoCheio(x.getFilhos().get(i), chave);
        }
    }

    private void dividirFilho(NoArvoreB<T> x, int i, NoArvoreB<T> y) {
        NoArvoreB<T> z = new NoArvoreB<>(y.isFolha());
        z.setN(NoArvoreB.t - 1);
        for (int j = 0; j < NoArvoreB.t - 1; j++) {
            z.getChaves().add(y.getChaves().get(j + NoArvoreB.t));
        }
        if (!y.isFolha()) {
            for (int j = 0; j < NoArvoreB.t; j++) {
                z.getFilhos().add(y.getFilhos().get(j + NoArvoreB.t));
            }
        }
        y.setN(NoArvoreB.t - 1);
        x.getFilhos().add(i + 1, z);
        x.getChaves().add(i, y.getChaves().get(NoArvoreB.t - 1));
        x.setN(x.getN() + 1);
    }

    public void excluir(T chave) {
        NoArvoreB<T> raiz = arvoreB.getRaiz();
        if (raiz == null || buscar(raiz, chave) == null) {
            System.out.println("A chave " + chave + " não existe na árvore.");
            return;
        }

        excluir(raiz, chave);
        if (raiz.getN() == 0) {
            if (!raiz.isFolha()) {
                arvoreB.setRaiz(raiz.getFilhos().get(0));
            } else {
                arvoreB.setRaiz(null);
            }
        }
        salvarDados();
        salvarDadosTexto();
    }

    private void excluir(NoArvoreB<T> x, T chave) {
        int idx = buscarChave(x, chave);
        if (idx < x.getN() && x.getChaves().get(idx).compareTo(chave) == 0) {
            if (x.isFolha()) {
                removerDeFolha(x, idx);
            } else {
                removerDeNaoFolha(x, idx);
            }
        } else {
            if (x.isFolha()) {
                System.out.println("A chave " + chave + " não existe na árvore.");
                return;
            }
            boolean flag = (idx == x.getN());
            if (x.getFilhos().get(idx).getN() < NoArvoreB.t) {
                preencher(x, idx);
            }
            if (flag && idx > x.getN()) {
                excluir(x.getFilhos().get(idx - 1), chave);
            } else {
                excluir(x.getFilhos().get(idx), chave);
            }
        }
    }

    private int buscarChave(NoArvoreB<T> x, T chave) {
        int idx = 0;
        while (idx < x.getN() && x.getChaves().get(idx).compareTo(chave) < 0) {
            idx++;
        }
        return idx;
    }

    private void removerDeFolha(NoArvoreB<T> x, int idx) {
        for (int i = idx + 1; i < x.getN(); i++) {
            x.getChaves().set(i - 1, x.getChaves().get(i));
        }
        x.setN(x.getN() - 1);
    }

    private void removerDeNaoFolha(NoArvoreB<T> x, int idx) {
        T k = x.getChaves().get(idx);
        if (x.getFilhos().get(idx).getN() >= NoArvoreB.t) {
            T pred = getPredecessor(x, idx);
            x.getChaves().set(idx, pred);
            excluir(x.getFilhos().get(idx), pred);
        } else if (x.getFilhos().get(idx + 1).getN() >= NoArvoreB.t) {
            T succ = getSucessor(x, idx);
            x.getChaves().set(idx, succ);
            excluir(x.getFilhos().get(idx + 1), succ);
        } else {
            juntar(x, idx);
            excluir(x.getFilhos().get(idx), k);
        }
    }

    private T getPredecessor(NoArvoreB<T> x, int idx) {
        NoArvoreB<T> cur = x.getFilhos().get(idx);
        while (!cur.isFolha()) {
            cur = cur.getFilhos().get(cur.getN());
        }
        return cur.getChaves().get(cur.getN() - 1);
    }

    private T getSucessor(NoArvoreB<T> x, int idx) {
        NoArvoreB<T> cur = x.getFilhos().get(idx + 1);
        while (!cur.isFolha()) {
            cur = cur.getFilhos().get(0);
        }
        return cur.getChaves().get(0);
    }

    private void juntar(NoArvoreB<T> x, int idx) {
        NoArvoreB<T> filho = x.getFilhos().get(idx);
        NoArvoreB<T> irmao = x.getFilhos().get(idx + 1);
        filho.getChaves().add(x.getChaves().get(idx));
        for (int i = 0; i < irmao.getN(); i++) {
            filho.getChaves().add(irmao.getChaves().get(i));
        }
        if (!filho.isFolha()) {
            for (int i = 0; i <= irmao.getN(); i++) {
                filho.getFilhos().add(irmao.getFilhos().get(i));
            }
        }
        for (int i = idx + 1; i < x.getN(); i++) {
            x.getChaves().set(i - 1, x.getChaves().get(i));
        }
        for (int i = idx + 2; i <= x.getN(); i++) {
            x.getFilhos().set(i - 1, x.getFilhos().get(i));
        }
        filho.setN(filho.getN() + irmao.getN() + 1);
        x.setN(x.getN() - 1);
    }

    private void preencher(NoArvoreB<T> x, int idx) {
        if (idx != 0 && x.getFilhos().get(idx - 1).getN() >= NoArvoreB.t) {
            pegarDoAnterior(x, idx);
        } else if (idx != x.getN() && x.getFilhos().get(idx + 1).getN() >= NoArvoreB.t) {
            pegarDoProximo(x, idx);
        } else {
            if (idx != x.getN()) {
                juntar(x, idx);
            } else {
                juntar(x, idx - 1);
            }
        }
    }

    private void pegarDoAnterior(NoArvoreB<T> x, int idx) {
        NoArvoreB<T> filho = x.getFilhos().get(idx);
        NoArvoreB<T> irmao = x.getFilhos().get(idx - 1);
        filho.getChaves().add(0, x.getChaves().get(idx - 1));
        if (!filho.isFolha()) {
            filho.getFilhos().add(0, irmao.getFilhos().get(irmao.getN()));
        }
        x.getChaves().set(idx - 1, irmao.getChaves().remove(irmao.getN() - 1));
        filho.setN(filho.getN() + 1);
        irmao.setN(irmao.getN() - 1);
    }

    private void pegarDoProximo(NoArvoreB<T> x, int idx) {
        NoArvoreB<T> filho = x.getFilhos().get(idx);
        NoArvoreB<T> irmao = x.getFilhos().get(idx + 1);
        filho.getChaves().add(x.getChaves().get(idx));
        if (!filho.isFolha()) {
            filho.getFilhos().add(irmao.getFilhos().remove(0));
        }
        x.getChaves().set(idx, irmao.getChaves().remove(0));
        filho.setN(filho.getN() + 1);
        irmao.setN(irmao.getN() - 1);
    }

    public T buscar(T chave) {
        NoArvoreB<T> raiz = arvoreB.getRaiz();
        if (raiz == null) return null;
        NoArvoreB<T> no = buscar(raiz, chave);
        if (no != null) {
            for (int i = 0; i < no.getN(); i++) {
                if (no.getChaves().get(i).compareTo(chave) == 0) {
                    return no.getChaves().get(i);
                }
            }
        }
        return null;
    }

    private NoArvoreB<T> buscar(NoArvoreB<T> no, T chave) {
        int i = 0;
        while (i < no.getN() && chave.compareTo(no.getChaves().get(i)) > 0) {
            i++;
        }
        if (i < no.getN() && chave.compareTo(no.getChaves().get(i)) == 0) {
            return no;
        }
        if (no.isFolha()) {
            return null;
        } else {
            return buscar(no.getFilhos().get(i), chave);
        }
    }

    public void salvarDados() {
        NoArvoreB<T> raiz = arvoreB.getRaiz();
        if (raiz == null) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivo))) {
                writer.write("");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            try {
                GerenciadorArquivo.salvarArvore(nomeArquivo, raiz);
                System.out.println("Dados salvos em " + nomeArquivo);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void salvarDadosTexto() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(nomeArquivoTexto))) {
            writer.write("Estrutura da Árvore B:\n");
            salvarNoTexto(writer, arvoreB.getRaiz(), 0, "Nó Raiz");
            System.out.println("Dados salvos em " + nomeArquivoTexto);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void salvarNoTexto(BufferedWriter writer, NoArvoreB<T> no, int nivel, String posicao) throws IOException {
        if (no != null) {
            for (int i = 0; i < nivel; i++) {
                writer.write("    ");
            }
            writer.write(posicao + " - " + noParaString(no));
            writer.newLine();
            if (!no.isFolha()) {
                for (int i = 0; i <= no.getN(); i++) {
                    String novaPosicao = "Nó Interno";
                    if (no.getFilhos().get(i).isFolha()) {
                        if (i == 0) {
                            novaPosicao = "Nó Folha Esquerda";
                        } else if (i == no.getN()) {
                            novaPosicao = "Nó Folha Direita";
                        } else {
                            novaPosicao = "Nó Folha";
                        }
                    }
                    salvarNoTexto(writer, no.getFilhos().get(i), nivel + 1, novaPosicao);
                }
            }
        }
    }

    private String noParaString(NoArvoreB<T> no) {
        StringBuilder sb = new StringBuilder();
        if (no.isFolha()) {
            sb.append("Nó Folha (").append(no.getN()).append(" chaves):\n");
        } else {
            sb.append("Nó Interno (").append(no.getN()).append(" chaves):\n");
        }
        for (int i = 0; i < no.getN(); i++) {
            sb.append("    ").append(no.getChaves().get(i)).append("\n");
        }
        return sb.toString();
    }
}
