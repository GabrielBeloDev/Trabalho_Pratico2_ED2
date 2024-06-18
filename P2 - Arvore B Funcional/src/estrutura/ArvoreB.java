package estrutura;

import java.io.Serializable;

public class ArvoreB<T extends Comparable<T> & Serializable> {
    private NoArvoreB<T> raiz;
    private Class<T> clazz;

    public ArvoreB(Class<T> clazz) {
        this.raiz = new NoArvoreB<>(true);
        this.clazz = clazz;
    }

    public ArvoreB(NoArvoreB<T> raiz, Class<T> clazz) {
        this.raiz = raiz;
        this.clazz = clazz;
    }

    public NoArvoreB<T> getRaiz() {
        return raiz;
    }

    public void setRaiz(NoArvoreB<T> raiz) {
        this.raiz = raiz;
    }

    public Class<T> getClazz() {
        return clazz;
    }
}
