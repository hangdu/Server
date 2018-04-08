package com.example.hang.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    ServerSocket server;
    Socket client;
    BufferedReader in;
    PrintWriter out;
    String line;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        try {
            server = new ServerSocket(12345);
        } catch (IOException e) {
            System.out.println("Could not listen on port 12345");
            System.exit(-1);
        }

        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    client = server.accept();
                } catch (IOException e) {
                    System.out.println("Accept failed: 12345");
                    System.exit(-1);
                }

                try {
                    in = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    out = new PrintWriter(client.getOutputStream(), true);

                } catch (IOException e) {
                    System.out.println("Read failed");
                    System.exit(-1);
                }

                while (true) {
                    try {
                        line = in.readLine();
                        //Send data back to client
                        out.println("Hi, I got your message!");
                    } catch (IOException e) {
                        System.out.println("Read failed");
                        System.exit(-1);
                    }
                }
            }
        };
        new Thread(runnable).start();



    }
}


