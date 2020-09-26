package com.pucrs;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.*;

public interface JogoInterface extends Remote {
    public int registra(int port) throws RemoteException;
    public int joga(int id) throws RemoteException;
    public int encerra(int id) throws RemoteException;
}
