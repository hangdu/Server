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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_startCollect = (Button) findViewById(R.id.btn_startCollect);

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
            }
        };
        Thread connectThread = new Thread(runnable);
        connectThread.start();


        btn_startCollect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String buttonText = btn_startCollect.getText().toString();
                if (buttonText.equals("Start to collect")) {
                    worker.addCommand(1);
                    btn_startCollect.setText("Stop");
                } else {
                    worker.addCommand(0);
                    btn_startCollect.setText("Start to collect");
                }
            }
        });
    }
}


