package com.mycompany.stevesapp;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.app.Activity;
import android.content.ContentResolver;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.content.Intent;
import android.widget.Button;
import android.widget.EditText;
import android.view.View;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;


public class MainActivity extends Activity { //changed from ActionBarActivity to Activity??

    //public final static String EXTRA_MESSAGE = "com.mycompany.StevesApp.MESSAGE";

    // Constants used by SyncAdapter:
    // The authority for the sync adapter's content provider
    public static final String AUTHORITY = "com.mycompany.stevesapp.provider";
    // An account type, in the form of a domain name
    public static final String ACCOUNT_TYPE = "mycompany.com";
    // The account name
    public static final String ACCOUNT = "dummyaccount";
    // Instance fields
    Account mAccount;

    // Sync interval constants
    public static final long SECONDS_PER_MINUTE = 60L;
    public static final long SYNC_INTERVAL_IN_MINUTES = 60L;
    public static final long SYNC_INTERVAL =
            SYNC_INTERVAL_IN_MINUTES *
                    SECONDS_PER_MINUTE;
    // Global variables
    // A content resolver for accessing the provider
    ContentResolver mResolver;  //not sure about this - not using a provider!!!!!!!!!!!!!!!!!


    //Object variable for the edit text fields, buttons, dataTable and the database manager class
    EditText messageField, updateIDField, updateMessageField;
    Button addButton, retrieveButton, updateButton;
    TableLayout dataTable;
    MyDatabaseManager db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        try {
            super.onCreate(savedInstanceState);             //these statements could be put in a try-catch block.
            setContentView(R.layout.activity_main);
            //NEED TO CALL setupGUI(), addButtonListeners() ?? - Done.
            db = new MyDatabaseManager(this);
            setupGUI();
            addButtonListeners();
            updateTable();
        }
        catch (Exception e){
            Log.e("Error", e.toString());
            e.printStackTrace();
        }

        // Create the dummy account
        mAccount = CreateSyncAccount(this);

        // Get the content resolver for your app
        mResolver = getContentResolver();     //But app DOESN'T use a Resolver!!!!!!!!!!!!!!!!!
        /*
         * Turn on periodic syncing
         */
        ContentResolver.addPeriodicSync(mAccount,AUTHORITY,Bundle.EMPTY,SYNC_INTERVAL);

    }



    /**
     * creates references and listeners for the GUI
     */

    private void setupGUI(){
        //NOT SURE why (casting of Types) is needed in the following statements
        // when the types have been declared above
        dataTable = (TableLayout)findViewById(R.id.data_table);
        messageField = (EditText)findViewById(R.id.edit_message);
        updateIDField = (EditText)findViewById(R.id.update_id);
        updateMessageField = (EditText)findViewById(R.id.update_message);
        addButton = (Button)findViewById(R.id.add_button);
        retrieveButton = (Button)findViewById(R.id.retrieve_button);
        updateButton = (Button)findViewById(R.id.update_button);
    }

    private void addButtonListeners(){  //Done more easily with xml onClick?
        addButton.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){addRow();}});
        retrieveButton.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){retrieveRow();}});
        updateButton.setOnClickListener(new View.OnClickListener(){@Override public void onClick(View v){updateRow();}});

    }

    private void addRow(){
        //put in a try-catch
        db.addRow(messageField.getText().toString());   // db is a MyDatabaseManager object
        updateTable();
        emptyFormFields();
        Toast feedback = Toast.makeText(getApplicationContext(),"Message Added to Database", Toast.LENGTH_SHORT);
        feedback.setGravity(Gravity.TOP| Gravity.CENTER, 0,0);
        feedback.show();
    }

    private void retrieveRow(){

        try{
            ArrayList<Object> row;
            //retrieve row using db object
            row = db.getRow(Long.parseLong(updateIDField.getText().toString()));
            //from ArrayList put the text into the text field
            updateMessageField.setText((String)row.get(1));
        }
        catch(Exception e){
            Log.e("Retreive Error", e.toString());
            e.printStackTrace();
        }
    }

    private void updateRow(){

        try{
            db.updateRow(Long.parseLong(updateIDField.getText().toString()),updateMessageField.getText().toString());
            updateTable();
            emptyFormFields();
            Toast feedback = Toast.makeText(getApplicationContext(),"Message Updated in Database", Toast.LENGTH_SHORT);
            feedback.setGravity(Gravity.TOP| Gravity.CENTER, 0,0);
            feedback.show();

        }
        catch(Exception e){
            Log.e("Update Error", e.toString());
            e.printStackTrace();
        }
    }

    private void updateTable(){
        //delete all but the first row. 'count' starts at 1 and index starts at 0.
        while (dataTable.getChildCount()>1){ //count
            //while there are least two rows, delete the second
            dataTable.removeViewAt(1); //index to remove leaving 0,2...
        }
        //put all data into 2-d Array
        ArrayList<ArrayList<Object>> dataArray = db.getAllRows();

        for (int i=0; i<dataArray.size(); i++){
            TableRow tr = new TableRow(this);

            ArrayList<Object> row = dataArray.get(i);

            TextView idText = new TextView(this);
            idText.setText(row.get(0).toString());
            tr.addView(idText);

            TextView message = new TextView(this);
            message.setText(row.get(1).toString());
            tr.addView(message);

            dataTable.addView(tr);
        }

    }

    private void emptyFormFields(){

        messageField.setText("");
        updateIDField.setText("");
        updateMessageField.setText("");
    }



    /**
     * Create a new dummy account for the sync adapter
     *
     * at param context The application context
     */
    public static Account CreateSyncAccount(Context context) {
        // Create the account type and default account
        Account newAccount = new Account(
                ACCOUNT, ACCOUNT_TYPE);
        // Get an instance of the Android account manager
        AccountManager accountManager =
                (AccountManager) context.getSystemService(
                        ACCOUNT_SERVICE);
        /*
         * Add the account and account type, no password or user data
         * If successful, return the Account object, otherwise report an error.
         */
        if (accountManager.addAccountExplicitly(newAccount, null, null)) {
            /*
             * If you don't set android:syncable="true" in
             * in your <provider> element in the manifest,
             * then call context.setIsSyncable(account, AUTHORITY, 1)
             * here.
             */

        } else {
            /*
             * The account exists or some other error occurred. Log this, report it,
             * or handle it internally.
             */
        }
        return newAccount;
    }


    /*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        //return true;    --  replaced by:
        return super.onCreateOptionsMenu(menu);
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

    //method defined for xml attribute of button-onClick
    public void sendMessage(View view) {
        Intent intent = new Intent(this, DisplayMessageActivity.class);
        EditText editText = (EditText) findViewById(R.id.edit_message);
        String message = editText.getText().toString();
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }
    */
}
