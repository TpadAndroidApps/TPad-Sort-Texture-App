package com.example.david.sorttextureapp;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;

public class IntroActivity extends AppCompatActivity {

    // load images from TPad github
    String[] image_url_array = {"https://github.com/TpadAndroidApps/TPad-Sort-Texture-App/blob/master/texture%20image/image1.jpg?raw=true",
            "https://github.com/TpadAndroidApps/TPad-Sort-Texture-App/blob/master/texture%20image/image2.jpg?raw=true",
            "https://github.com/TpadAndroidApps/TPad-Sort-Texture-App/blob/master/texture%20image/image3.jpg?raw=true"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        DownloadTask mDownloadTask = new DownloadTask();
        mDownloadTask.execute(image_url_array);
    }

    // start button
    public void StartTask(View view) {
        Intent intent = new Intent(this, ListArrangeActivity.class);
        startActivity(intent);
    }

    class DownloadTask extends AsyncTask<String, Integer, String> {

        ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            mProgressDialog = new ProgressDialog(IntroActivity.this);
            mProgressDialog.setTitle("Updating Images Library");
            mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            mProgressDialog.setMax(100);
            mProgressDialog.setProgress(0);
            mProgressDialog.show();

        }

        @Override
        protected String doInBackground(String... urls) {

            int file_length = 0;
            try {
                for(int i = 0; i < urls.length; i++) {
                    URL url = new URL(urls[i]);
                    URLConnection urlConnection = url.openConnection();
                    urlConnection.connect();
                    file_length = urlConnection.getContentLength();

                    File new_folder = new File("sdcard/TPadImageRes");
                    if (!new_folder.exists()) {
                        new_folder.mkdir();
                    }
                    File input_file = new File(new_folder, "image" + i + ".jpg");

                    InputStream inputStream = new BufferedInputStream(url.openStream(), 8192);

                    byte[] data = new byte[1024];
                    int total = 0;
                    int count = 0;

                    OutputStream outputStream = new FileOutputStream(input_file);

                    while ((count = inputStream.read(data)) != -1) {
                        total += count;
                        outputStream.write(data, 0, count);

                        int progress = total * 100 / file_length;
                        publishProgress(progress);

                    }

                    inputStream.close();
                    outputStream.close();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return "Update Successful!";
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            mProgressDialog.setProgress(values[0]);
            super.onProgressUpdate(values);
        }

        @Override
        protected void onPostExecute(String  result) {
            mProgressDialog.hide();
            Toast.makeText(getApplicationContext(),result,Toast.LENGTH_LONG).show();
        }
    }
}
