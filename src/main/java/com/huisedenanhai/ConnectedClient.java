/*
 * Created by huisedenanhai on 2018/06/04
 */

package com.huisedenanhai;

import com.huisedenanhai.exception.TooManyConnectionException;
import com.huisedenanhai.math.Random;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Calendar;

public class ConnectedClient {

    private final Socket clientSocket;

    private final BOBServer server;

    private final int id;

    private InputStream inputStream;

    private OutputStream outputStream;

    private final ArrayList<JSONSerializable> actions = new ArrayList<>();


    /**
     * Send all actions to server's action pool, and clear all actions in the list
     */
    private void pushAllActionsToPool() {
        synchronized (this.actions) {
            server.pushAllActionsToPool(actions);
            actions.clear();
        }
    }

    /**
     * Close the connection, and do some cleanup
     */
    private void closeConnection() {
        pushAllActionsToPool();
        server.removeConnectedClient(this);
        try {
            clientSocket.close();
            inputStream.close();
            outputStream.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String CID_CHARSET = "1234567890";
    private static final int CID_LENGTH = 16;
    private static final int BUFFER_LENGTH = 512;

    /**
     * Read request from client
     *
     * @return the decoded JSONObject
     * @throws IOException
     */
    private JSONObject readClientRequest() throws IOException {
        byte[] buffer = new byte[BUFFER_LENGTH];
        inputStream.read(buffer);
        return new JSONObject(new String(buffer, Config.DEFAULT_CHARSET));
    }

    /**
     * Send json to client
     *
     * @param jsonObject the json object to sent
     * @throws IOException
     */
    private void sendJsonResponse(JSONObject jsonObject) throws IOException {
        outputStream.write((jsonObject.toString() + "\r\n").getBytes(Config.DEFAULT_CHARSET));
    }

    /**
     * Check if it is a valid connection
     * The connected client should follow the our predefined protocol
     *
     * @return true if check succeed, else return false
     */
    private boolean checkConnection() {
        try {
            JSONObject jsonObject = new JSONObject();
            Long cid = Long.parseUnsignedLong(Random.randomString(CID_CHARSET, CID_LENGTH));
            jsonObject.put("method", "checking");
            jsonObject.put("time", Calendar.getInstance().getTimeInMillis());
            jsonObject.put("id", id);
            jsonObject.put("cid", cid);
            sendJsonResponse(jsonObject);

            JSONObject jsonResponse = readClientRequest();
            System.out.println(jsonResponse);
            // check consistency
            if (!"checking-response".equals(jsonResponse.getString("method"))) {
                return false;
            }
            if (!cid.equals(jsonResponse.get("cid"))) {
                return false;
            }
            if (id != jsonResponse.getInt("id")) {
                return false;
            }
        } catch (UnsupportedEncodingException ex) {
            ex.printStackTrace();
            return false;
        } catch (IOException | JSONException ex) {
            return false;
        }
        return true;
    }

    /**
     * Append action to list
     * This method is thread safe
     *
     * @param action the action to be appended
     */
    private void appendActionToList(JSONSerializable action) {
        synchronized (this.actions) {
            actions.add(action);
        }
    }

    private void serve() {
        while (true) {
            try {
                JSONObject request = readClientRequest();
                System.out.println(request);
                String method = request.getString("method");
                if ("action".equals(method)) {
                    Action action = new Action();
                    action.parseJSON(request);
                    appendActionToList(action);
                } else if ("disconnect".equals(method)) {
                    closeConnection();
                    break;
                }
            } catch (IOException | JSONException ex) {
                ex.printStackTrace();
                closeConnection();
                break;
            }
        }
    }

    public InetAddress getInetAddress() {
        return clientSocket.getInetAddress();
    }

    /**
     * The thread that do the main work of serving
     */
    class WorkingThread extends Thread {
        @Override
        public void run() {
            super.run();
            // check if this is a valid connection
            if (!checkConnection()) {
                closeConnection();
                return;
            }
            // Check connection succeed, tell the server to add this client
            try {
                server.addConnectedClient(ConnectedClient.this);
            } catch (TooManyConnectionException ex) {
                // Too many connection, return
                closeConnection();
                return;
            }
            serve();
        }
    }


    public ConnectedClient(int id, Socket clientSocket, BOBServer server) {
        this.clientSocket = clientSocket;
        this.server = server;
        this.id = id;
        // Open input and output stream
        try {
            this.inputStream = clientSocket.getInputStream();
            this.outputStream = clientSocket.getOutputStream();
        } catch (IOException ex) {
            ex.printStackTrace();
            return;
        }
        // if any of the above operation fails, the method will return
        // Create a new thread and serve the client
        new WorkingThread().start();
    }
}
