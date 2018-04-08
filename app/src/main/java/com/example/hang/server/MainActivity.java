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
                while (true) {
                    Socket socket;
                    try {
                        socket = server.accept();
                        new Thread(new ClientWorker(socket)).start();
                    } catch (IOException e) {
                        System.out.println("Accept failed: 12345");
                        System.exit(-1);
                    }
                }
            }
        };
        new Thread(runnable).start();
    }
}


