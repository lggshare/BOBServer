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
import java.util.Calendar;

public class ConnectedClient {

    private final Socket clientSocket;

    private final BOBServer server;

    private final int id;

    private InputStream inputStream;

    private OutputStream outputStream;

    /**
     * Close the connection, and do some cleanup
     */
    private void closeConnection() {
        server.removeConnectedClient(this);
        try {

            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static final String CID_CHARSET = "1234567890";
    private static final int CID_LENGTH = 16;
    private static final int BUFFER_LENGTH = 512;

    private JSONObject readClientResponse() throws IOException {
        byte[] buffer = new byte[BUFFER_LENGTH];
        inputStream.read(buffer);
        return new JSONObject(new String(buffer, Config.DEFAULT_CHARSET));
    }

    private void sendJsonMessage(JSONObject jsonObject) throws IOException {
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
            sendJsonMessage(jsonObject);

            JSONObject jsonResponse = readClientResponse();
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

    private void serve() {
        while (true) {
//            try {
//                Thread.sleep(200);
//                outputStream.write("Hello".getBytes());
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            } catch (IOException e) {
//                // Fail to write, close connection
//                e.printStackTrace();
//                closeConnection();
//                break;
//            }
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
