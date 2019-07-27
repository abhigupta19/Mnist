package com.sar.user.mnist;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import org.tensorflow.contrib.android.TensorFlowInferenceInterface;

import java.io.BufferedOutputStream;
import java.util.Random;

public class MainActivity extends AppCompatActivity {
    static{
        //System.loadLibrary("tensorflow_inference");
        System.loadLibrary("tensorflow_inference");

    }
    ImageView imageView;
    private static final String Model_file="file:///android_asset/optimized_frozen_mnist_model.pb";
    private static final String input_node="x_input";
    private static final String output_node="y_actual";
    private static final int[] node={1,784};
    private  TensorFlowInferenceInterface tensorFlowInferenceInterface;
    private int a;
    private final int[] image=
    {
                R.drawable.digit0,
                R.drawable.digit1,
                R.drawable.digit2,
                R.drawable.digit3,
                R.drawable.digit4,
                R.drawable.digit5,
                R.drawable.digit6,
                R.drawable.digit7,
                R.drawable.digit8,
                R.drawable.digit9



    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      imageView=findViewById(R.id.imageView);
        final TextView textView=findViewById(R.id.textView);


        tensorFlowInferenceInterface=new TensorFlowInferenceInterface();
        tensorFlowInferenceInterface.initializeTensorFlow(getAssets(),Model_file);
        Button button=findViewById(R.id.button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                float[] converted=convert();
                float[] result=frompredication(converted);
                float max=0;
                int abc=0;
                for(int i=0;i<10;i++)
                {
                    if(result[i]>max)
                    {
                        max=result[i];
                        abc=i;
                    }
                    else {
                        continue;
                    }
                }
                textView.setText("model prefdict--"+String.valueOf(abc));

            }
        });
        Button button1=findViewById(R.id.button2);
        button1.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("ResourceType")
            @Override
            public void onClick(View v) {
             Random r=new Random();
             a=r.nextInt(10);
             imageView.setImageDrawable(getDrawable(image[a]));
            }
        });
    }
    private float[] frompredication(float[] pixelBuffer)
    {
        tensorFlowInferenceInterface.fillNodeFloat(input_node,node,pixelBuffer);
        tensorFlowInferenceInterface.runInference(new String[]{output_node});
        float[] reults={0,0,0,0,0,0,0,0,0,0,0};
        tensorFlowInferenceInterface.readNodeFloat(output_node,reults);
        return reults;

    }
    private float[] convert(){
        Bitmap bitmap=BitmapFactory.decodeResource(getResources(),image[a]);
        bitmap=Bitmap.createScaledBitmap(bitmap,28,28,true);
         imageView.setImageBitmap(bitmap);
         int [] imageintArray=new int[784];
        float [] imagefloatArray=new float[784];
        bitmap.getPixels(imageintArray,0,28,0,0,28,28);
        for(int i=0;i<784;i++)
        {
            imagefloatArray[i]= Float.valueOf(imageintArray[i]/-16777216);
        }

        return imagefloatArray;

    }
}
