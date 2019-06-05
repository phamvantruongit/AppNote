package vn.com.it.truongpham.appnote;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

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
        int id=intent.getIntExtra("id",0);
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
