package com.example.bbartha.catchthebuscj;

import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by Bartha on 12/1/2014.
 */
public class MainActivity extends ListActivity {
    static final String[] SETTINGS = new String[]{"24b Polus --> Unirii", "24b Unirii --> Polus",
            "37 MihaiViteazu --> Tetarom1", "37 Tetarom1 --> MihaiViteazu", "Quit!"};

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setListAdapter(new ArrayAdapter<String>(this, R.layout.activity_main,
                SETTINGS));

        ListView listView = getListView();
        listView.setTextFilterEnabled(true);

        // gives a new click listener to the current listView

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                if (((((TextView) view).getText())).equals("24b Polus --> Unirii")) {
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    i.putExtra("busRouteNumber", "24b");
                    i.putExtra("direction", "polus_unirii");
                    startActivity(i);
                } else if (((((TextView) view).getText()))
                        .equals("24b Unirii --> Polus")) {
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    i.putExtra("busRouteNumber", "24b");
                    i.putExtra("direction", "unirii_polus");
                    startActivity(i);
                } else if (((((TextView) view).getText()))
                        .equals("37 MihaiViteazu --> Tetarom1")) {
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    i.putExtra("busRouteNumber", "37");
                    i.putExtra("direction", "MihaiViteazu_Tetarom1");
                    startActivity(i);
                } else if (((((TextView) view).getText()))
                        .equals("37 Tetarom1 --> MihaiViteazu")) {
                    Intent i = new Intent(MainActivity.this, MapsActivity.class);
                    i.putExtra("busRouteNumber", "37");
                    i.putExtra("direction", "Tetarom1_MihaiViteazu");
                    startActivity(i);
                } else if (((((TextView) view).getText())).equals("Quit!")) {
                    finish();
                }
            }
        });
    }

//    public boolean onKeyDown(int keyCode, KeyEvent event) {
//        if (keyCode == KeyEvent.KEYCODE_MENU) {
//            Intent i = new Intent(MainActivity.this, setupCommands.class);
//            startActivity(i);
//            return true;
//        }
//        return super.onKeyDown(keyCode, event);
//    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // call the base class method
        super.onCreateOptionsMenu(menu);
        return true;
    }
}
