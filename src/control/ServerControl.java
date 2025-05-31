package control;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

import model.*;
import NET.ServerThread;

public class ServerControl {

    public static final int PORT = 1234;
    private ServerSocket serverSocket;
    private VehicleStore store;

    public ServerControl() throws IOException {
        store = new VehicleStore();
        serverSocket = new ServerSocket(PORT);
        startServer();
    }

    public void startServer() throws IOException {
        while (true) {
            Socket clientSocket = serverSocket.accept();
            ServerThread client = new ServerThread(clientSocket, store);
            client.start();
        }
    } 
}
