package com.example.davidmvp23.realrealflash;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;


public class flashcard extends Activity implements GestureDetector.OnGestureListener, GestureDetector.OnDoubleTapListener {

    private GestureDetector mDetector;
    TextView cardView;
    private int currentIndex;
    String cardQuestion, cardAnswer;
    Boolean cardFlipped; //TRUE is answer side, FALSE is question side
    protected static FlashCarddbAdapter dbAdapt;   // made static to access in CourseView - must be better way?
    private static Cursor cCursor;
    //TODO: Need some type of global data structure for holding the cards
    protected static ArrayList<Card> allcards;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_flashcard);
        currentIndex = 0;
        mDetector = new GestureDetector(this, this);

        cardView = (TextView) findViewById(R.id.cardTextView);

        //Pull information from intent
        Intent mIntent = getIntent();
        String subject = mIntent.getStringExtra("subject");
        Boolean randomize = mIntent.getBooleanExtra("randomize", false);

        //Pull all cards with string subject from the database and put them in an arraylist or something
        dbAdapt = new FlashCarddbAdapter(this);
        dbAdapt.open();
        allcards = new ArrayList<Card>();
        populateList();

        //If randomize==true then shuffle the cards in the arraylist
        Random rn = new Random();

        for (int i = 0 ; i < allcards.size();i++) {
            int answer = rn.nextInt(allcards.size()-i) + i;
            Card temp = allcards.get(i);
            allcards.set(i, allcards.get(answer));
            allcards.set(answer, temp);
        }

        //Get first card in data structure, replace the line below
        Card c = allcards.get(0);
        cardQuestion = c.getQuestion();
        cardAnswer = c.getAnswer();

        cardView.setText(cardQuestion);
        cardFlipped = false;

    }

    @Override
    public void onResume() {
        super.onResume();

    }

    public void populateList()
    {
        cCursor = dbAdapt.getAllCard();
        update();



    }

    public void update() {

        allcards.clear();

        if (cCursor.moveToFirst())
            do {
                Card result = new Card(cCursor.getString(1), cCursor.getString(2), cCursor.getString(3));
                allcards.add(0, result);

            } while (cCursor.moveToNext());
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
        else if (id == R.id.deleteButton) {
            //TODO: Delete the card we are currently on. Use currentIndex to figure out which card
            //System.out.println("LOL");
            Card c = allcards.remove(this.currentIndex);
            dbAdapt.removeCard(c);
            if (allcards.size()== 0) {
                //TODO: show text this is the last card
                cardView.setText("Deleted the last card. Please go back and add more cards");
            }
            else if ( this.currentIndex == 0) {
                this.currentIndex--;
                this.onSwipeLeft();

            }
            else {

                this.onSwipeRight();
            }



            //we're on and remove from arraylist and database
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


    private void remove() {
        long ri = 0;
        Card c = allcards.get(this.currentIndex);


    }
    private void onSwipeRight() {
        Toast.makeText(this, "Swiped right", Toast.LENGTH_SHORT).show();

        //If this is the first card, cannot scroll back. Check index of data structure. Maybe use variable to keep track of which card we're on?
        if (currentIndex == 0) {
            Toast.makeText(this, "First card cant scroll back!!!", Toast.LENGTH_SHORT).show();
        }
        else {
            //Pull previous card in data structure and put question and answer into the strings

            Card c = allcards.get(currentIndex - 1);
            currentIndex--;
            cardAnswer = c.getAnswer();
            cardQuestion = c.getQuestion();
            cardView.setText(cardQuestion);
            cardFlipped = false;
        }

    }

    private void onSwipeLeft() {
        Toast.makeText(this, "swiped left", Toast.LENGTH_SHORT).show();

        //If this is the last card, cannot move to next card
        if (currentIndex == allcards.size() ) {
            Toast.makeText(this, "Last card cant scroll forward!!!", Toast.LENGTH_SHORT).show();
        }
        //Pull next card in data structure and put question and answer into the strings
        else {
            Card c = allcards.get(currentIndex + 1);
            currentIndex++;
            cardAnswer = c.getAnswer();
            cardQuestion = c.getQuestion();
            cardView.setText(cardQuestion);
            cardFlipped = false;
        }



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
