package com.example.shubhdeepk.forgetmenot.activities;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.shubhdeepk.forgetmenot.R;
import com.example.shubhdeepk.forgetmenot.models.ToDoDbHandler;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by shkochh on 9/27/2016.
 */

public class AddToDoActivity extends AppCompatActivity {

    private final int REQ_TODO_INPUT = 100;
    private final int REQ_NOTES_INPUT = 200;

    Bundle bundle;
    ToDoDbHandler db;
    EditText editName, editDueDate, editNotes;
    Spinner spinnerStatus, spinnerPriority;
    Button toDoSpeak, notesSpeak;
    String name, type;
    boolean fromEdit = false;

    DatePickerDialog.OnDateSetListener dateSetListener;
    private int year, month, day;

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
        if(name.matches("")) fromEdit = false;
        else fromEdit = true;


        toDoSpeak = (Button) findViewById(R.id.toDoSpeak);
        toDoSpeak.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                toDoSpeechInput();
            }
        });
        notesSpeak = (Button) findViewById(R.id.notesSpeak);
        notesSpeak.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                notesSpeechInput();
            }
        });
        editName = (EditText) findViewById(R.id.editName);
        editDueDate = (EditText) findViewById(R.id.editDueDate);
        editNotes = (EditText) findViewById(R.id.editNotes);
        spinnerPriority = (Spinner) findViewById(R.id.spinnerPriority);
        spinnerStatus = (Spinner) findViewById(R.id.spinnerStatus);
        editDueDate.setKeyListener(null);
        editDueDate.setCursorVisible(false);
        editDueDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog(0);
            }
        });

        db = new ToDoDbHandler(this);
        if(!fromEdit) {
            final Calendar cal = Calendar.getInstance();
            day = cal.get(Calendar.DAY_OF_MONTH);
            month = cal.get(Calendar.MONTH);
            year = cal.get(Calendar.YEAR);

            editDueDate.setText(new StringBuilder().append(year).append("/").append(month + 1).append("/").append(day));
        }
        else{
            editName.setText(name);
            editNotes.setText(db.getNotes(name));
            editDueDate.setText(db.getDueDate(name));
            String[] date = db.getDueDate(name).split("/");
            year = Integer.parseInt(date[0]);
            month = Integer.parseInt(date[1]) - 1;
            day = Integer.parseInt(date[2]);
            spinnerPriority.setSelection(Arrays.asList(getResources().getStringArray(R.array.priority_list)).indexOf(db.getPriority(name)));
            spinnerStatus.setSelection(Arrays.asList(getResources().getStringArray(R.array.status_list)).indexOf(db.getStatus(name)));

        }
        dateSetListener = new DatePickerDialog.OnDateSetListener(){

                @Override
                public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                    AddToDoActivity.this.month = month;
                    AddToDoActivity.this.year = year;
                    AddToDoActivity.this.day = dayOfMonth;

                    editDueDate.setText(new StringBuilder().append(year).append("/").append(month+1).append("/").append(day));
            }
        };

    }

    private void toDoSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Say Task!");
        try {
            startActivityForResult(intent, REQ_TODO_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Sorry no Speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    private void notesSpeechInput() {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault());
        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Add notes!");
        try {
            startActivityForResult(intent, REQ_NOTES_INPUT);
        } catch (ActivityNotFoundException a) {
            Toast.makeText(getApplicationContext(), "Sorry no Speech input",
                    Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected Dialog onCreateDialog(int id) {
        switch (id) {
            case 0:
                return new DatePickerDialog(this, dateSetListener, year, month, day);
        }
        return null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_todo, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addSave:
                if(editName.getText().toString().matches("")){
                    Toast.makeText(getApplicationContext(), "Empty name", Toast.LENGTH_LONG).show();
                }
                else{
                    if(fromEdit){
                        if(db.updateToDo(name,editName.getText().toString(), editDueDate.getText().toString(), spinnerPriority.getSelectedItem().toString(),spinnerStatus.getSelectedItem().toString(), editNotes.getText().toString())){
                            Intent resIntent = new Intent();
                            setResult(MainActivity.RESULT_OK, resIntent);
                            finish();
                        }
                        else{
                            Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                        }

                    }
                    else{

                    if(db.insertToDo(editName.getText().toString(), editDueDate.getText().toString(), spinnerPriority.getSelectedItem().toString(),spinnerStatus.getSelectedItem().toString(), editNotes.getText().toString())){
                        Intent resIntent = new Intent();
                        setResult(MainActivity.RESULT_OK, resIntent);
                        finish();
                    }

                    else{
                        Toast.makeText(getApplicationContext(), "Error", Toast.LENGTH_LONG).show();
                    }
                }
                }
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent resIntent = new Intent();
        setResult(MainActivity.RESULT_CANCELED, resIntent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_TODO_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editName.setText(result.get(0));
                }
                break;
            }
            case REQ_NOTES_INPUT: {
                if (resultCode == RESULT_OK && null != data) {

                    ArrayList<String> result = data
                            .getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
                    editNotes.setText(result.get(0));
                }
                break;
            }

        }
    }

}