package com.example.davidmvp23.realrealflash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity {

    //UI components for review tab
    Spinner subjectSpinner;
    CheckBox randomizeCheckBox;
    protected static ArrayList<Card> allcards;
    //UI components for insert tab
    EditText questionEditText, answerEditText;
    Spinner insertSubjectSpinner;
    private Context context;
    protected static FlashCarddbAdapter dbAdapt;   // made static to access in CourseView - must be better way?
    private static Cursor cCursor;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = getApplicationContext();
        //Set up the tabs
        setupTabs();

        //Set up the UI for the review tab
        setupReviewTab();

        //Setup the UI for the insert tab
        setupInsertTab();
        context = getApplicationContext();
        dbAdapt = new FlashCarddbAdapter(this);
        dbAdapt.open();
        dbAdapt.insertCard(new Card("intro java", "A+", "HAHA"));
        allcards = new ArrayList<Card>();
        populateList();

    }

    public void populateList()
    {
        cCursor = dbAdapt.getAllCard();
        cCursor.moveToNext();
        cCursor.moveToNext();
        cCursor.moveToNext();

        System.out.println(cCursor.getCount());


    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_realflash, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private void setupTabs() {
        TabHost tabHost = (TabHost) findViewById(R.id.tabHost);
        tabHost.setup();
        TabHost.TabSpec tabSpec = tabHost.newTabSpec("reviewTab");
        tabSpec.setContent(R.id.reviewLayout);
        tabSpec.setIndicator("Review");
        tabHost.addTab(tabSpec);
        tabSpec = tabHost.newTabSpec("insertTab");
        tabSpec.setContent(R.id.insertLayout);
        tabSpec.setIndicator("Insert cards");
        tabHost.addTab(tabSpec);
    }

    private void setupReviewTab() {
        subjectSpinner = (Spinner) findViewById(R.id.subjectSpinner);
        randomizeCheckBox = (CheckBox) findViewById(R.id.randomizeCheckBox);
        Button startButton = (Button) findViewById(R.id.startButton);

        //TODO: Populate spinner here (need to pull subjects from database) DONE
        SharedPreferences values = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = values.edit();
        int num = values.getInt("number", 0);
        String str = values.getString("subject", "");
        String[] parts = str.split("_");
        ArrayList<String> ar = new ArrayList<String>();
        for (int i = 0 ; i < num; i++) {
            ar.add(parts[i]);
        }

        //Populate the spinner with the arraylist from the shared preferences
        String[] spinnerList = new String[ar.size()];
        spinnerList = ar.toArray(spinnerList);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i=0; i < ar.size(); i++) {
            adapter.add(spinnerList[i]);
        }
        subjectSpinner.setAdapter(adapter);



        //TODO: Do we need this? Same as below- check the listener
        /*
        //Set checkbox listener to subject checkbox
        subjectSpinner.setOnClickListener(cbListener);
        */

        //Set start button listener
        startButton.setOnClickListener(reviewTabListener);
    }

    private void setupInsertTab() {

        insertSubjectSpinner = (Spinner)findViewById(R.id.insertSubjectSpinner);
        Button addSubjectButton = (Button)findViewById(R.id.addSubjectButton);
        questionEditText = (EditText)findViewById(R.id.questionEditText);
        answerEditText = (EditText)findViewById(R.id.answerEditText);
        Button insertButton = (Button)findViewById(R.id.insertButton);

        //Set listener to add subject button and insert button
        addSubjectButton.setOnClickListener(insertTabListeners);
        insertButton.setOnClickListener(insertTabListeners);

        //TODO: Populate spinner from the database DONE
        SharedPreferences values = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = values.edit();
        int num = values.getInt("number", 0);
        String str = values.getString("subject", "");
        String[] parts = str.split("_");
        ArrayList<String> ar = new ArrayList<String>();
        for (int i = 0 ; i < num; i++) {
            ar.add(parts[i]);
        }

        //Populate the spinner with the arraylist from the shared preferences
        String[] spinnerList = new String[ar.size()];
        spinnerList = ar.toArray(spinnerList);
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (this, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        for (int i=0; i < ar.size(); i++) {
            adapter.add(spinnerList[i]);
        }
        insertSubjectSpinner.setAdapter(adapter);

    }









    //Listener for the buttons on the review tab
    View.OnClickListener reviewTabListener = new View.OnClickListener() {
        @Override
        public void onClick (View v) {
            reviewStartButton();
        }
    };

    //Listener for the buttons on the insert cards tab
    View.OnClickListener insertTabListeners = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            switch(v.getId()) {
                case R.id.addSubjectButton:
                    addSubject();
                    break;
                case R.id.insertButton:
                    insertFlashcard();
                    break;
            }
        }
    };




    //Code for buttons

    protected void addSubject() {

        //Setup view for alert dialog
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View tempView = inflater.inflate(R.layout.alert_add_subject, null);
        final EditText subjectEditText = (EditText)tempView.findViewById(R.id.subjectEditText);

        //Make a builder for the alert dialog
        AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
        mBuilder.setTitle("Add a new subject");
        mBuilder.setView(tempView);
        //Set the positive button listener for the alert dialog
        mBuilder.setPositiveButton("Enter", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String newSubject = subjectEditText.getText().toString();

                //TODO: Need to add new subject to the database DONE
                SharedPreferences values = PreferenceManager.getDefaultSharedPreferences(context);
                SharedPreferences.Editor editor = values.edit();
                int num = values.getInt("number", 0) + 1;
                String str;
                if (num==1)
                    str = newSubject;
                else
                    str = values.getString("subject", "") + "_" + newSubject;
                editor.putInt("number", num);
                editor.putString("subject", str);
                editor.commit();
                //TODO: Need to notify spinner adapter that the data set has changed / repopulate the spinner


                //TODO: Populate spinner from the database DONE
                num = values.getInt("number", 0);
                str = values.getString("subject", "");
                String[] parts = str.split("_");
                ArrayList<String> ar = new ArrayList<String>();
                for (int i = 0 ; i < num; i++) {
                    ar.add(parts[i]);
                }
                //Populate the spinner with the arraylist from the shared preferences
                String[] spinnerList = new String[ar.size()];
                spinnerList = ar.toArray(spinnerList);
                ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence> (getApplicationContext(), android.R.layout.simple_spinner_item);
                adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                for (int i=0; i < ar.size(); i++) {
                    adapter.add(spinnerList[i]);
                }
                insertSubjectSpinner.setAdapter(adapter);
                subjectSpinner.setAdapter(adapter);


            }
        });
        mBuilder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                //cancel alert
            }
        });
        mBuilder.show();
    }

    protected void insertFlashcard(){

        if(questionEditText.getText().toString().equals("") ||
                answerEditText.getText().toString().equals("")) {
            Toast.makeText(this, "Please fill question and answer fields", Toast.LENGTH_SHORT).show();
            return;
        }
        else if (insertSubjectSpinner.getSelectedItem().toString().equals("Select a subject")) {
            Toast.makeText(this, "Please make/select a subject", Toast.LENGTH_SHORT).show();
            return;
        }
        else {
            String question = questionEditText.getText().toString();
            String answer = answerEditText.getText().toString();
            String subject = insertSubjectSpinner.getSelectedItem().toString();
            Card c = new Card(subject, question, answer);

            //TODO: bundle information into the database
            dbAdapt.insertCard(c);
            Toast.makeText(this, "The card has been added!", Toast.LENGTH_SHORT).show();

        }

    }

    private void reviewStartButton() {

        System.out.println(cCursor.moveToNext());
        System.out.println(cCursor.moveToNext());
        System.out.println(cCursor.moveToNext());

        //TODO: If the spinner is empty, then we can not start the new activity, else we go ahead with it

            //Get the subject selected by the spinner
            String subject = subjectSpinner.getSelectedItem().toString();
            //Get the randomize boolean from the checkbox
            Boolean randomize = randomizeCheckBox.isChecked();

            Intent mIntent = new Intent(this, flashcard.class);
            mIntent.putExtra("randomize", randomize);
            mIntent.putExtra("subject", subject);
            startActivity(mIntent);

    }


}
