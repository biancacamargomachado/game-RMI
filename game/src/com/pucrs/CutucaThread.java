package com.pucrs;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class CutucaThread extends Thread {
    private volatile List<Jogador> jogadores;

    public CutucaThread(List<Jogador> jogadores) {
        super();
        this.jogadores = jogadores;
    }

    @Override
    public void start() {
        while(true) {
            synchronized (this.jogadores) {
                try {
                    for (Jogador j: this.jogadores) {
                        String connectLocation = "rmi://" + j.getIp() + ":52369/Callback";
                        JogadorInterface client = null;
                        try {
                            client = (JogadorInterface) Naming.lookup(connectLocation);
                            client.cutuca();
                            System.out.println("Jogador " + j.getId() + " ATIVO!");
                        } catch (Exception e) {
                            System.out.println("Jogador " + j.getId()+" INATIVO!");
                            e.printStackTrace();
                        }
                    }
                    Thread.sleep(3000);
                }
                catch(RemoteException | InterruptedException exception) {
                    System.out.println("");
                }
            }
        }
    }
}
