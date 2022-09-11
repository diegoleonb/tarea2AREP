package edu.escuelaing.arep;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EchoClient {
    static int hilos = 0;

    public static void main(String[] args) throws IOException {
        int hilos = Integer.parseInt(args[0]);
        ExecutorService pool = Executors.newFixedThreadPool(7);
        multiRequest(hilos, pool);
    }

    private static int multiRequest(int hilo, ExecutorService pool) {
        hilos--;
        hilo = hilos;
        MultiHilosProcessor processor = new MultiHilosProcessor(getPort());
        pool.execute(processor);
        if (hilos > 0) {
            multiRequest(hilo, pool);
        }
        pool.shutdown();
        return hilo;
    }

    private static int getPort() {
        if (System.getenv("PORT") != null) {
            return Integer.parseInt((System.getenv("PORT")));
        }
        return 35000;
    }

}
