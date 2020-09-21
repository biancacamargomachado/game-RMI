public class Jogador {
    private int id;
    private String ip;
    public int contaJogadas = 0;

   
    public Jogador(int id, String ip) {
        this.id = id;
        this.ip = ip;
    }

    public int getId() {
        return this.id;
    }

    public String getIp() {
        return this.ip;
    }
}