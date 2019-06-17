package vn.com.it.truongpham.appnote;

import android.app.DatePickerDialog;
import android.app.Dialog;

import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import vn.com.it.truongpham.appnote.adapter.AdapterTypeBook;
import vn.com.it.truongpham.appnote.data.TypeBook;

public class NoteBookActivity extends AppCompatActivity implements AdapterTypeBook.IOnClick {


    RecyclerView rvBook;
    AdapterTypeBook adapterTypeBook;
    RecyclerView.LayoutManager layoutManager;
    EditText edSeach;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note_book);

        rvBook = findViewById(R.id.rvbook);
        edSeach =findViewById(R.id.edSearch);

        AdView mAdView = findViewById(R.id.adViews);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
        mAdView.loadAd(adRequest);


        getData();

        getSearchName();
    }

    private void getSearchName() {
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
                                    adapterTypeBook = new AdapterTypeBook(NoteBookActivity.this, typeBooks,NoteBookActivity.this);
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

    private void getSearchDate() {
        edSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(edSeach.getText().toString().length()>0){
                    String name=edSeach.getText().toString();
                    ApplicationNote.db.typeBookDAO().findByDate("%"+name+"%")
                            .observe(NoteBookActivity.this, new Observer<List<TypeBook>>() {
                                @Override
                                public void onChanged(List<TypeBook> typeBooks) {
                                    layoutManager = new LinearLayoutManager(NoteBookActivity.this);
                                    adapterTypeBook = new AdapterTypeBook(NoteBookActivity.this, typeBooks,NoteBookActivity.this);
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
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.item_add) {
            final Dialog dialog = new Dialog(this);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.show_popup_type_book);
            dialog.show();
            if (dialog.getWindow() != null) {
                dialog.getWindow().setGravity(Gravity.CENTER);
                dialog.getWindow().setLayout(
                        WindowManager.LayoutParams.MATCH_PARENT,
                        WindowManager.LayoutParams.WRAP_CONTENT
                );
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
            }
            TextView tvOK = dialog.findViewById(R.id.tvOK);
            final EditText edTypeBook = dialog.findViewById(R.id.edTypeBook);
            tvOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text = edTypeBook.getText().toString();
                    if (TextUtils.isEmpty(text)) {
                        edTypeBook.setError(getString(R.string.enter_info));
                        return;
                    }
                    SimpleDateFormat time = new SimpleDateFormat("HH:mm aa");
                    SimpleDateFormat date = new SimpleDateFormat("dd/M/yyyy");


                    TypeBook typeBook = new TypeBook();
                    typeBook.name = text;
                    typeBook.date=date.format(new Date());
                    typeBook.time= time.format(new Date());
                    ApplicationNote.db.typeBookDAO().insertTypeBook(typeBook);
                    dialog.dismiss();

                }
            });
            dialog.findViewById(R.id.tvExit).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });

        }

        if(item.getItemId()==R.id.item_search){
            DatePickerFragment fragment=new DatePickerFragment(edSeach);
            fragment.show(getSupportFragmentManager(),"Select date");
            getSearchDate();
        }
        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        LiveData<List<TypeBook>> listLiveData = ApplicationNote.db.typeBookDAO().getListTypeBook();
        listLiveData.observe(this, new Observer<List<TypeBook>>() {
            @Override
            public void onChanged(@Nullable List<TypeBook> typeBooks) {
                layoutManager = new LinearLayoutManager(NoteBookActivity.this);
                adapterTypeBook = new AdapterTypeBook(NoteBookActivity.this, typeBooks,NoteBookActivity.this);
                rvBook.setLayoutManager(layoutManager);
                rvBook.setAdapter(adapterTypeBook);
            }
        });

    }

    @Override
    public void ionClick() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        EditText date;
        public DatePickerFragment(EditText textView) {

            date=textView;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH)+1;
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            date.setText( String.valueOf(day) + "/" + String.valueOf(month) + "/" + String.valueOf(year));
        }
    }
}
