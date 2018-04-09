package com.example.hang.server;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class MainActivity extends AppCompatActivity {
    ServerSocket server;
    Socket client;
    Worker worker;
    Button btn_startCollect;
    Button btn_stop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_startCollect = (Button) findViewById(R.id.btn_startCollect);
        btn_stop = (Button) findViewById(R.id.btn_stop);

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
                    System.out.println("New client");
                    worker = new Worker(client);
                } catch (IOException e) {
                    System.out.println("Accept failed: 12345");
                    System.exit(-1);
                }
//                while (true) {
//                    try {
//                        String messageFromClient = in.readUTF();
//                        System.out.println("I got message " + messageFromClient);
//                    } catch (IOException e) {
//                        System.out.println("Read failed");
//                        System.exit(-1);
//                    }
//                }
            }
        };
        Thread connectThread = new Thread(runnable);
        connectThread.start();


        btn_startCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worker.addCommand(1);
            }
        });
        btn_stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                worker.addCommand(0);
            }
        });
    }
}


