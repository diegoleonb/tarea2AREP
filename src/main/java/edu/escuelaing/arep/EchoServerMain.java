package edu.escuelaing.arep;

public class EchoServerMain {
    public static void main(String[] args) {
        EchoServer server = EchoServer.getInstance();
        try {
            server.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
