package com.example.hang.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class MainActivity extends AppCompatActivity {
    ServerSocket server;
    private static final int NTHREADS = 5;
    private static final Executor exec = Executors.newFixedThreadPool(NTHREADS);
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
                        Runnable task = new ClientWorker(socket);
                        exec.execute(task);
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


