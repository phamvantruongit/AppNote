package vn.com.it.truongpham.appnote;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import vn.com.it.truongpham.appnote.adapter.AdapterListNote;
import vn.com.it.truongpham.appnote.data.Book;

public class ListNoteActivity extends AppCompatActivity {
   RecyclerView recyclerView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_note);
        recyclerView=findViewById(R.id.rvbook);
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        Intent intent=getIntent();
        final int id=intent.getIntExtra("id",0);
        getData(id);

        final EditText edSearch= findViewById(R.id.edSearch);
        edSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                  if(edSearch.getText().toString().length()>0){
                      String name=edSearch.getText().toString();
                      ApplicationNote.db.bookDAO().findByName("%"+name+"%","%"+name+"%")
                      .observe(ListNoteActivity.this, new Observer<List<Book>>() {
                          @Override
                          public void onChanged(List<Book> books) {
                              AdapterListNote adapterListNote=new AdapterListNote(books);
                              recyclerView.setAdapter(adapterListNote);
                          }
                      });

                      ;
                  }else {
                      getData(id);
                  }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

    }

    private void getData(int id) {
        LiveData<List<Book>> listLiveData = ApplicationNote.db.bookDAO().getListBook(id);
        listLiveData.observe(this, new Observer<List<Book>>() {
            @Override
            public void onChanged(@Nullable List<Book> books) {
                AdapterListNote adapterListNote=new AdapterListNote(books);
                recyclerView.setAdapter(adapterListNote);
            }
        });
    }
}
