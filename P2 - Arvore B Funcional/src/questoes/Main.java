package questoes;

import estrutura.*;
import documentos.GerenciadorArquivo;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Main {
    private static final Scanner scanner = new Scanner(System.in);
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");

    public static void main(String[] args) {
        ArvoreB<Usuario> arvoreB;
        OperacoesArvoreB<Usuario> operacoesArvoreB;

        try {
            arvoreB = new ArvoreB<>(GerenciadorArquivo.carregarArvore("arvore_b_dados.ser", Usuario.class), Usuario.class);
            operacoesArvoreB = new OperacoesArvoreB<>(arvoreB);
        } catch (IOException | ClassNotFoundException e) {
            arvoreB = new ArvoreB<>(Usuario.class);
            operacoesArvoreB = new OperacoesArvoreB<>(arvoreB);
            System.out.println("Arquivo não encontrado, criando nova árvore.");
        }

        boolean running = true;
        while (running) {
            System.out.println("Escolha uma operação:");
            System.out.println("1. Inserir usuário");
            System.out.println("2. Buscar usuário");
            System.out.println("3. Excluir usuário");
            System.out.println("4. Visualizar estrutura dos nós");
            System.out.println("5. Sair");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Opção inválida. Tente novamente.");
                continue;
            }

            switch (choice) {
                case 1:
                    inserirUsuario(operacoesArvoreB);
                    break;
                case 2:
                    buscarUsuario(operacoesArvoreB);
                    break;
                case 3:
                    excluirUsuario(operacoesArvoreB);
                    break;
                case 4:
                    visualizarEstrutura(arvoreB);
                    break;
                case 5:
                    running = false;
                    operacoesArvoreB.salvarDados();
                    operacoesArvoreB.salvarDadosTexto();
                    System.out.println("Dados salvos. Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        }
    }

    private static void inserirUsuario(OperacoesArvoreB<Usuario> operacoesArvoreB) {
        try {
            System.out.print("ID do usuário: ");
            int id = Integer.parseInt(scanner.nextLine());
            System.out.print("Login: ");
            String login = scanner.nextLine();
            System.out.print("Nome: ");
            String nome = scanner.nextLine();
            System.out.print("Email: ");
            String email = scanner.nextLine();
            System.out.print("Data de nascimento (dd/MM/yyyy): ");
            Date dataNascimento = dateFormat.parse(scanner.nextLine());
            System.out.print("Foto: ");
            String foto = scanner.nextLine();

            Usuario usuario = new Usuario(id, login, nome, email, dataNascimento, foto);
            operacoesArvoreB.inserir(usuario);
            System.out.println("Usuário inserido com sucesso.");
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Deve ser um número.");
        } catch (ParseException e) {
            System.out.println("Erro ao inserir usuário. Data de nascimento inválida.");
        }
    }

    private static void buscarUsuario(OperacoesArvoreB<Usuario> operacoesArvoreB) {
        try {
            System.out.print("ID do usuário a buscar: ");
            int id = Integer.parseInt(scanner.nextLine());
            Usuario usuarioBusca = new Usuario(id, "", "", "", null, "");
            Usuario usuarioEncontrado = operacoesArvoreB.buscar(usuarioBusca);
            if (usuarioEncontrado != null) {
                System.out.println("Usuário encontrado: " + usuarioEncontrado);
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Deve ser um número.");
        }
    }

    private static void excluirUsuario(OperacoesArvoreB<Usuario> operacoesArvoreB) {
        try {
            System.out.print("ID do usuário a excluir: ");
            int id = Integer.parseInt(scanner.nextLine());
            Usuario usuarioExcluir = new Usuario(id, "", "", "", null, "");
            if (operacoesArvoreB.buscar(usuarioExcluir) != null) {
                operacoesArvoreB.excluir(usuarioExcluir);
                System.out.println("Usuário excluído com sucesso.");
            } else {
                System.out.println("Usuário não encontrado.");
            }
        } catch (NumberFormatException e) {
            System.out.println("ID inválido. Deve ser um número.");
        }
    }

    private static void visualizarEstrutura(ArvoreB<Usuario> arvoreB) {
        System.out.println("Estrutura da árvore B:");
        NoArvoreB<Usuario> raiz = arvoreB.getRaiz();
        if (raiz == null || raiz.getN() == 0) {
            System.out.println("A árvore está vazia.");
        } else {
            visualizarNo(raiz, 0, "Nó Raiz");
        }
    }

    private static void visualizarNo(NoArvoreB<Usuario> no, int nivel, String posicao) {
        if (no != null) {
            for (int i = 0; i < nivel; i++) {
                System.out.print("    ");
            }
            System.out.println(posicao + " - " + noParaString(no));
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
                    visualizarNo(no.getFilhos().get(i), nivel + 1, novaPosicao);
                }
            }
        }
    }

    private static String noParaString(NoArvoreB<Usuario> no) {
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
