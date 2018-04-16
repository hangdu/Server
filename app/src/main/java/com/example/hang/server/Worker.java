package com.example.hang.server;

import android.os.Handler;
import android.os.Message;

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
    Handler handler;
    public Worker(Socket client, Handler myHandler) {
        this.client = client;
        handler = myHandler;
        queue = new LinkedBlockingDeque<>();
        try {
            in = new DataInputStream(client.getInputStream());
            out = new DataOutputStream(client.getOutputStream());

        } catch (IOException e) {
            System.out.println("Read failed");
            System.exit(-1);
        }

        new Thread(writeTask).start();
        new Thread(readTask).start();
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

    Runnable readTask = new Runnable() {
        @Override
        public void run() {
            while (true) {
                try {
                    String message = in.readUTF();
                    System.out.println("From Client : " + message);
                    Message msg = Message.obtain();
                    msg.what = 0;
                    msg.obj = message;
                    handler.sendMessage(msg);

                } catch (IOException e) {
                    System.out.println("Read failed");
                    System.exit(-1);
                }
            }

        }
    };


    public void addCommand(int command) {
        queue.add(command);
    }

}
