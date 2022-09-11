package edu.escuelaing.arep;

import java.net.*;
import java.util.Scanner;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.io.*;

public class EchoServer {
    private static boolean esPath = true;
    private static EchoServer instance = new EchoServer();

    public static EchoServer getInstance() {
        return instance;
    }

    public void start() throws IOException {
        ServerSocket serverSocket = iniciarServidor();
        String flag = "";
        ExecutorService pool = Executors.newFixedThreadPool(7);
        while (flag != "exit") {
            Socket clientSocket = activarSocket(serverSocket);
            RequestProcessor processor = new RequestProcessor(clientSocket);
            pool.execute(processor);

        }
        serverSocket.close();

    }

    /**
     * Se inicializa el ServerSocket
     * @return
     */
    private ServerSocket iniciarServidor() {
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(getPort());
        } catch (IOException e) {
            System.err.println("Could not listen on port: 35000.");
            System.exit(1);
        }
        return serverSocket;
    }

    /**
     * Se activa el Socket
     * @param socket
     * @return
     */
    private Socket activarSocket(ServerSocket socket) {
        Socket clientSocket = null;
        try {
            System.out.println("Activado");
            clientSocket = socket.accept();
        } catch (IOException e) {
            System.err.println("Accept failed.");
            System.exit(1);
        }
        return clientSocket;
    }

    /**
     * Devuelve la respuesta en caso de que el archivo exista, 
     * de lo contrario manda error y lo muestra en pantalla
     * @param url
     * @param socket
     * @throws IOException
     */
    public static void respuesta(String url, Socket socket) throws IOException {
        String tipo = url.substring(url.indexOf(".") + 1);
        File file = new File(url);
        PrintWriter out;
        if (file.exists()) {
            tipoArchivo(tipo, socket, file);
        } else {
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(error(404));
            out.close();
        }

    }

    /**
     * Verifica que tipo de archivo ha ingresado para hacer la 
     * respectiva validacion y mirar a que metodo mandarlo
     * @param tipo
     * @param clientSocket
     * @param archivo
     * @throws IOException
     */
    private static void tipoArchivo(String tipo, Socket clientSocket, File archivo) throws IOException {

        if (tipo.equals("png") || tipo.equals("jpg") || tipo.equals("gif")
                || tipo.equals("jpeg")) {
            binario(tipo, clientSocket, archivo);
        } else if (tipo.equals("html") || tipo.equals("js")) {
            archivos(tipo, clientSocket, archivo);
        }
    }

    /**
     * Muestra en pantalla archivos de tipo binario
     * @param tipo
     * @param clientSocket
     * @param archivo
     * @return
     * @throws IOException
     */
    private static String binario(String tipo, Socket clientSocket, File archivo) throws IOException {
        String outputLine = "";
        byte[] imagen = leerImagen(archivo);
        DataOutputStream binario = new DataOutputStream(clientSocket.getOutputStream());

        outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: image/" + tipo + "\r\n"
                + "Content-Length: " + imagen.length + "\r\n"
                + "\r\n";
        binario.writeBytes(outputLine);
        binario.write(imagen);
        binario.close();
        binario.close();
        return outputLine;
    }

    /**
     * Muestra en pantalla el archivo
     * @param tipo
     * @param clientSocket
     * @param archivo
     * @return
     * @throws IOException
     */
    private static String archivos(String tipo, Socket clientSocket, File archivo) throws IOException {
        String outputLine = "";
        PrintWriter out;
        out = new PrintWriter(clientSocket.getOutputStream(), true);
        outputLine = "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/" + tipo + "\r\n"
                + "\r\n"
                + leerArchivo(archivo);
        out.println(outputLine);
        out.close();
        return outputLine;
    }

    /**
     * Lee el respectivo archivo
     * @param archivo
     * @return 
     * @throws FileNotFoundException
     */
    private static String leerArchivo(File archivo) throws FileNotFoundException {
        String lista = new String();
        try (Scanner scanner = new Scanner(archivo)) {
            while (scanner.hasNextLine()) {
                lista += (scanner.nextLine());
            }
            return lista;
        } catch (NumberFormatException e) {
            return e.toString();
        }
    }

    /**
     * Lee una imagen
     * @param archivo
     * @return
     * @throws IOException
     */
    private static byte[] leerImagen(File archivo) throws IOException {
        try {
            FileInputStream inputImage = new FileInputStream(archivo);
            byte[] bytes = new byte[(int) archivo.length()];
            inputImage.read(bytes);
            inputImage.close();
            return bytes;
        } catch (Exception e) {
            byte[] bytes = new byte[0];
            return bytes;
        }

    }

    /**
     * Retorna el path para ser enviado a Response
     * @param in
     * @return
     * @throws IOException
     */
    public static String request(BufferedReader in) throws IOException {
        String path = "";
        String inputLine = "";
        while ((inputLine = in.readLine()) != null) {
            esPath = true;
            if (esPath && inputLine.startsWith("GET")) {
                System.out.println("Path: " + inputLine.split(" ")[1].substring(1));
                path = inputLine.split(" ")[1].substring(1);
                esPath = false;
            }
            System.out.println("Received: " + inputLine);
            if (!in.ready()) {
                break;
            }
        }
        return path;
    }

    /**
     * Muestra en pantalla que no se ha encontrado el archivo
     * @param tipo
     * @return
     */
    private static String error(int tipo) {
        String respuesta = "";
        if (tipo == 404) {
            respuesta = "HTTP/1.1 404 Not Found\r\n\r\n"
                    + "<!DOCTYPE html>"
                    + "<html>"
                    + "<head>"
                    + "<title>Home</title>"
                    + "</head>"
                    + "<body style='background-color:black'>"
                    + "<h1 style='color:white'>File not found</h1>"
                    + "<img src='https://www.trecebits.com/wp-content/uploads/2020/11/Error-404.jpg'>"
                    + "</body>"
                    + "</html>";
        }
        return respuesta;
    }

    /**
     * Devuelve el puerto 35000 para ser usado por el servidor
     * @return
     */
    private int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt(System.getenv("PORT"));
        }
        return 35000;
    }

}
