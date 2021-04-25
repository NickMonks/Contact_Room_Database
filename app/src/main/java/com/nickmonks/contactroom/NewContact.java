package com.nickmonks.contactroom;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;
import com.nickmonks.contactroom.model.Contact;
import com.nickmonks.contactroom.model.ContactViewModel;

public class NewContact extends AppCompatActivity {

    public static final String NAME_REPLY = "name_reply";
    public static final String OCCUPATION_REPLY = "occupation_reply";
    private EditText enterName;
    private EditText enterOccupation;
    private Button saveButton;
    private int contactID = 0;
    private boolean isEdit = false; // will track whether we want to edit a contact or simply add a new one

    // ViewModel
    private ContactViewModel contactViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_contact);

        enterName = findViewById(R.id.enter_name);
        enterOccupation = findViewById(R.id.enter_occupation);
        saveButton = findViewById(R.id.save_button);

        // we need to use this viewModel provider, otherwise we will get an error
        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(NewContact.this
                .getApplication())
                .create(ContactViewModel.class);


        // Whenever we click one of the ViewHolder, it will start NewContact Activity,
        // get the id (since an observer is triggered) and set the name and occupation
        if (getIntent().hasExtra(MainActivity.CONTACT_ID)){

            contactID = getIntent().getIntExtra(MainActivity.CONTACT_ID, 0);
            contactViewModel.get(contactID).observe(this, new Observer<Contact>() {
                @Override
                public void onChanged(Contact contact) {
                    if (contact != null){
                        enterName.setText(contact.getName());
                        enterOccupation.setText(contact.getOccupation());
                    }
                }
            });

            // If we get data, we hide the save button
            isEdit = true;
        }


        // -------- SAVE BUTTON ------------------------------------------
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // Intent is a mechanism to pass data around - is an action
                Intent replyIntent = new Intent();

                if (!TextUtils.isEmpty(enterName.getText().toString()) &&
                        !TextUtils.isEmpty(enterOccupation.getText().toString()))
                {

                    String name = enterName.getText().toString();
                    String occupation = enterOccupation.getText().toString();

                    // Pass data to intent

                    replyIntent.putExtra(NAME_REPLY, name);
                    replyIntent.putExtra(OCCUPATION_REPLY, occupation);
                    setResult(RESULT_OK, replyIntent);


                    //ContactViewModel.insert(contact);

                } else {
                    setResult(RESULT_CANCELED, replyIntent);
                }

                // get rid of the activity to not stack activities
                finish();

            }
        });

        // -------- UPDATE BUTTON ------------------------------------------
        Button updateButton = findViewById(R.id.update_button);

        updateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = contactID;
                String name = enterName.getText().toString().trim();
                String occupation = enterOccupation.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(occupation)){
                    Snackbar.make(enterName, R.string.empty, Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Contact contact = new Contact();
                    contact.setId(id);
                    contact.setName(name);
                    contact.setOccupation(occupation);

                    // use ViewModel to update

                    ContactViewModel.update(contact);

                    // finish the activity
                    finish();
                }
            }
        });

        Button deleteButton = findViewById(R.id.delete_button);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = contactID;
                String name = enterName.getText().toString().trim();
                String occupation = enterOccupation.getText().toString().trim();

                if (TextUtils.isEmpty(name) || TextUtils.isEmpty(occupation)){
                    Snackbar.make(enterName, R.string.empty, Snackbar.LENGTH_SHORT)
                            .show();
                } else {
                    Contact contact = new Contact();
                    contact.setId(id);
                    contact.setName(name);
                    contact.setOccupation(occupation);

                    // use ViewModel to update

                    ContactViewModel.delete(contact);

                    // finish the activity
                    finish();
                }
            }
        });

        if (isEdit) {
            saveButton.setVisibility(View.GONE);

        }else{
            updateButton.setVisibility(View.GONE);
            deleteButton.setVisibility(View.GONE);
        }


    }
}