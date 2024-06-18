package documentos;

import estrutura.NoArvoreB;
import java.io.*;

public class GerenciadorArquivo {

    public static <T extends Comparable<T> & Serializable> void salvarArvore(String nomeArquivo, NoArvoreB<T> raiz) throws IOException {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(nomeArquivo))) {
            oos.writeObject(raiz);
        }
    }

    @SuppressWarnings("unchecked")
    public static <T extends Comparable<T> & Serializable> NoArvoreB<T> carregarArvore(String nomeArquivo, Class<T> clazz) throws IOException, ClassNotFoundException {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(nomeArquivo))) {
            return (NoArvoreB<T>) ois.readObject();
        }
    }
}
