package com.example.hang.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;
import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.TimeUnit;

/**
 * Created by hang on 4/9/18.
 */

public class Worker {
    DataInputStream in;
    DataOutputStream out;
    Socket client;
    BlockingDeque<Integer> queue;
    public Worker(Socket client) {
        this.client = client;
        queue = new LinkedBlockingDeque<>();
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(-1);
        }

        new Thread(writeTask).start();
    }

    Runnable writeTask = new Runnable() {
        @Override
        public void run() {
            while (true) {
                Integer command = null;
                try {
                    command = queue.poll(300, TimeUnit.MILLISECONDS);
                    if (command != null) {
                        System.out.println("command="+command);
                        out.writeUTF(String.valueOf(command));
                        out.flush();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    };


    public void addCommand(int command) {
        queue.add(command);
    }

}
