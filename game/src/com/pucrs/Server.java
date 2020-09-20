package com.pucrs;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class Server extends UnicastRemoteObject implements JogoInterface {
    private static volatile String remoteHostName;
    private static Random random = new Random();
    private static volatile boolean jogadoresRegistrados = false;

    public Server() throws RemoteException {
    }

    public static void main(String[] args) throws RemoteException {
        System.out.println("Server has started");

        if (args.length != 1) {
            System.out.println("Usage: java Server <server ip>");
            System.exit(1);
        }

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

        while (true) {
            if (jogadoresRegistrados == true) {

                String connectLocation = "rmi://" + remoteHostName + ":52369/Callback";

                JogadorInterface jogador = null;
                try {
                    System.out.println("Calling client back at : " + connectLocation);
                    jogador = (JogadorInterface) Naming.lookup(connectLocation);
                } catch (Exception e) {
                    System.out.println ("Callback failed: ");
                    e.printStackTrace();
                }

                try {
                    jogador.inicia();
                } catch (RemoteException e) {
                    e.printStackTrace();
                }
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException ex) {}
        }
    }

    @Override
    public int registra() throws RemoteException {
        int idJogador = random.nextInt();
        // conferir contador, para setar jogadoresRegistrados para true
        return idJogador;
    }

    @Override
    public int joga(int id) throws RemoteException {
        return 0;
    }

    @Override
    public int encerra(int id) throws RemoteException {
        return 0;
    }
}