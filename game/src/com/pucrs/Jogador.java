package com.pucrs;

public class Jogador {
    private int id;
    private String ip;
    public int port;
    public boolean iniciado = false;
    public int contadorJogadas = 0;

   
    public Jogador(int id, String ip, int port) {
        this.id = id;
        this.ip = ip;
        this.port = port;
    }

    public int getId() {
        return this.id;
    }

    public String getIp() {
        return this.ip;
    }
}