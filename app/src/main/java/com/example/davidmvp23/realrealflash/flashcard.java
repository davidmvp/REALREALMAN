package com.example.davidmvp23.realrealflash;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;


public class flashcard extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetector mDetector;
    TextView cardView;

    String cardQuestion, cardAnswer;
    Boolean cardFlipped; //TRUE is answer side, FALSE is question side

    //TODO: Need some type of global data structure for holding the cards


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);

        mDetector = new GestureDetector(this, this);

        cardView = (TextView) findViewById(R.id.cardTextView);

        //Pull information from intent
        Intent mIntent = getIntent();
        String subject = mIntent.getStringExtra("subject");
        Boolean randomize = mIntent.getBooleanExtra("randomize", false);

        //TODO: Pull all cards with string subject from the database and put them in an arraylist or something

        //TODO: If randomize==true then shuffle the cards in the arraylist

        //TODO: Get first card in data structure, replace the line below
        Card c = new Card("hello", "THIS IS THE QUESTION", "THIS IS THE ANSWER");
        cardQuestion = c.getQuestion();
        cardAnswer = c.getAnswer();

        cardView.setText(cardQuestion);
        cardFlipped = false;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_flashcard, menu);
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


    private void singleTap() {
        Toast.makeText(this, "Tapped", Toast.LENGTH_SHORT).show();

        if(cardFlipped) {
            cardView.setText(cardQuestion);
            cardFlipped = false;
        }
        else {
            cardView.setText(cardAnswer);
            cardFlipped = true;
        }

    }

    private void onSwipeRight() {
        Toast.makeText(this, "Swiped right", Toast.LENGTH_SHORT).show();

        //TODO: If this is the first card, cannot scroll back. Check index of data structure. Maybe use variable to keep track of which card we're on?

        //TODO: Pull previous card in data structure and put question and answer into the strings
        /*
        Card c; //Pull from data structure here
        cardAnswer = c.getAnswer();
        cardQuestion = c.getQuestion();
        cardView.setText(cardQuestion);
        cardFlipped = false;
        */
    }

    private void onSwipeLeft() {
        Toast.makeText(this, "swiped left", Toast.LENGTH_SHORT).show();

        //TODO: If this is the last card, cannot move to next card. Same as the above todo

        //TODO: Pull next card in data structure and put question and answer into the strings
        /*
        Card c; //Pull from data structure here
        cardAnswer = c.getAnswer();
        cardQuestion = c.getQuestion();
        cardView.setText(cardQuestion);
        cardFlipped = false;
        */

    }


    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        // Be sure to call the superclass implementation
        return super.onTouchEvent(event);
    }

    @Override
    public boolean onDown(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onFling(MotionEvent event1, MotionEvent event2,
                           float velocityX, float velocityY) {
        float diffX = event2.getX() - event1.getX();
        if (Math.abs(diffX) > 100 && Math.abs(velocityX) > 100) {
            if (diffX > 0) {
                onSwipeRight();
            } else {
                onSwipeLeft();
            }
        }

        return true;
    }

    @Override
    public void onLongPress(MotionEvent event) {
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    @Override
    public void onShowPress(MotionEvent event) {
    }

    @Override
    public boolean onSingleTapUp(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onSingleTapConfirmed(MotionEvent event) {
        singleTap();
        return true;
    }
    @Override
    public boolean onDoubleTap(MotionEvent event) {
        return false;
    }

    @Override
    public boolean onDoubleTapEvent(MotionEvent event) {
        return false;
    }

}
