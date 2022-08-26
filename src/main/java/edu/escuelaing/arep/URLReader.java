package edu.escuelaing.arem;

import java.io.*;
import java.net.*;

public class URLReader {

    public static void main(String[] args) throws Exception{
        URL url = new URL("https://www.google.com/?hl=es");
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(url.openStream()));
            String inputLine = null;
            while((inputLine = reader.readLine()) != null){
                System.out.println(inputLine);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
}
