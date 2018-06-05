/*
 * Created by huisedenanhai on 2018/06/04
 */

package com.huisedenanhai;

import com.huisedenanhai.exception.BadServerSocketException;
import com.huisedenanhai.exception.TooManyConnectionException;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collection;

public class BOBServer {

    private ServerSocket serverSocket;

    private int nextId = 0;

    private final ArrayList<ConnectedClient> connectedClients = new ArrayList<>();

    private final ArrayList<JSONSerializable> actionPool = new ArrayList<>();

    /**
     * Push all actions into the action pool
     * Action pool stores all the actions that will be send to the model for calculation
     * It should be noticed that this method will do nothing on the model
     *
     * @param actions actions to be added
     */
    public void pushAllActionsToPool(Collection<JSONSerializable> actions) {
        synchronized (this.actionPool) {
            actionPool.addAll(actions);
            System.out.println("Current action count: " + actionPool.size());
        }
    }

    /**
     * Add a connected client
     *
     * @param client the connected client
     * @throws TooManyConnectionException too many clients what to connect to the server
     */
    public void addConnectedClient(ConnectedClient client) throws TooManyConnectionException {
        synchronized (this.connectedClients) {
            if (connectedClients.size() >= Config.MAX_CONNECTION) {
                throw new TooManyConnectionException("Too many connects", null);
            }
            connectedClients.add(client);
            System.out.println("Get connection from: " + client.getInetAddress());
            System.out.println("current connection count: " + connectedClients.size());
        }
    }

    /**
     * Remove the client from connected clients list
     *
     * @param client the connected client
     */
    public void removeConnectedClient(ConnectedClient client) {
        synchronized (this.connectedClients) {
            connectedClients.remove(client);
            System.out.println("Remove connection of: " + client.getInetAddress());
            System.out.println("current connection count: " + connectedClients.size());
        }
    }

    public int getConnectionCount() {
        synchronized (this.connectedClients) {
            return connectedClients.size();
        }
    }

    private int generateId() {
        return nextId++;
    }

    /**
     * Start the server
     * Waiting for connections from clients
     */
    public void start() {
        while (true) {
            try {
                Socket clientSocket = serverSocket.accept();
                if (getConnectionCount() < Config.MAX_CONNECTION) {
                    new ConnectedClient(generateId(), clientSocket, this);
                }
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
    }

    public BOBServer() {
        try {
            serverSocket = new ServerSocket(Config.SERVER_PORT);
        } catch (IOException ex) {
            throw new BadServerSocketException("Can't open server socket.", ex);
        }
    }

    public static void main(String[] args) {
        BOBServer server = new BOBServer();
        server.start();
    }
}
