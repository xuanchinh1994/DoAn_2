package com.example.chinh.doan_2;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import java.io.IOException;


public class MainActivity extends Activity implements ClientSocket.ServerListener{

    EditText edtIp;
    EditText edtSend;
    EditText edtPort;
    TextView txtReceive;
    Button test;


    Intent i;

    private boolean connected = false;
    private ClientSocket clientSocket;
    private Settings settings;
    public static final String IP_1="192.168.1.3";

    private final String TAG = getClass().getSimpleName();
    private ToggleButton toggleButton3;

    MediaPlayer mp3;


    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        edtIp = (EditText) findViewById(R.id.edtIp);//2
//        edtSend = (EditText) findViewById(R.id.edtSend);
        edtPort = (EditText) findViewById(R.id.edtPort);
//        txtReceive = (TextView) findViewById(R.id.txtReceive);
        test=(Button)findViewById(R.id.test);

        settings=new Settings(this);

        Log.d(TAG,"onCreate: ");
        test.setEnabled(false);

        mp3 = MediaPlayer.create(MainActivity.this, R.raw.feded);
        mp3.start();

        toggleButton3 = (ToggleButton)findViewById(R.id.toggleButton3);
        toggleButton3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(toggleButton3.isChecked()){


                        mp3.start();

                }
                else {
                        mp3.stop();
                    try {
                        mp3.prepare();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }
            }
        });





    }


    public void tab (View v){
        switch (v.getId()) {
            case R.id.test:
//                clientSocket.disconnect();
                 i = new Intent(this,JoyStickActivity.class);
                clientSocket.disconnect();
                startActivity(i);
                break;
            default:
                i = new Intent(this,MainActivity.class);
                startActivity(i);
        }

    }

    public void Start(View view){

        if (connected){
            connectStatusChange(true);


        }
        if (!connected){
            String ip = edtIp.getText().toString();
            int port = Integer.valueOf(edtPort.getText().toString());
            clientSocket = new ClientSocket(ip,port);
            clientSocket.setServerListener(MainActivity.this);

            clientSocket.connect();
            settings.putString(Settings.IP_ADDRESS, ip);
            settings.putInt(Settings.PORT, port);
            if (ip.equals(IP_1)) {
                Toast.makeText(this, "Connected", Toast.LENGTH_SHORT).show();
                test.setEnabled(true);
            }else {
                Toast.makeText(this, "Sai IP hoặc PORT", Toast.LENGTH_SHORT).show();
                test.setEnabled(false);
            }

        }
        else {
            clientSocket.disconnect();
            Toast.makeText(this, "Disconnected", Toast.LENGTH_SHORT).show();
            test.setEnabled(false);
        }

    }

    public void onCloseSocket(View view) {
        switch (view.getId()) {

            case R.id.btnClose:
                mp3.stop();
                finish();
            default:
                finish();
        }

    }



    @Override
    public void connectStatusChange(boolean status) {
        this.connected = status;
//        this..setText(status ? "DISCONNECT" : "CONNECT");
//        this.edIP.setEnabled(!status);
//        this.edPort.setEnabled(!status);
//        this.mSectionsPagerAdapter.notifyDataSetChanged();
    }

    public void newMessengeFromServer(String messenge) {
//        Toast.makeText(this, "receive: " + messenge, Toast.LENGTH_SHORT).show();
//
        txtReceive.setText(messenge);
//        Intent c = new Intent(this,JoyStickActivity.class);
//        Bundle bundle = new Bundle();
//        bundle.putString("msg",messenge);
//        c.putExtra("put",bundle);



    }




//    @Override
//    public void onClick(View v) {
//
//    }


    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG,"onStart: ");
    }

    public void onResume() {
        super.onResume();
        Log.d(TAG,"onResume: ");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.d(TAG,"onPause: ");

    }


    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d(TAG,"onRestart: ");
//        if(!connected){
//            clientSocket.connect();
//        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.d(TAG,"onStop: ");
        if(this.connected){
            clientSocket.disconnect();
        }

    }


}