package vn.com.it.truongpham.appnote;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;


import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import vn.com.it.truongpham.appnote.data.Book;
import vn.com.it.truongpham.appnote.view.ShowToast;

public class DetailNoteActivity extends AppCompatActivity {
    EditText edTitle, edContent;
    Book book;
    InterstitialAd mInterstitialAd;
    protected TextView tvTime, tvDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        edTitle = findViewById(R.id.edTitle);
        edContent = findViewById(R.id.edContent);
        tvDate=findViewById(R.id.tvDataDate);
        tvTime=findViewById(R.id.tvDataTime);


        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat time = new SimpleDateFormat("HH:mm aa");



        tvDate.setText(date.format(new Date()));
        tvTime.setText(time.format(new Date()));


        dateTime();




        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.id_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        book = (Book) getIntent().getSerializableExtra("note");
        if (book != null) {
            edTitle.setText(book.chapter);
            edContent.setText(book.content);
            tvTime.setText(book.time);
            tvDate.setText(book.date);
        }

        TextView tvTitle = findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.add_infomation));

        Toolbar toolbar = findViewById(R.id.toolbar);


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        findViewById(R.id.imgRight).setVisibility(View.VISIBLE);
        findViewById(R.id.imgRight).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int id = getIntent().getIntExtra("id", 0);
                Book bookAdd = new Book();
                String title = edTitle.getText().toString();
                String content = edContent.getText().toString();
                if (content.length() == 0) {
                    ShowToast.showToast(DetailNoteActivity.this, R.layout.show_toast_error, getString(R.string.add_infomation));
                    return;
                }

                if (book != null) {
                    ApplicationNote.db.bookDAO().update(title, content, book.id);
                    edTitle.requestFocus();
                    edTitle.setText("");
                    edContent.setText("");

                    Intent intent=new Intent(DetailNoteActivity.this,ListNoteActivity.class);
                    intent.putExtra("id",book.id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    finish();
                } else {
                    bookAdd.chapter = title;
                    bookAdd.content = content;
                    bookAdd.date=tvDate.getText().toString();
                    bookAdd.time =tvTime.getText().toString();
                    bookAdd.id_type_book = id;
                    ApplicationNote.db.bookDAO().insertBook(bookAdd);
                    edTitle.requestFocus();
                    edTitle.setText("");
                    edContent.setText("");

                    Intent intent=new Intent(DetailNoteActivity.this,ListNoteActivity.class);
                    intent.putExtra("id",id);
                    startActivity(intent);
                    overridePendingTransition(R.anim.left_in, R.anim.right_out);
                    finish();


                }
            }
        });

    }

    private void dateTime() {
        tvDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerFragment fragment=new DatePickerFragment(tvDate);
                fragment.show(getSupportFragmentManager(),"Select date");
            }
        });

        tvTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               TimePicker timePicker=new TimePicker(tvTime);
               timePicker.show(getSupportFragmentManager(),"Select time");
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.right_in, R.anim.left_out);
        finish();
        if (mInterstitialAd.isLoaded()) {
            mInterstitialAd.show();
        }

    }

    public static class TimePicker extends DialogFragment implements TimePickerDialog.OnTimeSetListener{
        TextView time;
        public TimePicker(TextView textView) {
            time=textView;
        }

        @NonNull
        @Override
        public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            return new TimePickerDialog(getActivity(), this, hour, minute, DateFormat.is24HourFormat(getActivity()));
        }

        @Override
        public void onTimeSet(android.widget.TimePicker view, int hourOfDay, int minute) {
             time.setText(String.valueOf(hourOfDay) + ":" + String.valueOf(minute));
        }
    }

    public static class DatePickerFragment extends DialogFragment implements DatePickerDialog.OnDateSetListener {

        TextView date;
        public DatePickerFragment(TextView textView) {
            date=textView;
        }
        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            return new DatePickerDialog(getActivity(), this, year, month, day);
        }
        public void onDateSet(DatePicker view, int year, int month, int day) {
            date.setText( String.valueOf(day) + " / " + String.valueOf(month) + "/" + String.valueOf(year));
        }
    }

}
