package com.pzh.wheeldemo;

import android.app.Activity;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.pzh.view.WheelView;

import java.util.ArrayList;

public class MainActivity extends Activity {
    private WheelView wheelView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        wheelView= (WheelView) findViewById(R.id.item_pick);
        ArrayList<String> items=new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            items.add("items"+i);
        }
        wheelView.setWheelLists(items);
        wheelView.setOnWheelViewSelected(new WheelView.OnWheelViewSelected() {
            @Override
            public void onSelected(int index, String value) {
                Toast.makeText(MainActivity.this,value,Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onClick(View v){
        ArrayList<String> items=new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            items.add("pzh"+i);
        }
        wheelView.setvisibleCount(5);
        wheelView.setWheelLists(items);
        wheelView.setOnWheelViewSelected(new WheelView.OnWheelViewSelected() {
            @Override
            public void onSelected(int index, String value) {
                Toast.makeText(MainActivity.this,index+" "+value,Toast.LENGTH_LONG).show();
            }
        });
    }

}
