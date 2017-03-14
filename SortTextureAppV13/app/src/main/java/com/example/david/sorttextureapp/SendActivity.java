package com.example.david.sorttextureapp;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.content.Intent;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;


public class SendActivity extends AppCompatActivity {

    TextView textResponse;
    EditText editTextAddress, editTextPort, editTextText;
    Button buttonTryagain, buttonExit;
    ArrayList<String> receivedFinishedOrder ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send);



        //editTextAddress = (EditText) findViewById(R.id.address);
        //editTextPort = (EditText) findViewById(R.id.port);
        editTextText = (EditText) findViewById(R.id.text);
        buttonTryagain = (Button) findViewById(R.id.Try_Again);
        buttonExit = (Button) findViewById(R.id.Exit);
        textResponse = (TextView) findViewById(R.id.response);

        buttonTryagain.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                startActivity(new Intent(SendActivity.this, ListArrangeActivity.class));
            }

        });

        buttonExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                finish();
                moveTaskToBack(true);
               // System.exit(0);
            }

        });


        // Get finished order of textures from acticity_list_arrange

        ArrayList<String> data = getIntent().getStringArrayListExtra("passToSendActivity");
        receivedFinishedOrder = data;
        MyClientTask myClientTask = new MyClientTask();
        myClientTask.execute();

    }





    public class MyClientTask extends AsyncTask<Void, Void, Void> {

        String dstAddress = "10.105.129.248";  // may need to change
        ArrayList<String> dstText=receivedFinishedOrder;
        int dstPort = 8080;
        String response = "";
        @Override
        protected Void doInBackground(Void... arg0) {

            Socket socket = null;
            OutputStream os=null;

            try {
                socket = new Socket(dstAddress, dstPort);
                os=socket.getOutputStream();
                PrintStream osprintStream = new PrintStream(os);
                osprintStream.print(dstText);
                osprintStream.close();
                response = "Log Successfully";
                ByteArrayOutputStream byteArrayOutputStream =
                        new ByteArrayOutputStream(1024);
                byte[] buffer = new byte[1024];
                int bytesRead;
                InputStream inputStream = socket.getInputStream();


                /*
                 * notice:
                 * inputStream.read() will block if no data return
                 */
//                if ((bytesRead = inputStream.read(buffer)) != -1){
//                    byteArrayOutputStream.write(buffer, 0, bytesRead);
////                    response += byteArrayOutputStream.toString("UTF-8");
//                }
//                response = "Log Successfully";
//                os=socket.getOutputStream();
//                PrintStream osprintStream = new PrintStream(os);
//                osprintStream.print(dstText);
//                osprintStream.close();

            } catch (UnknownHostException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
                response = "UnknownHostException: " + e.toString();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
//                response = "IOException: " + e.toString();
                //response = "Failed";
            }finally{
                if(socket != null){
                    try {
                        socket.close();
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
//            textResponse.setText("Hello");
            textResponse.setText(response);
            super.onPostExecute(result);
        }
    }
}
