import java.io.*;
import java.util.*;

public class IndiceRemissivo {
    
    public static void main(String[] args) throws IOException {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("Digite o valor de X (número mínimo de caracteres para considerar uma palavra):");
        int x = scanner.nextInt();
        
        System.out.println("Escolha a estrutura de dados para o índice remissivo:");
        System.out.println("1 - Hash");
        System.out.println("2 - Árvore AVL");
        System.out.println("3 - Árvore Rubro-Negra");
        int escolhaEstrutura = scanner.nextInt();
        
        int escolhaColisao = 0;
        if (escolhaEstrutura == 1) {
            System.out.println("Escolha a estratégia de tratamento de colisões:");
            System.out.println("1 - Tentativa Linear");
            System.out.println("2 - Tentativa Quadrática");
            escolhaColisao = scanner.nextInt();
        }
        
        EstruturaDeDados estrutura;
        if (escolhaEstrutura == 1) {
            estrutura = new TabelaHash(escolhaColisao);
        } else if (escolhaEstrutura == 2) {
            estrutura = new ArvoreAVL();
        } else {
            estrutura = new ArvoreRB();
        }
        
        processarArquivos(x, estrutura);
        
        scanner.nextLine();  // Limpar o buffer
        while (true) {
            System.out.println("Escolha uma opção:");
            System.out.println("1 - Buscar palavra");
            System.out.println("2 - Remover palavra");
            System.out.println("3 - Imprimir índice");
            System.out.println("4 - Sair");
            int opcao = scanner.nextInt();
            scanner.nextLine();  // Limpar o buffer

            if (opcao == 1) {
                System.out.println("Digite a palavra para buscar:");
                String palavra = scanner.nextLine();
                estrutura.buscar(palavra);
            } else if (opcao == 2) {
                System.out.println("Digite a palavra para remover:");
                String palavra = scanner.nextLine();
                estrutura.remover(palavra);
            } else if (opcao == 3) {
                estrutura.imprimir();
            } else if (opcao == 4) {
                break;
            }
        }
        
        scanner.close();
    }
    
    public static void processarArquivos(int x, EstruturaDeDados estrutura) throws IOException {
        // Ajuste para usar o caminho relativo correto
        String basePath = System.getProperty("user.dir") + "/Codigos_Trabalhos CP/Projetos_Java/Trabalho_2_ED2/arquivos/";
        String[] arquivos = {basePath + "texto1.txt", basePath + "texto2.txt"};
        
        for (String arquivo : arquivos) {
            BufferedReader leitor = new BufferedReader(new FileReader(arquivo));
            String linha;
            int numeroLinha = 1;
            while ((linha = leitor.readLine()) != null) {
                processarLinha(linha, numeroLinha, x, estrutura);
                numeroLinha++;
            }
            leitor.close();
        }
    }
    
    public static void processarLinha(String linha, int numeroLinha, int x, EstruturaDeDados estrutura) {
        String[] palavras = linha.split("\\W+");
        for (String palavra : palavras) {
            if (palavra.length() > x) {
                estrutura.inserir(palavra, numeroLinha);
            }
        }
    }
}