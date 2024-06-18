package estrutura;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Usuario implements Comparable<Usuario>, Serializable {
    private int id_usuario;
    private String login;
    private String nome;
    private String email;
    private Date data_nascimento;
    private String foto;

    public Usuario(int id_usuario, String login, String nome, String email, Date data_nascimento, String foto) {
        this.id_usuario = id_usuario;
        this.login = login;
        this.nome = nome;
        this.email = email;
        this.data_nascimento = data_nascimento;
        this.foto = foto;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public String getLogin() {
        return login;
    }

    public String getNome() {
        return nome;
    }

    public String getEmail() {
        return email;
    }

    public Date getData_nascimento() {
        return data_nascimento;
    }

    public String getFoto() {
        return foto;
    }

    @Override
    public int compareTo(Usuario other) {
        return Integer.compare(this.id_usuario, other.id_usuario);
    }

    @Override
    public String toString() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
        String dataNascimentoStr = dateFormat.format(data_nascimento);
        return "ID: " + id_usuario + "\n" +
                "Login: " + login + "\n" +
                "Nome: " + nome + "\n" +
                "Email: " + email + "\n" +
                "Data de Nascimento: " + dataNascimentoStr + "\n" +
                "Foto: " + foto + "\n";
    }
}
