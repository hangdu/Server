package com.example.hang.server;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
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
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements LabelDialog.LabelDialogListener {
    ServerSocket server;
    Socket client;
    Worker worker;
    Button btn_startCollect;
    TextView tv_status;
    private ListView listView;
    String label = null;
    List<Integer> strengthList;
    private PositionAdapter positionAdapter;
    private List<Position> list;

    Handler myHandler = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message inputMessage) {
            // Gets the image task from the incoming Message object.
            if (inputMessage.what == 0) {
                String str = inputMessage.obj.toString();
                tv_status.setText("RSSI = " + str);
                strengthList.add(Integer.valueOf(str));
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
        listView = (ListView) findViewById(R.id.lv_label);
        strengthList = new ArrayList<>();
        list = new ArrayList<>();
        positionAdapter = new PositionAdapter(this, list);
        listView.setAdapter(positionAdapter);

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
                    openDialog();
                } else {
                    worker.addCommand(0);
                    btn_startCollect.setText("Start to collect");

                    //process strengthList
                    double ave = getAvearge();
                    tv_status.setText("Label="+label+";     aveRSSI="+ave);
                    System.out.println("Label="+label+";aveRSSI="+ave);
                    positionAdapter.add(new Position(label, ave));
                }
            }
        });
    }

    public void openDialog() {
        LabelDialog labelDialog = new LabelDialog();
        labelDialog.show(getSupportFragmentManager(), "label dialog");
    }


    @Override
    public void applyText(String label) {
        this.label = label;
        tv_status.setText("label is " + label);
        strengthList.clear();
        worker.addCommand(1);
        btn_startCollect.setText("Stop");
    }


    private double getAvearge() {
        double res = 0;
        for (int val : strengthList) {
            res += val;
        }
        if (strengthList.size() == 0) {
            return 0;
        } else {
            return res/strengthList.size();
        }
    }
}
