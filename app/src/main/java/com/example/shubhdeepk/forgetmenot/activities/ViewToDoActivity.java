package com.example.shubhdeepk.forgetmenot.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shubhdeepk.forgetmenot.R;
import com.example.shubhdeepk.forgetmenot.models.ToDoDbHandler;

import java.util.Arrays;

public class ViewToDoActivity extends AppCompatActivity {

    Bundle bundle;
    ToDoDbHandler db;

    EditText editName, editDueDate, editNotes;
    Button toDoSpeak, notesSpeak;
    Spinner spinnerPriority, spinnerStatus;
    String name,type;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_item);

        bundle = getIntent().getExtras();
        name = bundle.getString("NAME");
        type = bundle.getString("TYPE");
        setTitle(type);

        getSupportActionBar().setLogo(R.mipmap.forgetflower);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        getSupportActionBar().setDisplayUseLogoEnabled(true);
        db = new ToDoDbHandler(this);

        editName = (EditText) findViewById(R.id.editName);
        editName.setText(name);
        editName.setEnabled(false);
        editName.setFocusable(false);
        editName.setCursorVisible(false);
        editName.setKeyListener(null);

        editDueDate = (EditText) findViewById(R.id.editDueDate);
        editDueDate.setText(db.getDueDate(name));
        editDueDate.setEnabled(false);
        editDueDate.setFocusable(false);
        editDueDate.setCursorVisible(false);
        editDueDate.setKeyListener(null);

        editNotes = (EditText) findViewById(R.id.editNotes);
        editNotes.setText(db.getNotes(name));
        editNotes.setEnabled(false);
        editNotes.setFocusable(false);
        editNotes.setCursorVisible(false);
        editNotes.setKeyListener(null);

        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        spinnerPriority.setSelection(Arrays.asList(getResources().getStringArray(R.array.priority_list)).indexOf(db.getPriority(name)));
        spinnerPriority.setEnabled(false);

        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        spinnerStatus.setSelection(Arrays.asList(getResources().getStringArray(R.array.status_list)).indexOf(db.getStatus(name)));
        spinnerStatus.setEnabled(false);

        toDoSpeak = (Button) findViewById(R.id.toDoSpeak);
        toDoSpeak.setVisibility(View.INVISIBLE);

        notesSpeak = (Button) findViewById(R.id.notesSpeak);
        notesSpeak.setVisibility(View.INVISIBLE);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_view, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.viewEdit:
                //Intent editIntent = new Intent(ViewTaskActivity.this, EditTaskActivity.class);
                //editTaskIntent.putExtra(MainActivity.BUNDLE_TASK_NAME, editTextTaskName.getText().toString());
                //ViewTaskActivity.this.startActivityForResult(editTaskIntent, MainActivity.REQUEST_CODE_EditTaskActivity);
                Toast.makeText(getApplicationContext(), "Edit " + name, Toast.LENGTH_LONG).show();
                Intent editIntent = new Intent(ViewToDoActivity.this,AddToDoActivity.class);
                editIntent.putExtra("NAME",name);
                editIntent.putExtra("TYPE","Edit");
                ViewToDoActivity.this.startActivityForResult(editIntent,1);
                return true;
            case R.id.viewDelete:
                //checkDeleteOnTask = Boolean.TRUE;
                db.delete(name);
                Intent resultIntent = new Intent();
                setResult(RESULT_OK, resultIntent);
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        this.finish();
    }
}
