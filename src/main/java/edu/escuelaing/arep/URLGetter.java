package edu.escuelaing.arem;
import java.net.*;

public class URLGetter{
    public void main( String[] args ) throws Exception{
       try {
           URL myUrl = new URL("https://www.google.com/?hl=es");
           printUrl(myUrl);
       } catch (Exception e) {
           e.printStackTrace();
       }
    }

    public void printUrl(URL myUrl){
        System.out.println("Protocol: " + myUrl.getProtocol());
        System.out.println("Authority: " + myUrl.getAuthority());
        System.out.println("Host: " + myUrl.getHost());
        System.out.println("Port: " + myUrl.getPort());
        System.out.println("Path: " + myUrl.getPath());
        System.out.println("Query: " + myUrl.getQuery());
        System.out.println("File: " + myUrl.getFile());
        System.out.println("Ref: " + myUrl.getRef());
    }
}
