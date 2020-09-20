package com.pucrs;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;

public class Client extends UnicastRemoteObject implements JogadorInterface {
    public Client() throws RemoteException {
    }

    public static void main(String[] args) {
        System.out.println("Client started");
    }

    @Override
    public void inicia() throws RemoteException {

    }

    @Override
    public void finaliza() throws RemoteException {

    }

    @Override
    public void cutuca() throws RemoteException {

    }
}
