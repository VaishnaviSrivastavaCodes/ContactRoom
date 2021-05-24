package com.example.contactroom;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.contactroom.adapter.RecyclerViewAdapter;
import com.example.contactroom.model.Contact;
import com.example.contactroom.model.ContactViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class MainActivity extends AppCompatActivity implements RecyclerViewAdapter.OnContactClickListener {

    private ContactViewModel contactViewModel;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private RecyclerViewAdapter recyclerViewAdapter;
    private List<Contact> contactList;
    private static final int NEW_CONTACT_ACTIVITY_REQUEST_CODE = 1 ; //any number you want
    public static final String CONTACT_ID =   "contact_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        contactViewModel = new ViewModelProvider.AndroidViewModelFactory(MainActivity.this
                .getApplication())
                .create(ContactViewModel.class);


        contactViewModel.getAllContacts().observe(this, contacts -> {

            recyclerViewAdapter = new RecyclerViewAdapter(contacts, MainActivity.this, this);
            recyclerView.setAdapter(recyclerViewAdapter);

        });


        fab = findViewById(R.id.add_contact_fab);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(MainActivity.this, NewContact.class);
            startActivityForResult(intent, NEW_CONTACT_ACTIVITY_REQUEST_CODE);

        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == NEW_CONTACT_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            assert data != null;
            String name = data.getStringExtra(NewContact.NAME_REPLY);
            String occupation = data.getStringExtra(NewContact.OCCUPATION_REPLY);

            assert name != null;
            Contact contact = new Contact(name, occupation);

            ContactViewModel.insert(contact);
        }
    }

    @Override
    public void onContactClick(int position) {

        Contact contact = Objects.requireNonNull(contactViewModel.allContacts.getValue().get(position));
//        Log.d("Clicked", "onContactClick: "+ contact.getName());

        Intent intent = new Intent(MainActivity.this, NewContact.class);
        intent.putExtra(CONTACT_ID,contact.getId());
        startActivity(intent);
    }


}



