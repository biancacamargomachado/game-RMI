package com.pucrs;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.util.*;

public class Server extends UnicastRemoteObject implements JogoInterface {
    public Server() throws RemoteException {
    }

    public static void main(String[] args) throws RemoteException {
        System.out.println("Server started");
    }

    @Override
    public int registra() throws RemoteException {
        return 0;
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