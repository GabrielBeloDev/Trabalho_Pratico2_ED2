import java.util.*;

public class TabelaHash implements EstruturaDeDados {
    private static final int TAMANHO_INICIAL = 16;
    private static final double FATOR_CARGA = 0.75;
    
    private Entrada[] tabela;
    private int tamanho;
    private int ocupacao;
    private int metodoColisao;
    
    public TabelaHash(int metodoColisao) {
        this.tabela = new Entrada[TAMANHO_INICIAL];
        this.tamanho = TAMANHO_INICIAL;
        this.ocupacao = 0;
        this.metodoColisao = metodoColisao;
    }
    
    @Override
    public void inserir(String palavra, int linha) {
        if ((double) ocupacao / tamanho >= FATOR_CARGA) {
            redimensionar();
        }
        
        int indice = hash(palavra);
        int passo = 1;
        while (tabela[indice] != null && tabela[indice].estado == Estado.Ocupado) {
            if (tabela[indice].palavra.equals(palavra)) {
                tabela[indice].linhas.add(linha);
                return;
            }
            indice = proximoIndice(indice, passo);
            passo++;
        }
        
        tabela[indice] = new Entrada(palavra, linha, Estado.Ocupado);
        ocupacao++;
    }
    
    @Override
    public void buscar(String palavra) {
        long inicio = System.nanoTime();
        int indice = hash(palavra);
        int passo = 1;
        while (tabela[indice] != null) {
            if (tabela[indice].estado == Estado.Ocupado && tabela[indice].palavra.equals(palavra)) {
                long fim = System.nanoTime();
                System.out.println("Palavra encontrada nas linhas: " + tabela[indice].linhas);
                System.out.println("Tempo de busca: " + (fim - inicio) + " ns");
                return;
            }
            indice = proximoIndice(indice, passo);
            passo++;
        }
        long fim = System.nanoTime();
        System.out.println("Palavra não encontrada.");
        System.out.println("Tempo de busca: " + (fim - inicio) + " ns");
    }
    
    @Override
    public void remover(String palavra) {
        int indice = hash(palavra);
        int passo = 1;
        while (tabela[indice] != null) {
            if (tabela[indice].estado == Estado.Ocupado && tabela[indice].palavra.equals(palavra)) {
                tabela[indice].estado = Estado.Livre;
                ocupacao--;
                return;
            }
            indice = proximoIndice(indice, passo);
            passo++;
        }
        System.out.println("Palavra não encontrada.");
    }
    
    @Override
    public void imprimir() {
        for (Entrada entrada : tabela) {
            if (entrada != null && entrada.estado == Estado.Ocupado) {
                System.out.println(entrada.palavra + ": " + entrada.linhas);
            }
        }
    }
    
    private int hash(String palavra) {
        return Math.abs(palavra.hashCode()) % tamanho;
    }
    
    private int proximoIndice(int indice, int passo) {
        if (metodoColisao == 1) {
            return (indice + passo) % tamanho;
        } else {
            return (indice + passo * passo) % tamanho;
        }
    }
    
    private void redimensionar() {
        int novoTamanho = tamanho * 2;
        Entrada[] novaTabela = new Entrada[novoTamanho];
        
        for (Entrada entrada : tabela) {
            if (entrada != null && entrada.estado == Estado.Ocupado) {
                int indice = Math.abs(entrada.palavra.hashCode()) % novoTamanho;
                int passo = 1;
                while (novaTabela[indice] != null) {
                    indice = (indice + passo) % novoTamanho;
                    passo++;
                }
                novaTabela[indice] = entrada;
            }
        }
        
        tabela = novaTabela;
        tamanho = novoTamanho;
    }
    
    private static class Entrada {
        String palavra;
        List<Integer> linhas;
        Estado estado;
        
        Entrada(String palavra, int linha, Estado estado) {
            this.palavra = palavra;
            this.linhas = new ArrayList<>();
            this.linhas.add(linha);
            this.estado = estado;
        }
    }
    
    private enum Estado {
        Livre, Ocupado
    }
}