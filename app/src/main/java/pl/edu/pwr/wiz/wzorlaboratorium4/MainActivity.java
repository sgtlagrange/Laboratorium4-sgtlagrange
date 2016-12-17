package pl.edu.pwr.wiz.wzorlaboratorium4;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private SharedPreferences pref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /* Resetowanie ustawień */
        Button btn = (Button) findViewById(R.id.btn_reset);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(view.getContext());

                // Tytul
                alertDialogBuilder.setTitle("Reset ustawień");

                // Ustawiamy dialog
                alertDialogBuilder
                        .setMessage("Kliknij tak, aby potwierdzić?")
                        .setCancelable(false)
                        .setPositiveButton("Tak",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // Kliknięto tak
                                // @TODO Reset ustawień

                                // @TODO Wyświetlenie Toast z informacją
                            }
                        })
                        .setNegativeButton("Nie",new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,int id) {
                                // Kliknięto nie
                                // @TODO Wyświetlenie Toast z informacją
                                dialog.cancel();
                            }
                        });

                // Utworz okno dialogowe i wyswietl je
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();

        showPrefs();
    }

    private void showPrefs() {
        /* Pobieramy ustawienia i wyswietlamy login FB, jesli jest ustawiony */
        pref = PreferenceManager.getDefaultSharedPreferences(this);
        Boolean fb_on = pref.getBoolean("facebook_on", false);
        TextView textView = (TextView) findViewById(R.id.dane_fb);

        if(fb_on) {
            textView.setText("Twój login FB to: " + pref.getString("facebook_login", "brak"));
        } else {
            textView.setText("FB wyłączone");
        }

        // @TODO Wyświetlanie przycisku kierującego do profilu Twitter, o ile taki został ustawiony
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

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this, SettingsActivity.class);
            startActivity(settings);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

