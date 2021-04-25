package com.nickmonks.contactroom.model;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.nickmonks.contactroom.data.ContactRepository;

import java.util.List;

// The viewModel is responsible of holding UI data so it survives to configuraton changes
// (i.e, rotation of the device), and use Livedata to notify/publish changes in the backend of the application

// This is where we invoke the methods from the repository class
public class ContactViewModel extends AndroidViewModel {

    public static ContactRepository repository;
    public final LiveData<List<Contact>> allContacts;

    public ContactViewModel(@NonNull Application application) {
        super(application);
        repository = new ContactRepository(application);
        allContacts = repository.getAllData();
    }

    // You can use the methods as static, since once the class is being instantiated once it will hold the same repository for the class,

    public LiveData<List<Contact>> getAllContacts() {
        return allContacts;
    }

    public static void insert(Contact contact) { repository.insert(contact);}

    public LiveData<Contact> get(int id){return repository.get(id);}

    public static void update(Contact contact) {repository.update(contact);}

    public static void delete(Contact contact) { repository.delete(contact);}


}
