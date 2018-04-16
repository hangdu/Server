package com.example.hang.server;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

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
    TextView tv_status;
    Handler myHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            // Gets the image task from the incoming Message object.
            if (inputMessage.what == 0) {
                String str = inputMessage.obj.toString();
                tv_status.setText(str);
            } else {
                tv_status.setText("");
            }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btn_startCollect = (Button) findViewById(R.id.btn_startCollect);
        tv_status = (TextView) findViewById(R.id.tv_status);

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
                    worker = new Worker(client, myHandler);
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
                    myHandler.sendEmptyMessage(1);
                }
            }
        });
    }
}


