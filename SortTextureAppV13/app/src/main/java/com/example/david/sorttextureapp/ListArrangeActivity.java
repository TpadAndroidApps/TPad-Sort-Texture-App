package com.example.david.sorttextureapp;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;

import com.example.tpadlib.views.FrictionMapView;

import java.util.ArrayList;

public class ListArrangeActivity extends AppCompatActivity {
    DraggableGridView dgv;
    Button button4;
    ArrayList<String> myorder = new ArrayList<String>();
    int totalNumImages = 3; // total number of images

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_arrange);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        dgv = ((DraggableGridView)findViewById(R.id.vgv));
        button4 = ((Button)findViewById(R.id.button4));

        setListeners();

        int current_image_index;
        for (current_image_index = 0; current_image_index < totalNumImages; current_image_index++) {
            ImageView view = new ImageView(ListArrangeActivity.this);
            view.setImageBitmap(getThumb(current_image_index));
            myorder.add(String.valueOf(current_image_index+1));
            dgv.addView(view);
        }

    }
    private void setListeners()
    {
        dgv.setOnRearrangeListener(new DraggableGridView.OnRearrangeListener() {
            public void onRearrange(int oldIndex, int newIndex) {
                String word = myorder.remove(oldIndex);
                if (oldIndex < newIndex)
                    myorder.add(newIndex, word);
                else
                    myorder.add(newIndex, word);
            }
        });

        // click on picture
        dgv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                Intent intent = new Intent(ListArrangeActivity.this, PictureLoaderActivity.class);
                intent.putExtra("currentImage", arg2); // pass current image ImageFullScreenActivity
                intent.putStringArrayListExtra("passToFullActivity", myorder); // pass order to ImageFullScreenActivity
                //Log.d("order", String.valueOf(myorder));
                //Log.d("item click", String.valueOf(arg2));
                startActivity(intent);
            }
        });

        // button4
        button4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View arg0) {
                String finishedOrder = "";
                for (String s : myorder)
                    finishedOrder += s + " ";
                AlertDialog.Builder builder = new AlertDialog.Builder(ListArrangeActivity.this);
                builder.setTitle("Order from smooth to rough:");
                builder.setMessage(finishedOrder);
                builder.setPositiveButton("Submit", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(ListArrangeActivity.this, SendActivity.class);
                        // intent.putExtra("finishedOrder",arg2); // pass current image ImageFullScreenActivity
                        intent.putStringArrayListExtra("passToSendActivity", myorder);

                        startActivity(intent);
//                        AlertDialog.Builder builder1 = new AlertDialog.Builder(ListArrangeActivity.this);
//                        builder1.setTitle("Your order from smooth to rough is:");
//                        builder1.show();
                    }
                });
                builder.setNegativeButton("Cancel", null);
                builder.show();
            }
        });
    }

    private Bitmap getThumb(int imgIndex)
    {
        Bitmap bmp = Bitmap.createBitmap(700, 285, Bitmap.Config.RGB_565); // 150, 150

        String path = ("sdcard/TPadImageRes/image"+imgIndex+".jpg");
        Bitmap myImg = BitmapFactory.decodeFile(path);
        Canvas canvas = new Canvas(bmp);
        Paint paint = new Paint();

        canvas.drawRect(new Rect(0, 0, 700, 285), paint);
        canvas.drawBitmap(myImg, 0, 0, null);
        return bmp;
    }
}
