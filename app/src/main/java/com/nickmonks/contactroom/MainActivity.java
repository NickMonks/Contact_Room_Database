package com.nickmonks.contactroom;

import androidx.annotation.LongDef;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.nickmonks.contactroom.adapter.RecyclerViewAdapter;
import com.nickmonks.contactroom.model.Contact;
import com.nickmonks.contactroom.model.ContactViewModel;

import java.util.List;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.onContactClickListener{
    public static final String TAG = "Clicked";
    public static final String CONTACT_ID = "contact_id";
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1;
    private ContactViewModel contactViewModel;
    private TextView textView;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private LiveData<List<Contact>> contactList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // create Recycler view:
        recyclerView = findViewById(R.id.recycler_view);
        // how to layout our recyclerView on the screen
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // we need to use this viewModel provider, otherwise we will get an error
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                                                                        .getApplication())
                                                                        .create(ContactViewModel.class);

        contactViewModel.getAllContacts().observe(this, contacts -> {

            //Set up adapter
            // When we pass the context to the recycleAdapter, it will know that implements the click event
            recyclerViewAdapter = new RecyclerViewAdapter(contacts,MainActivity.this, this);
            recyclerView.setAdapter(recyclerViewAdapter);

        });





        FloatingActionButton fab = findViewById(R.id.add_contact_fab);
        fab.setOnClickListener(v -> {
            // This takes us back to "NewContact" Activity
            Intent intent = new Intent(MainActivity.this, NewContact.class);
            startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);
        });
    }

    // Invoked whenever we return from activity

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {
            Contact contact = new Contact(data.getStringExtra(NewContact.NAME_REPLY),
                                            data.getStringExtra(NewContact.OCCUPATION_REPLY));

            ContactViewModel.insert(contact);

            Log.d("TAG", "onActivityResult: " + data.getStringExtra(NewContact.NAME_REPLY));
            Log.d("TAG", "onActivityResult: " + data.getStringExtra(NewContact.OCCUPATION_REPLY));
        }
    }

    // we implement this interface to allow the developer to directly FROM THE ADAPTER implement the click event
    @Override
    public void onContactClick(int Position) {
        Contact contact = contactViewModel.allContacts.getValue().get(Position);
        Log.d(TAG, "onContactClick: " + contact.getName());

        Intent intent = new Intent(MainActivity.this, NewContact.class);
        intent.putExtra(CONTACT_ID, contact.getId());
        startActivity(intent); // start activity on the NewCOntact Activity
    }
}