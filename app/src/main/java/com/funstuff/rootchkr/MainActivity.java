package com.funstuff.rootchkr;

import android.annotation.TargetApi;

import com.gc.materialdesign.views.ButtonFlat;
import com.gc.materialdesign.widgets.Dialog;
import com.gc.materialdesign.views.ButtonRectangle;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class MainActivity extends ActionBarActivity {

    TextView Result;
    Toolbar toolbar;
    LinearLayout layout;
    boolean lolipop;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        int currentapiVersion = android.os.Build.VERSION.SDK_INT;
        if (currentapiVersion > Build.VERSION_CODES.KITKAT){
            lolipop = true;
        } else{
            lolipop = false;
        }

        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        Result = (TextView) findViewById(R.id.result);
        ButtonRectangle check = (ButtonRectangle) findViewById(R.id.button);

        layout = (LinearLayout) findViewById(R.id.layout);

        check.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new checkRT().execute();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Result.setTextColor(getResources().getColor(R.color.black));
                            Result.setText("Testing");
                        }
                    });
                }
            });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch (id){
            case R.id.about:
                showAbout();
        }

        return super.onOptionsItemSelected(item);
    }


    private void showAbout(){


        final Dialog dialog = new Dialog(MainActivity.this, getResources().getString(R.string.about), getResources().getString(R.string.aboutDESCRIPTION));
        // Include dialog.xml file
        dialog.show();

        //dialog.setContentView(R.layout.activity_about);
        // Set dialog title
        //dialog.setTitle("About");

        // set values for custom dialog components - text, image and button
        //ImageView image = (ImageView) dialog.findViewById(R.id.imageDialog);
        //image.setImageResource(R.mipmap.ic_launcher);

//        ButtonFlat accept = (ButtonFlat) findViewById(R.id.okButton);
  //      dialog.setButtonAccept(accept);

        dialog.setOnAcceptButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
      /*  accept = (ButtonFlat) findViewById(R.id.okButton);
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });*/





    }

    private class checkRT extends AsyncTask<String, Integer, Void> {

        boolean result = false;
        private boolean CHECK() throws IOException, InterruptedException, SecurityException {
            String printline, result = null;
            String command = "su -c 'id'";
            try {

                java.lang.Process p = Runtime.getRuntime().exec(command);
                BufferedReader input = new BufferedReader(new InputStreamReader(p.getInputStream()));
                while ((printline = input.readLine()) != null) {
                    Log.d("Output", printline);
                    result = result + printline + "\n";
                }
            } catch (Exception e) {
                Log.d("Error :", e.toString());
            }
            if (result != null && result.contains("uid=0(root)")) {
                return true;
            } else {
                return false;
            }
        }
        protected Void doInBackground(String... command) {
            try {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Result.setText("Testing");
                    }

                });
                result = CHECK();

                if (result == true) {
                    runOnUiThread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            Result.setTextColor(getResources().getColor(R.color.granted));
                            toolbar.setBackgroundColor(getResources().getColor(R.color.granted));
                            layout.setBackgroundColor(getResources().getColor(R.color.grantedTint));
                            if(lolipop){
                                Window window = MainActivity.this.getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.grantedDark));
                            }

                            Result.setText("You have Root Access!");
                        }
                    });
                } else if (result == false) {
                    runOnUiThread(new Runnable() {
                        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
                        @Override
                        public void run() {
                            Result.setTextColor(getResources().getColor(R.color.denied));
                            toolbar.setBackgroundColor(getResources().getColor(R.color.denied));
                            layout.setBackgroundColor(getResources().getColor(R.color.deniedTint));

                            if(lolipop){
                                Window window = MainActivity.this.getWindow();
                                window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
                                window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
                                window.setStatusBarColor(MainActivity.this.getResources().getColor(R.color.deniedDark));
                            }

                            Result.setText("You do not have Root Access");
                        }
                    });
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        protected void onProgressUpdate(Integer... progress) {
        }

        protected void onPostExecute(final Long result) {
            Log.d("Post Execute", "executed");
        }
    }


}



