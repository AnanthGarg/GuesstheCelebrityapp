package com.example.ananthgarg.guessthecelebrityapp;
import java.util.Random;
import java.util.regex.Pattern;
import java.util.regex.Matcher;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.ImageDecoder;
import android.media.Image;
import android.os.AsyncTask;
import android.os.CountDownTimer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;

public class MainActivity extends AppCompatActivity {
ArrayList<String> arr= new ArrayList<String>();
Button b1,b2,b3,b4;
ArrayList<String> img = new ArrayList<String>();
ImageView imageView;
    Bitmap myimage;
int answer ;
public void checkAnswer(View view)
{

     String res="";
if(view.getTag().toString().equals(Integer.toString(answer)))
{
    res="Correct!";
}
else
{
    res="Wrong!";
}

           Toast.makeText(this,res,Toast.LENGTH_SHORT).show();
b1.setEnabled(false);
b2.setEnabled(false);
b3.setEnabled(false);
b4.setEnabled(false);

  new CountDownTimer(4010,100)
{

    @Override
    public void onTick(long millisUntilFinished) {

    }

    @Override
    public void onFinish() {
        checkcel();
    }
}.start();

}
public class DownloadTask extends AsyncTask<String,Void,String>
{

    @Override
    protected String doInBackground(String... urls) {
        String result="";
        URL url;
        HttpURLConnection urlConnection = null;
        try {
            url = new URL(urls[0]);

            urlConnection  = (HttpURLConnection) url.openConnection();
            InputStream in  =  urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(in);
            int data = reader.read();
            while(data!=-1)
            {
                char current = (char) data;
                result+=current;
                data=reader.read();

            }
        } catch (Exception e) {
            e.printStackTrace();
            return "Failed";
        }
        return result;
    }
}
public void checkcel()

{


    DownloadImage Task = new DownloadImage();

    Random rand = new Random();
    int x=0, y=0, d=0, f=0;
    while (!(x!=y && y!=d && d!=f && f!=x && f!=y && d!=x))
    {
        x = rand.nextInt(arr.size());


        y = rand.nextInt(arr.size());


        d = rand.nextInt(arr.size());


        f = rand.nextInt(arr.size());
    }
    ArrayList<Integer> disp = new ArrayList<Integer>();
    disp.add(x);
    disp.add(y);
    disp.add(d);
    disp.add(f);
    int q = rand.nextInt(3);
    imageView = findViewById(R.id.imageView);
    try{

        myimage =  Task.execute(img.get(disp.get(q))).get();
        imageView.setImageBitmap(myimage);
    }

    catch(Exception e){
        e.printStackTrace();
    }
    b1 = findViewById(R.id.button1);
    b2   = findViewById(R.id.button2);
    b3 = findViewById(R.id.button3);
    b4 = findViewById(R.id.button4);
    Log.i("info",arr.get(disp.get(0)));
    Log.i("info",arr.get(disp.get(1)));
    Log.i("info",arr.get(disp.get(2)));
    Log.i("info",arr.get(disp.get(3)));
    b1.setText(arr.get(disp.get(0)));
    b2.setText(arr.get(disp.get(1)));
    b3.setText(arr.get(disp.get(2)));
    b4.setText(arr.get(disp.get(3)));
    answer = q;
    if(!(b1.isEnabled() && b2.isEnabled() && b3.isEnabled() && b4.isEnabled()) ) {
        b1.setEnabled(true);
        b2.setEnabled(true);
        b3.setEnabled(true);
        b4.setEnabled(true);
    }
}
public class DownloadImage extends AsyncTask<String,Void,Bitmap>
    {


        @Override
        protected Bitmap doInBackground(String... urls) {
            try{
            URL url= new URL(urls[0]);

                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                Bitmap mybitmap = BitmapFactory.decodeStream(in);
                return mybitmap;
            } catch (Exception e) {
                e.printStackTrace();
                return null;
            }

        }
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        DownloadTask task = new DownloadTask();

        String result = null;
        try {
            result = task.execute("http://www.posh24.se/kandisar").get();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String[] resultsplit = result.split("<div class=\"listedArticle\">");
        Pattern p = Pattern.compile("img src=\"(.*?)\"");
        Matcher m = p.matcher(resultsplit[0]);

        while (m.find()) {
            System.out.println(m.group(1));
            img.add(m.group(1));
        }
        p = Pattern.compile("alt=\"(.*?)\"");
        m = p.matcher(resultsplit[0]);
        while (m.find()) {
            System.out.println(m.group(1));
            arr.add(m.group(1));

        }
checkcel();

    }
}
