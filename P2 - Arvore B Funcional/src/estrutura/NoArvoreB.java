package estrutura;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class NoArvoreB<T extends Comparable<T> & Serializable> implements Serializable {
    public static final int t = 2;
    private int n;
    private List<T> chaves;
    private List<NoArvoreB<T>> filhos;
    private boolean folha;

    public NoArvoreB(boolean folha) {
        this.folha = folha;
        this.chaves = new ArrayList<>(2 * t - 1);
        this.filhos = new ArrayList<>(2 * t);
        this.n = 0;
    }

    public int getN() {
        return n;
    }

    public void setN(int n) {
        this.n = n;
    }

    public List<T> getChaves() {
        return chaves;
    }

    public List<NoArvoreB<T>> getFilhos() {
        return filhos;
    }

    public boolean isFolha() {
        return folha;
    }

    public void setFolha(boolean folha) {
        this.folha = folha;
    }
}
