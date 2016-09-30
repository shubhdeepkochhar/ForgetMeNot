package com.example.shubhdeepk.forgetmenot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

import com.example.shubhdeepk.forgetmenot.adapters.CustomToDoAdapter;
import com.example.shubhdeepk.forgetmenot.R;
import com.example.shubhdeepk.forgetmenot.models.ToDoDbHandler;

import static com.example.shubhdeepk.forgetmenot.R.id.addNewItem;

//import static com.example.shubhdeepk.forgetmenot.R.id.addNewItem;

public class MainActivity extends AppCompatActivity {

    ToDoDbHandler db;
    ListView listToDo;

    FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setIcon(R.mipmap.forgetflower);

        fab = (FloatingActionButton) findViewById(addNewItem);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               // Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
               //.setAction("Action", null).show();

                Intent addIntent = new Intent(MainActivity.this, AddToDoActivity.class);
                addIntent.putExtra("NAME","");
                addIntent.putExtra("TYPE","Add");
                MainActivity.this.startActivityForResult(addIntent,1);
            }
        });

        db = new ToDoDbHandler(this);
        listToDo = (ListView)findViewById(R.id.lvItems);
        listToDo.setAdapter(new CustomToDoAdapter(this, db));
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


        return super.onOptionsItemSelected(item);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*switch(requestCode) {
            case (1) :
                if (resultCode == MainActivity.RESULT_OK) {
                    listToDo.setAdapter(new CustomToDoAdapter(this, db));
                }
                break;
            case (2) :
                if (resultCode == MainActivity.RESULT_OK) {
                    listToDo.setAdapter(new CustomToDoAdapter(this, db));
                }
                break;
            case (3) :
                if (resultCode == MainActivity.RESULT_OK) {
                    listToDo.setAdapter(new CustomToDoAdapter(this, db));
                }
                break;
        }*/
        listToDo.setAdapter(new CustomToDoAdapter(this, db));
    }
}
