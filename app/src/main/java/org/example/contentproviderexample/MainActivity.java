package org.example.contentproviderexample;

import android.Manifest;
import android.content.ContentResolver;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final int REQUEST_CODE_READ_CONTACTS = 0;
    private static boolean READ_CONTACTS_GRANTED = false;

    private ListView contactsListView;
    private FloatingActionButton fab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        contactsListView = findViewById(R.id.contacts_list_view);

        int hasReadContactsPermission = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);
        Log.d(TAG, "onCreate: checkSelfPermission : " + hasReadContactsPermission);

        if(hasReadContactsPermission == PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "onCreate: Permission granted");
            READ_CONTACTS_GRANTED = true;
        } else{
            Log.d(TAG, "onCreate: Requesting permissions");
            ActivityCompat.requestPermissions(this, new String[] {Manifest.permission.READ_CONTACTS}, REQUEST_CODE_READ_CONTACTS);
            //After interacting with user, android returns result in onRequestPermissionsResult method
        }
        
        fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(READ_CONTACTS_GRANTED) {
                    String[] projection = {ContactsContract.Contacts.DISPLAY_NAME_PRIMARY};
                    ContentResolver contentResolver = getContentResolver();
                    Cursor cursor = contentResolver.query(ContactsContract.Contacts.CONTENT_URI, projection, null, null, null);

                    if (cursor != null) {
                        List<String> contacts = new ArrayList<>();
                        while (cursor.moveToNext()) {
                            contacts.add(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)));
                        }
                        cursor.close();

                        ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.list_view_element, R.id.contacts_text_view, contacts);
                        contactsListView.setAdapter(adapter);

                    }
                }
                else{
                    Snackbar.make(view, "Permission has not been provided", Snackbar.LENGTH_LONG).show();
                }
            }
        });

        Log.d(TAG, "onCreate: ends");
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        Log.d(TAG, "onRequestPermissionsResult: starts");

        switch (requestCode){
            case REQUEST_CODE_READ_CONTACTS: {
                //This means that we are checking for permission related to accessing contacts
                //If permission is denied, grantResults array is empty
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // Permission is granted. Continue the action or workflow in your app.
                    Log.d(TAG, "onRequestPermissionsResult: permission granted!");
                    READ_CONTACTS_GRANTED = true;
                } else {
                    Log.d(TAG, "onRequestPermissionsResult: permission denied");
                    //Permission denied
                    // Explain to the user that the feature is unavailable because
                    // the features requires a permission that the user has denied.
                }

            }
        }

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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}