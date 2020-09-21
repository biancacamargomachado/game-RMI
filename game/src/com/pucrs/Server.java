package com.pucrs;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class Server extends UnicastRemoteObject implements JogoInterface {
    private static volatile String remoteHostName;
    private static int maxJogadas = 10;
    private static Random random = new Random();
    private static volatile boolean jogadoresRegistrados = false;
    private static volatile int maxJogadores;
    private static volatile List<Jogador> jogadores = new ArrayList<Jogador>();

    public Server() throws RemoteException {
    }

    public static void main(String[] args) throws RemoteException {
        System.out.println("Server has started");
        
        if (args.length != 2) {
            System.out.println("Usage: java Server <server ip> <max players>");
            System.exit(1);
        }
        maxJogadores = Integer.parseInt(args[1]);

        try {
            System.setProperty("java.rmi.server.hostname", args[0]);
            LocateRegistry.createRegistry(52369);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("java RMI registry already exists.");
        }

        // URL para conexão do cliente
        try {
            String server = "rmi://" + args[0] + ":52369/Connection";
            Naming.rebind(server, new Server());
            System.out.println("Server is ready.");
        } catch (Exception e) {
            System.out.println("Server failed: " + e);
        }

        while (playersList.size() < maxPlayers) {
            Thread.sleep(500);
        }
        jogadoresRegistrados = true;
        while (true) {
            if (jogadoresRegistrados == true) {
                for(Jogador j: jogadores){ 
                    String connectLocation = "rmi://" + remoteHostName + ":52369/Callback";
                    JogadorInterface jogador = null;
                    try {
                        System.out.println("Calling client back at : " + connectLocation);
                        jogador = (JogadorInterface) Naming.lookup(connectLocation);
                        jogador.inicia();
                    } catch (Exception e) {
                        System.out.println ("Callback failed: ");
                        e.printStackTrace();
                    }   
                }
            }
        }
    }

    @Override
    public int registra() throws RemoteException {
        if(jogadores.size() >= maxJogadores) return -1;
        int idJogador = random.nextInt();
        try {
            remoteHostName = getClientHost();
            Jogador jogador = new Jogador(id,remoteHostName);
            jogadores.add(jogador)
        } catch (Exception e) {
            System.out.println ("Failed to get client IP");
            e.printStackTrace();
        }
        // conferir contador, para setar jogadoresRegistrados para true
        return idJogador;
    }

    @Override
    public int joga(int id) throws RemoteException {
        for(Jogador j:jogadores){
            if(j.getId() == id){
                if(j.jogadas == maxJogadas){
                    String connectLocation = "rmi://" + p.getIp() + ":3001/client_if";
                    JogadorInterface client = null;
                    try {
                        client = (JogadorInterface) Naming.lookup(connectLocation);
                        client.finaliza();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                j.jogadas++;
                System.out.println("PlayerID:" + j.getId() + " played");
            }
        }
        return 0;
    }

    @Override
    public int encerra(int id) throws RemoteException {
        return 0;
    }
}