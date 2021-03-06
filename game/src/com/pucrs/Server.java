package com.pucrs;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class Server extends UnicastRemoteObject implements JogoInterface {
    private static volatile String remoteHostName;
    // private static volatile int maxJogadas = 10;
    private static volatile Random random = new Random();
    private static volatile boolean jogadoresRegistrados = false;
    private static volatile int maxJogadores;
    private static int qtdJogadoresJogando = 0;
    private static volatile List<Jogador> jogadores = new ArrayList<Jogador>();

    public Server() throws RemoteException {
    }

    public static void main(String[] args) throws RemoteException, InterruptedException {
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

        while (jogadores.size() < maxJogadores) {
            Thread.sleep(500);
        }
        jogadoresRegistrados = true;
        // controlador para quando um jogador encerra
        qtdJogadoresJogando = maxJogadores;

        while (true) {
            if (jogadoresRegistrados == true && qtdJogadoresJogando > 0) {
                for(Jogador j: jogadores){
                    String connectLocation = "rmi://" + j.getIp() + ":52369/Callback";
                    JogadorInterface jogador = null;
                    try {
                        if(!j.iniciado) {
                            j.iniciado = true;
                            System.out.println("Calling client back at : " + connectLocation);
                            jogador = (JogadorInterface) Naming.lookup(connectLocation);

                        // se este jogador não foi iniciado ainda
                            jogador.inicia();
                        }
                    } catch (Exception e) {
                        System.out.println ("Callback failed: ");
                        e.printStackTrace();
                    }
                }
                new CutucaThread(jogadores).start();
            }
        }
    }

    @Override
    public int registra() throws RemoteException {
        // inicia o jogo
        if(jogadores.size() >= maxJogadores)
            return -1;

        int idJogador = random.nextInt();

        try {
            remoteHostName = getClientHost();
            Jogador jogador = new Jogador(idJogador,remoteHostName);
            jogadores.add(jogador);
        } catch (Exception e) {
            System.out.println ("Failed to get client IP");
            e.printStackTrace();
        }
        return idJogador;
    }

    @Override
    public int joga(int id) throws RemoteException {
        //TODO deve existir uma probabilidade de 1% do servidor finalizar o jogador (chamar método finaliza() do cliente)
        int probability = (int) (Math.random() * (100 - 1)) + 1;
        if (probability == 1) {
            for(Jogador j:jogadores){
                if(j.getId()==id){
                    String connectLocation = "rmi://" + j.getIp() + ":52369/Callback";
                    JogadorInterface client = null;
                    try {
                        client = (JogadorInterface) Naming.lookup(connectLocation);
                        client.finaliza();
                        System.out.println("Jogador de id: "+id+" expulso");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        }
        return 1;
    }

    @Override
    public int encerra(int id) throws RemoteException {
        for(Jogador j: jogadores){
            if (j.getId() == id) {
                qtdJogadoresJogando--;
                System.out.println(id + " -> Encerrou");
                // remove da lista o jogador que encerrou
                j.iniciado = false;
            }
        }
        return 0;
    }
}