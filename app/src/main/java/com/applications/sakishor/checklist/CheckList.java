package com.applications.sakishor.checklist;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class CheckList extends ActionBarActivity {
    private static final String LIST_SIZE = "list_size";
    private static final String PREF_NAME = "pref_name";
    private static final String TO_DO_ACTIVITY_PREFERENCE = "to_do_activity_preference";

    /**
     * The container view which has layout change animations turned on. In this
     * sample, this view is a {@link android.widget.LinearLayout}.
     */
    private ViewGroup mContainerView;
    private EditText mEditText;
    ArrayList<String> todoItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_to_do);
        mContainerView = (ViewGroup) findViewById(R.id.container);
        mEditText = (EditText) findViewById(R.id.myEditText);

        mEditText.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) &&
                        (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    saveItem();
                }
                return false;
            }
        });

        // Create the array list of to do items
        todoItems = new ArrayList<String>();
        restoreList();
    }

    /**
     * restore the saved list
     */
    private void restoreList() {
        SharedPreferences sharedPref = this.getSharedPreferences(TO_DO_ACTIVITY_PREFERENCE, Context.MODE_PRIVATE);
        if (sharedPref.getInt(LIST_SIZE, 0) > 0) {
            for (int i = 0; i < sharedPref.getInt(LIST_SIZE, 0); i++) {
                todoItems.add(sharedPref.getString(PREF_NAME + i, ""));
            }
        }
        for (String text : todoItems) {
            addItem(text);
        }
    }

    @Override
    protected void onPause() {
        super.onPause(); // Always call the superclass method first
        int count = mContainerView.getChildCount();
        for (int i = 0; i < mContainerView.getChildCount(); i++) {

            ViewGroup v = (ViewGroup) (mContainerView.getChildAt(i));
            TextView e = (TextView) v.getChildAt(0);

            SharedPreferences.Editor sharedPref =
                    this.getSharedPreferences(TO_DO_ACTIVITY_PREFERENCE, Context.MODE_PRIVATE).edit();
            sharedPref.putInt(LIST_SIZE, count);
            sharedPref.putString(PREF_NAME + i, e.getText().toString());

            sharedPref.commit();
        }

    }

    public void onSaveTheItem(View v) {
        saveItem();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_to_do, menu);
        menu.getItem(0).setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_save) {
            saveItem();
            mEditText.setText("");
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveItem() {
        String text = mEditText.getText().toString();
        if (!TextUtils.isEmpty(text)) {
            addItem(text);
        } else {
            Toast.makeText(this, "Won't save empty TODO", Toast.LENGTH_SHORT).show();
        }
        mEditText.setText("");
    }

    private void addItem(String textToAdd) {
        // Instantiate a new "row" view.
        final ViewGroup newView =
                (ViewGroup) LayoutInflater.from(this).inflate(R.layout.list_item_example, mContainerView, false);

        // Set the text in the new row to a random country.
        ((TextView) newView.findViewById(android.R.id.text1)).setText(textToAdd);

        // Set a click listener for the "X" button in the row that will remove
        // the row.
        newView.findViewById(R.id.delete_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Remove the row from its parent (the container view).
                // Because mContainerView has
                // android:animateLayoutChanges set to true,
                // this removal is automatically animated.
                mContainerView.removeView(newView);

                // If there are no rows remaining, show the empty view.
//                if (mContainerView.getChildCount() == 0) {
//                    findViewById(android.R.id.empty).setVisibility(View.VISIBLE);
//                }
            }
        });

        // Because mContainerView has android:animateLayoutChanges set to true,
        // adding this view is automatically animated.
        mContainerView.addView(newView, 0);
    }
}
