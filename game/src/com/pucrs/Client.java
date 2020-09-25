package com.pucrs;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

public class Client extends UnicastRemoteObject implements JogadorInterface {
    private static int id;
    private static JogoInterface server = null;
    private static boolean jogando = false;
    private static int qtdJogadas = 5;
    private static JogoInterface jogo = null;
    static Timer timer = new Timer();

    public Client() throws RemoteException {
    }

    static class Task extends TimerTask {
        @Override
        public void run() {
            int initial = 500;
            int end = 1500;
            int delay = new Random().nextInt(end-initial) + initial;
            if (qtdJogadas > 0 && jogando) timer.schedule(new Task(), delay);
            qtdJogadas--;
            try {
                jogo.joga(id);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
            System.out.println("Ainda posso jogar: " + qtdJogadas);
        }

    }

    public static void main(String[] args) {
        System.out.println("Client has started");

        if (args.length != 2) {
            System.out.println("Usage: java Client <server ip> <client ip>");
            System.exit(1);
        }

        try {
            System.setProperty("java.rmi.server.hostname", args[1]);
            LocateRegistry.createRegistry(52369);
            System.out.println("java RMI registry created.");
        } catch (RemoteException e) {
            System.out.println("java RMI registry already exists.");
        }

        // Para receber retorno do server [URl]
        try {
            String client = "rmi://" + args[1] + ":52369/Callback";
            Naming.rebind(client, new Client());
            System.out.println("Client is ready.");
        } catch (Exception e) {
            System.out.println("Client failed: " + e);
        }

        // Registrando no servidor
        String remoteHostName = args[0];
        String connectLocation = "rmi://" + remoteHostName + ":52369/Connection";

        try {
            System.out.println("Connecting to server at : " + connectLocation);
            jogo = (JogoInterface) Naming.lookup(connectLocation);
        } catch (Exception e) {
            System.out.println ("Client connection failed: ");
            e.printStackTrace();
        }

        try {
            id = jogo.registra();
            if (id != -1) {
                System.out.println("ID registrado: " + id);
            } else {
                System.out.println("Jogo já começou");
            }

        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public static void joga() throws RemoteException{
        while (qtdJogadas > 0 && jogando) {
            new Task().run();
        }
        jogo.encerra(id);
        System.out.println("Cliente encerrado.");
        jogando = false;
    }

    @Override
    public void inicia() throws RemoteException {
        jogando = true;
        System.out.println("Jogador iniciado.");
        joga();
    }

    @Override
    public void finaliza() throws RemoteException {
        jogando = false;
        System.out.println("Jogador finalizado.");
    }

    @Override
    public void cutuca() throws RemoteException {
        System.out.println("Jogador cutucado.");
    }
}
