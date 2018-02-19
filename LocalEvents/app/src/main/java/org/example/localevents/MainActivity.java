package org.example.localevents;


import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;

import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Add button to create an event.
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), CreateNew.class);
                startActivity(intent);
            }
        });

        // add events dynamically
        LinearLayout layout = findViewById(R.id.layout); //outer layout
        //add 20 items to the layout
        for (int i = 1; i <= 20; i++) {
            //get the screen data
            DisplayMetrics displayMetrics = new DisplayMetrics();
            getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

            //set the size constraint for each item
            int height = displayMetrics.heightPixels/10;
            int width = displayMetrics.widthPixels-(displayMetrics.widthPixels/4);

            //horizontal layout for event items
            LinearLayout layoutHorizontal = new LinearLayout(this);
            layoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);

            //vertical layout for list of event info
            LinearLayout layoutVertical = new LinearLayout(this);
            layoutVertical.setOrientation(LinearLayout.VERTICAL);
            //set height and width of the vertical layout
            //helps keep the button on right and info on left
            layoutVertical.setLayoutParams(new LinearLayout.LayoutParams(width,height));

            //add border to layout
            GradientDrawable border = new GradientDrawable();
            border.setColor(0xFFFFFFFF); //white background
            border.setStroke(1, 0xFF000000); //black border with full opacity
            //add to layout
            layoutVertical.setBackground(border);

            //add textview for the event name
            TextView Name = new TextView(this);
            Name.setText("Event Name"); //event name
            Name.setTextSize(18); // set size
            Name.setTextColor(Color.BLACK); //text color
            layoutVertical.addView(Name); //add to vertical layout

            //add location textview
            TextView Location = new TextView(this);
            Location.setText("Event Location");
            Location.setTextSize(18); //size
            layoutVertical.addView(Location); // add to vertical layout

            //add to horizontal layout
            layoutHorizontal.addView(layoutVertical);

            //add view button
            Button btn = new Button(this);
            btn.setId(i); //set button id
            final int id_ = btn.getId(); //get id
            btn.setText("View"); // button text
            //add to horizontal layout
            layoutHorizontal.addView(btn);
            //add to the upper layout
            layout.addView(layoutHorizontal);

            //get the button we just created
            Button btn1 = findViewById(id_);

            //set the action upon click
            btn1.setOnClickListener(new View.OnClickListener() {
                public void onClick(View view) {
                    //if clicked open the view activity

                    ///******Add data that's sent here*******

                    Intent intent = new Intent(view.getContext(), view_event.class);
                    startActivity(intent);

                }
            });

        }
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

        //if account setting is selected
        if (id == R.id.account) {
            //open the login activity
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            return true;
        }
        
        return super.onOptionsItemSelected(item);
    }
}
