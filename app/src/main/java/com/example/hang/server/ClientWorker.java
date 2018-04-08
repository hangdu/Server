package com.example.hang.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Created by hang on 4/8/18.
 */

public class ClientWorker implements Runnable {
    private Socket client;
    public ClientWorker(Socket client) {
        this.client = client;
    }

    public void run() {
        String line;
        BufferedReader in = null;
        PrintWriter out = null;
        try {
            in = new BufferedReader(new InputStreamReader(client.getInputStream()));
            out = new PrintWriter(client.getOutputStream(), true);
        } catch (IOException e) {
            System.out.println("In or out failed");
            System.exit(-1);
        }

        while (true) {
            try {
                line = in.readLine();
                out.println("Hello client");
            } catch (IOException e) {
                System.out.println("Read failed");
                System.exit(-1);
            }
        }
    }
}
