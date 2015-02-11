package com.mycompany.stevesapp;

import android.accounts.Account;
import android.content.AbstractThreadedSyncAdapter;
import android.content.ContentProviderClient;
import android.content.ContentResolver;
import android.content.Context;
import android.content.SyncResult;
import android.os.Bundle;

/**
 * Handle the transfer of data between a server and an
 * app, using the Android sync adapter framework.
 */
public class SyncAdapter extends AbstractThreadedSyncAdapter {

    // Global variables
    // Define a variable to contain a content resolver instance
    ContentResolver mContentResolver;

    /**
     * Set up the sync adapter
     */
    public SyncAdapter(Context context, boolean autoInitialize) {
        super(context, autoInitialize);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();
    }

    /**
     * Set up the sync adapter. This form of the
     * constructor maintains compatibility with Android 3.0
     * and later platform versions
     */
    public SyncAdapter(
            Context context,
            boolean autoInitialize,
            boolean allowParallelSyncs) {
        super(context, autoInitialize, allowParallelSyncs);
        /*
         * If your app uses a content resolver, get an instance of it
         * from the incoming Context
         */
        mContentResolver = context.getContentResolver();

    }

    /*
     * Specify the code you want to run in the sync adapter. The entire
     * sync adapter runs in a background thread, so you don't have to set
     * up your own background processing.
     */
    @Override
    public void onPerformSync(
            Account account,
            Bundle extras,
            String authority,
            ContentProviderClient provider,
            SyncResult syncResult) {
    /*
     * Put the data transfer code here. TO DO !!!!!!!!!!
     */
        // 1. Establish Connection:
        //look at how connection to Postgres Server done - manually & via local java program (SD2).
        //also look at socket programming exercise in CCN.

        // 2. Transfer data:
        /*
         * Access SQLite database through MyDatabaseManager Class. (Need a flag field for data pulled)
          * and get a cursor object of all un-flagged data. (Might need a second flag-type for amended data.)
         * Access remote database with an INSERT INTO ... VALUES SQL Query.
         * Pull amended data in second query and do UPDATE, but then need matching unique identifier (to know what to update).
         * If data can be deleted, it would need flags 3 and 4. Flag 3 as to-delete. Pull this and delete on main database, changing flag to 4, then separate SQLite query to delete all 4s.
         */


     /*
     * .... NOTES:
     * While the actual implementation of onPerformSync() is specific to your app's data synchronization
     * requirements and server connection protocols, there are a few general tasks your implementation should perform:
     *
     * Connecting to a server:
     * Although you can assume that the network is available when your data transfer starts,
     * the sync adapter framework doesn't automatically connect to a server.
     *
     * Downloading and uploading data:
     * A sync adapter doesn't automate any data transfer tasks.
     * If you want to download data from a server and store it in a content provider,
     * you have to provide the code that requests the data, downloads it, and inserts it in the provider.
     * Similarly, if you want to send data to a server, you have to read it from a file, database,
     * or provider, and send the necessary upload request.
     * You also have to handle network errors that occur while your data transfer is running.
     *
     * Handling data conflicts or determining how current the data is:
     * A sync adapter doesn't automatically handle conflicts between data on the server and data on the device.
     * Also, it doesn't automatically detect if the data on the server is newer than the data on the device,
     * or vice versa. Instead, you have to provide your own algorithms for handling this situation.
     *
     * Clean up:
     * Always close connections to a server and clean up temp files and caches at the end of your data transfer.
     * Note: The sync adapter framework runs onPerformSync() on a background thread,
     * so you don't have to set up your own background processing.
     */

    }
}
