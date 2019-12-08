package com.example.danhba;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SearchView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements SearchView.OnQueryTextListener {
    private FloatingActionButton fba_add;
    private ListView lvContact;
    ArrayList<Contact> arrayList;
    MyDatabase db;
    EditText edtSearch;
    SearchView searchView;
    CustomAdapter customAdapter;
    Contact contact;
    int local;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchView = findViewById(R.id.search_bar);
        fba_add = findViewById(R.id.fab_add);
        lvContact = findViewById(R.id.lv_contact);
        edtSearch = findViewById(R.id.edt_search);

        db = new MyDatabase(this);
        arrayList = new ArrayList<Contact>();

        arrayList = db.getAllContact();
        customAdapter = new CustomAdapter(this, R.layout.activity_row_listview, arrayList);
        lvContact.setAdapter(customAdapter);
        lvContact.deferNotifyDataSetChanged();

        lvContact.setTextFilterEnabled(true);
        setupSearchView();
        lvContact.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                local = position;
                EditContact();
            }
        });
        fba_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, AddContactActivity.class);
                startActivityForResult(intent, 123);
            }
        });
        //edt_Search =  findViewById(R.id.edt_Search);
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                arrayList = db.search(edtSearch.getText().toString());
                lvContact.setAdapter(new CustomAdapter(MainActivity.this,R.layout.activity_row_listview,arrayList));
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private void setupSearchView() {
        searchView.setIconifiedByDefault(false);
        searchView.setOnQueryTextListener(this);
        searchView.setSubmitButtonEnabled(true);
        searchView.setQueryHint("Search Here");
    }

    private void EditContact() {
        Intent intent = new Intent(MainActivity.this, EditContactActivity.class);
        Bundle bundle = new Bundle();
        bundle.putSerializable("contact", arrayList.get(local));
        intent.putExtra("package", bundle);
        startActivityForResult(intent, 456);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 123 && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra("package");
            contact = (Contact) bundle.getSerializable("contact");
            arrayList.add(contact);
            db.addContact(contact);
            db.getAllContact();
            customAdapter.notifyDataSetChanged();
        }
        if (requestCode == 456 && resultCode == RESULT_OK) {
            Bundle bundle = data.getBundleExtra("package");
            Contact contact = (Contact) bundle.getSerializable("contact");
            contact.setId(local + 1);
            db.updateContact(contact);
            db.getAllContact();
            arrayList.set(local, contact);
            customAdapter.notifyDataSetChanged();
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (TextUtils.isEmpty(newText.toString())) {
                    lvContact.clearTextFilter();
                } else {
                    lvContact.setFilterText(newText.toString());
                }
                return true;
    }
}