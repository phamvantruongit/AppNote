package vn.com.it.truongpham.appnote;

import android.app.Dialog;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;


import java.util.List;

import vn.com.it.truongpham.appnote.adapter.AdapterTypeBook;
import vn.com.it.truongpham.appnote.data.TypeBook;
import vn.com.it.truongpham.appnote.view.ShowToast;

public class NoteBookActivity extends AppCompatActivity {


    RecyclerView rvBook;
    AdapterTypeBook adapterTypeBook;
    RecyclerView.LayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_book);

        rvBook = findViewById(R.id.rvbook);


        getData();

        final EditText edSeach=findViewById(R.id.edSearch);
        edSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                 if(edSeach.getText().toString().length()>0){
                     String name=edSeach.getText().toString();
                     ApplicationNote.db.typeBookDAO().findByName("%"+name+"%")
                             .observe(NoteBookActivity.this, new Observer<List<TypeBook>>() {
                                 @Override
                                 public void onChanged(List<TypeBook> typeBooks) {
                                     layoutManager = new LinearLayoutManager(NoteBookActivity.this);
                                     adapterTypeBook = new AdapterTypeBook(NoteBookActivity.this, typeBooks);
                                     rvBook.setLayoutManager(layoutManager);
                                     rvBook.setAdapter(adapterTypeBook);
                                 }
                             });
                 }else {
                     getData();
                 }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_add) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.show_popup_type_book);
            dialog.setCancelable(false);
            dialog.show();
            TextView tvOK = dialog.findViewById(R.id.tvOK);
            final EditText edTypeBook = dialog.findViewById(R.id.edTypeBook);
            tvOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = edTypeBook.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        ShowToast.showToast(NoteBookActivity.this, R.layout.show_toast_error);
                        return;
                    }
                    TypeBook typeBook = new TypeBook();
                    typeBook.name = text;
                    ApplicationNote.db.typeBookDAO().insertTypeBook(typeBook);
                    dialog.dismiss();

                }
            });
            TextView tvCancel = dialog.findViewById(R.id.tvCancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        LiveData<List<TypeBook>> listLiveData = ApplicationNote.db.typeBookDAO().getListTypeBook();
        listLiveData.observe(this, new Observer<List<TypeBook>>() {
            @Override
            public void onChanged(@Nullable List<TypeBook> typeBooks) {
                layoutManager = new LinearLayoutManager(NoteBookActivity.this);
                adapterTypeBook = new AdapterTypeBook(NoteBookActivity.this, typeBooks);
                rvBook.setLayoutManager(layoutManager);
                rvBook.setAdapter(adapterTypeBook);
            }
        });

    }

}