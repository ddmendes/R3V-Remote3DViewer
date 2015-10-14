package br.usp.eesc.sel630.r3v.views;

import android.app.ActionBar;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.vrtoolkit.cardboard.CardboardActivity;
import com.google.vrtoolkit.cardboard.CardboardView;
import com.google.vrtoolkit.cardboard.Eye;
import com.google.vrtoolkit.cardboard.HeadTransform;
import com.google.vrtoolkit.cardboard.ScreenParams;
import com.google.vrtoolkit.cardboard.Viewport;

import javax.microedition.khronos.egl.EGLConfig;

import br.usp.eesc.sel630.r3v.R;

public class MainActivity extends CardboardActivity {

    private float [] direction;
    private CardboardView.Renderer renderer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        renderer = new R3VRenderer();
        CardboardView cardboardView = (CardboardView) findViewById(R.id.cardboard_view);
        cardboardView.setRenderer(renderer);

        // Manually set the screen size
        Display display = getWindowManager().getDefaultDisplay();
        ScreenParams params = new ScreenParams(display);
        params.setWidth(1920);
        params.setHeight(1080);
        cardboardView.updateScreenParams(params);
    }

    @Override
    public void onCardboardTrigger() {
        Toast.makeText(this, "dir[0]: " + direction[0]
                             + " dir[1]: " + direction[1]
                             + " dir[2]: " + direction[2], Toast.LENGTH_LONG).show();
    }

    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    */

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class R3VRenderer implements CardboardView.Renderer {

        @Override
        public void onDrawFrame(HeadTransform headTransform, Eye eye, Eye eye1) {

        }

        @Override
        public void onFinishFrame(Viewport viewport) {

        }

        @Override
        public void onSurfaceChanged(int i, int i1) {

        }

        @Override
        public void onSurfaceCreated(EGLConfig eglConfig) {

        }

        @Override
        public void onRendererShutdown() {

        }
    }

}
