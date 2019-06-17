package vn.com.it.truongpham.appnote;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import android.content.Intent;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;

import java.util.List;

import vn.com.it.truongpham.appnote.adapter.AdapterListNote;
import vn.com.it.truongpham.appnote.data.Book;

public class ListNoteActivity extends AppCompatActivity implements AdapterListNote.IOnClick {
   RecyclerView recyclerView;
    InterstitialAd mInterstitialAd;
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
        Toolbar toolbar=findViewById(R.id.toolbar);




        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.id_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());


        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        TextView tvTitle=findViewById(R.id.tvTitle);
        tvTitle.setText(getString(R.string.list_note));

        findViewById(R.id.imgRight).setVisibility(View.GONE);

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
                              AdapterListNote adapterListNote=new AdapterListNote(books,ListNoteActivity.this);
                              recyclerView.setAdapter(adapterListNote);
                          }
                      });

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
                AdapterListNote adapterListNote=new AdapterListNote(books,ListNoteActivity.this);
                recyclerView.setAdapter(adapterListNote);
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

    @Override
    public void ionClick() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }
}
