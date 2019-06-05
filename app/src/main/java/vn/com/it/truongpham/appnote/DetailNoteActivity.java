package vn.com.it.truongpham.appnote;

import android.app.Dialog;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import io.reactivex.Single;
import vn.com.it.truongpham.appnote.data.Book;
import vn.com.it.truongpham.appnote.data.TypeBook;
import vn.com.it.truongpham.appnote.view.ShowToast;

public class DetailNoteActivity extends AppCompatActivity {
    EditText edTitle, edContent;
    Book book;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        edTitle = findViewById(R.id.edTitle);
        edContent = findViewById(R.id.edContent);

        book= (Book) getIntent().getSerializableExtra("note");
        if(book!=null){
            edTitle.setText(book.chapter);
            edContent.setText(book.content);
        }


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_add) {
            int id = getIntent().getIntExtra("id", 0);
            Book bookAdd = new Book();
            String title = edTitle.getText().toString();
            String content = edContent.getText().toString();
            if(content.length()==0){
                Toast.makeText(this, "Nhap noi dung", Toast.LENGTH_SHORT).show();
                return true;
            }

            if(book!=null){
                ApplicationNote.db.bookDAO().update(title,content,book.id);
                Toast.makeText(this, "Sua thanh cong", Toast.LENGTH_SHORT).show();
                edTitle.requestFocus();
                edTitle.setText("");
                edContent.setText("");
            }else {
                bookAdd.chapter = title;
                bookAdd.content = content;
                bookAdd.id_type_book = id ;
                ApplicationNote.db.bookDAO().insertBook(bookAdd);
                Toast.makeText(this, "Them thanh cong", Toast.LENGTH_SHORT).show();
                edTitle.requestFocus();
                edTitle.setText("");
                edContent.setText("");


            }
        }
        return super.onOptionsItemSelected(item);
    }
}
