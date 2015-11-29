package com.sveder.cardboardpassthrough;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.protocol.ClientContext;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class CameraService extends Service {

    private static final int BUFFER_SIZE = 30000;
    public static final String NEW_EULER_ANGLES = "CameraService.NEW_EULER_ANGLES";
    private static String TAG = "CameraService";
    private static String uri = "http://192.168.1.107:5000"; //"http://143.107.235.44:5000";
    private static String camstream = "/camstream/";
    private static String camposition = "/camposition/";
    private static String set_zero = "/camposition/set_zero/";
    private CameraBinder binder = new CameraBinder();
    private boolean binded = false;

    private CameraListener listener;
    private AngleListerner angleListerner;
    private HttpContext httpContext;

    public void setListener(CameraListener listener) {
        this.listener = listener;
        Log.d(TAG, "Listener set: " + this.listener);
    }

    @Override
    public IBinder onBind(Intent intent) {
        Log.d(TAG, "onBind");
        binded = true;

        Thread t = new Thread() {
            @Override
            public void run() {
                Log.d(TAG, "Starting image reader thread.");
                MjpegInputStream mjpegStream = null;
                char img[] = new char[BUFFER_SIZE];
                int bytesRead = 0;

                try {
                    URL url = new URL(uri + camstream);
                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    mjpegStream = new MjpegInputStream(urlConnection.getInputStream());
                } catch (IOException e) {
                    Log.e(TAG, "When connecting to server: " + e.getMessage());
                }

                Log.d(TAG, "Entering reading loop.");
                while (binded) {
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

        httpContext = new BasicHttpContext();
        httpContext.setAttribute(ClientContext.COOKIE_STORE, new BasicCookieStore());

        new AngleListerner.SetZero(httpContext).execute();

        angleListerner = new AngleListerner(httpContext);
        IntentFilter filter = new IntentFilter(NEW_EULER_ANGLES);
        registerReceiver(angleListerner, filter);

        return binder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        binded = false;
        unregisterReceiver(angleListerner);
        angleListerner = null;
        return super.onUnbind(intent);
    }

    public interface CameraListener {
        void onImage(Bitmap bmp);
    }

    public class CameraBinder extends Binder {

        public CameraService getCameraService() {
            return CameraService.this;
        }

    }

    protected static class AngleListerner extends BroadcastReceiver {

        private static int MOVEMENT_THRESHOLD = 3;
        private HttpContext httpContext;
        private int lastYaw = 1024;
        private int lastPitch = 1024;
        private int originYaw = 0;
        private int originPitch = 0;

        public AngleListerner(HttpContext httpCtxt) {
            httpContext = httpCtxt;
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getAction() == NEW_EULER_ANGLES) {
                Double pitch = 57.29 * intent.getDoubleExtra("pitch", 0.0f);
                Double yaw = 57.29 * intent.getDoubleExtra("yaw", 0.0f);

                if(lastPitch == 1024) {
                    lastPitch = pitch.intValue();
                    lastYaw = yaw.intValue();
                    originPitch = lastPitch;
                    originYaw = lastYaw;
                    return;
                } else if(Math.abs(pitch.intValue() - lastPitch) > MOVEMENT_THRESHOLD ||
                          Math.abs(yaw.intValue() - lastYaw) > MOVEMENT_THRESHOLD) {
                    int pitchActuation = pitch.intValue() - originPitch;
                    int yawActuation = yaw.intValue() - originYaw;

                    if(pitchActuation >= 80 || pitchActuation <= -15) {
                        return;
                    }

                    if(yawActuation >= 100 || yawActuation <= -100) {
                        return;
                    }

                    lastPitch = pitch.intValue();
                    lastYaw = yaw.intValue();

                    angleSender = new AngleSender(httpContext);
                    Log.d("EulerAngles", "pitch: " + pitchActuation + " yaw: " + yawActuation);
                    angleSender.execute(pitchActuation, yawActuation);
                }
            }
        }

        AsyncTask<Integer, Void, Void> angleSender;

        protected static class AngleSender extends AsyncTask<Integer, Void, Void> {
            private HttpContext httpContext;

            public AngleSender(HttpContext httpCtxt) {
                httpContext = httpCtxt;
            }

            @Override
            protected Void doInBackground(Integer... params) {
                int pitch = params[0];
                int yaw = params[1];

                try {
                    HttpClient client = new DefaultHttpClient();
                    String url = Uri.parse(uri + camposition).buildUpon()
                            .appendQueryParameter("pitch", Integer.toString(pitch))
                            .appendQueryParameter("yaw", Integer.toString(yaw))
                            .build().toString();
                    HttpGet request = new HttpGet(url);
                    HttpResponse response = client.execute(request);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }

        protected static class SetZero extends AsyncTask<Void, Void, Void> {

            private HttpContext httpContext;

            public SetZero(HttpContext httpCtxt) {
                httpContext = httpCtxt;
            }

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    HttpClient client = new DefaultHttpClient();
                    String url = Uri.parse(uri + set_zero).toString();
                    HttpGet request = new HttpGet(url);
                    HttpResponse response = client.execute(request, httpContext);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                return null;
            }
        }
    }

}
