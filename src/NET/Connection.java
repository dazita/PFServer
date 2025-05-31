package NET;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class Connection {

    private Socket clientSocket;
    private DataOutputStream salida;
    private DataInputStream entrada;

    public Connection(Socket clientSocket){
        try {
            this.clientSocket = clientSocket;
            salida = new DataOutputStream(clientSocket.getOutputStream());
            entrada = new DataInputStream(clientSocket.getInputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void send(String message){
        try {
            salida.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void sendBigData(String message) {
        try {
            byte[] messageBytes = message.getBytes(StandardCharsets.UTF_8);
            // Enviar primero el tamaño del mensaje (4 bytes: int)
            salida.writeInt(messageBytes.length);
            // Enviar el mensaje completo
            salida.write(messageBytes);
            salida.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String receive(){
        String response = "";
        try {
            response =  entrada.readUTF();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return response;
    }

    public String receiveBigData() {
        try {
            int length = entrada.readInt();
            byte[] messageBytes = new byte[length];
            entrada.readFully(messageBytes);
            return new String(messageBytes, StandardCharsets.UTF_8);
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void close(){
        try {
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
