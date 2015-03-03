package com.example.davidmvp23.realrealflash;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TabHost;
import android.widget.Toast;


public class MainActivity extends Activity {

    //UI components for review tab
    Spinner subjectSpinner;
    CheckBox randomizeCheckBox;

    //UI components for insert tab
    EditText questionEditText, answerEditText;
    Spinner insertSubjectSpinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Set up the tabs
        setupTabs();

        //Set up the UI for the review tab
        setupReviewTab();

        //Setup the UI for the insert tab
        setupInsertTab();
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

        //TODO: Pull subjects from database and populate spinner


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

        //TODO: Load subjects from database and populate spinner


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

                //TODO: Need to add new subject to the database

                //TODO: Need to notify spinner adapter that the data set has changed / repopulate the spinner

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

            //TODO: bundle information into the database

            Toast.makeText(this, "The card has been added!", Toast.LENGTH_SHORT).show();

        }

    }

    private void reviewStartButton() {

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
