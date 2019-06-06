package vn.com.it.truongpham.appnote;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import vn.com.it.truongpham.appnote.data.Book;
import vn.com.it.truongpham.appnote.view.ShowToast;

public class DetailNoteActivity extends AppCompatActivity {
    EditText edTitle, edContent;
    Book book;
    InterstitialAd mInterstitialAd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_note);
        edTitle = findViewById(R.id.edTitle);
        edContent = findViewById(R.id.edContent);

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.id_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        book = (Book) getIntent().getSerializableExtra("note");
        if (book != null) {
            edTitle.setText(book.chapter);
            edContent.setText(book.content);
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
                    Toast.makeText(DetailNoteActivity.this, getString(R.string.edit_successfully), Toast.LENGTH_SHORT).show();
                    edTitle.requestFocus();
                    edTitle.setText("");
                    edContent.setText("");
                } else {
                    bookAdd.chapter = title;
                    bookAdd.content = content;
                    bookAdd.id_type_book = id;
                    ApplicationNote.db.bookDAO().insertBook(bookAdd);
                    Toast.makeText(DetailNoteActivity.this, getString(R.string.add_successfully), Toast.LENGTH_SHORT).show();
                    edTitle.requestFocus();
                    edTitle.setText("");
                    edContent.setText("");


                }
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

}
