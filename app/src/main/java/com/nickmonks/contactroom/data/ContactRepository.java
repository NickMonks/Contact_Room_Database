package com.nickmonks.contactroom.data;


import android.app.Application;

import androidx.lifecycle.LiveData;

import com.nickmonks.contactroom.model.Contact;
import com.nickmonks.contactroom.utils.ContactRoomDatabase;

import java.util.List;

public class ContactRepository {
    private ContactDAO contactDao;
    private LiveData<List<Contact>> allContacts;

    // for the constructor, we need to pass the context
    public ContactRepository(Application application){
        ContactRoomDatabase db = ContactRoomDatabase.getDatabase(application);


        // we use the DAO only inside the repositoru=y
        contactDao = db.contactDao();

        allContacts = contactDao.getAllContacts();
    }

    public LiveData<List<Contact>> getAllData() { return allContacts;}

    public void insert(Contact contact){

        // we pass a runnable as a lambda (functional interface)
        ContactRoomDatabase.databaseWriteExecutor.execute( () -> {
            contactDao.insert(contact);
        });
    }

    public LiveData<Contact> get(int id){
        return contactDao.get(id);
    }

    public void update(Contact contact){
        ContactRoomDatabase.databaseWriteExecutor.execute(()-> contactDao.update(contact));
    }

    public void delete(Contact contact) {
        ContactRoomDatabase.databaseWriteExecutor.execute(()-> contactDao.delete(contact));
    }
}
