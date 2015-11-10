package com.sveder.cardboardpassthrough;

import android.app.Service;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.ByteArrayBuffer;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.nio.CharBuffer;

public class CameraService extends Service {

    private static final int BUFFER_SIZE = 30000;
    private static String TAG = "CameraService";
    private static String uri = "http://143.107.235.44:5000/camstream/";
    private static CameraBinder binder;

    private CameraListener listener;


    public void setListener(CameraListener listener) {
        this.listener = listener;
    }

    @Override
    public IBinder onBind(Intent intent) {
        Thread t = new Thread() {
            @Override
            public void run() {
                MjpegInputStream mjpegStream = null;
                char img[] = new char[BUFFER_SIZE];
                int bytesRead = 0;

                try {
                    URL url = new URL(uri);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    mjpegStream = new MjpegInputStream(urlConnection.getInputStream());
                } catch (IOException e) {
                    Log.e(TAG, "When connecting to server: " + e.getMessage());
                }

                while (true) {
                    try {
                        Bitmap bmp = mjpegStream.readMjpegFrame();

                        if (listener != null) {
                            listener.onImage(bmp);
                        }
                    } catch (IOException e) {
                        Log.e(TAG, "While reading mjpeg frame: " + e.getMessage());
                    }
                }
            }
        };
        t.start();

        return binder;
    }

    public interface CameraListener {
        void onImage(Bitmap bmp);
    }

    public class CameraBinder extends Binder {

        public CameraService getCameraService() {
            return CameraService.this;
        }

    }

}
