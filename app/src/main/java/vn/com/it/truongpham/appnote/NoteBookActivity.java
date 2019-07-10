package vn.com.it.truongpham.appnote;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.app.Dialog;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ShareCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.DialogFragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Parcelable;
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
import android.widget.Toast;


import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.InterstitialAd;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import vn.com.it.truongpham.appnote.adapter.AdapterTypeBook;
import vn.com.it.truongpham.appnote.data.TypeBook;

public class NoteBookActivity extends AppCompatActivity implements AdapterTypeBook.IOnClick {


    RecyclerView rvBook;
    AdapterTypeBook adapterTypeBook;
    RecyclerView.LayoutManager layoutManager;
    EditText edSeach;
    SharedPreferences sharedPreferences;
    InterstitialAd mInterstitialAd;

    private DatabaseReference mDatabase;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDatabase=FirebaseDatabase.getInstance().getReference().child("UpdateApp").child("versionName");

        mInterstitialAd = new InterstitialAd(this);
        mInterstitialAd.setAdUnitId(getResources().getString(R.string.id_interstitial));
        mInterstitialAd.loadAd(new AdRequest.Builder().build());

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");


        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        sharedPreferences=getSharedPreferences("SaveLocal",MODE_PRIVATE);
        boolean checkList=sharedPreferences.getBoolean("check",false);
        if(!checkList){
            layoutManager=new LinearLayoutManager(this);
        }else {
            layoutManager = new GridLayoutManager(NoteBookActivity.this, 2);
        }


        rvBook = findViewById(R.id.rvbook);
        edSeach = findViewById(R.id.edSearch);

        AdView mAdView = findViewById(R.id.adViews);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        getData();
        init();

        getSearchName();

        boolean checkUpdate = sharedPreferences.getBoolean("checkUpdate",false);
        if(!checkUpdate) {
            mDatabase.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String versionNameUpdate = dataSnapshot.getValue(String.class);
                    if (!versionNameUpdate.equals(BuildConfig.VERSION_NAME)) {
                        updateApp();
                    }
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }

    }

    private void updateApp() {
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.putBoolean("checkUpdate",true).apply();
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setCancelable(false);
        dialog.setContentView(R.layout.show_popup_update);
        dialog.show();
        if (dialog.getWindow() != null) {
            dialog.getWindow().setGravity(Gravity.CENTER);
            dialog.getWindow().setLayout(
                    WindowManager.LayoutParams.MATCH_PARENT,
                    WindowManager.LayoutParams.WRAP_CONTENT
            );
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        }

        dialog.findViewById(R.id.tvLater).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        dialog.findViewById(R.id.tvAccept).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                openGooglePlay();
            }
        });

    }

    private void openGooglePlay(){
        try {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + BuildConfig.APPLICATION_ID)));
        } catch (android.content.ActivityNotFoundException anfe) {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID)));
        }
    }

    private void init() {
        findViewById(R.id.tvAddCategoty).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
                final Dialog dialog = new Dialog(view.getContext());
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
                        typeBook.date = date.format(new Date());
                        typeBook.time = time.format(new Date());
                        ApplicationNote.db.typeBookDAO().insertTypeBook(typeBook);
                        dialog.dismiss();

                    }
                });
                dialog.findViewById(R.id.tvExit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        dialog.cancel();
                    }
                });
            }

        });

        findViewById(R.id.tvSetPass).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
                Intent intent = new Intent(view.getContext(), LockScreenActivity.class);
                startActivity(intent);
            }
        });

        findViewById(R.id.tvNote).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
                openGooglePlay();
            }
        });

        findViewById(R.id.tvShare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
                share();
            }
        });

        findViewById(R.id.tvMoreApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                close();
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/developer?id=jspvtSoftware")));
            }
        });

        findViewById(R.id.tvUpdateApp).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                close();
                openGooglePlay();
            }
        });

    }

    private void share() {
        List<Intent> targetShareIntents = new ArrayList<Intent>();
        Intent shareIntent = new Intent();
        shareIntent.setAction(Intent.ACTION_SEND);
        shareIntent.setType("text/plain");
        PackageManager pm = getPackageManager();
        List<ResolveInfo> resInfos = pm.queryIntentActivities(shareIntent, 0);
        if (!resInfos.isEmpty()) {
            for (ResolveInfo resInfo : resInfos) {
                String packageName = resInfo.activityInfo.packageName;

                if (packageName.contains("com.twitter.android") || packageName.contains("com.facebook.katana")
                        || packageName.contains("com.whatsapp") || packageName.contains("com.facebook.orca")
                        || packageName.contains("com.instagram.android") || packageName.contains("com.skype.raider")
                        || packageName.contains("com.zing.zalo")
                        ) {
                    Intent intent = new Intent();

                    intent.setComponent(new ComponentName(packageName, resInfo.activityInfo.name));
                    intent.putExtra("AppName", resInfo.loadLabel(pm).toString());
                    intent.setAction(Intent.ACTION_SEND);
                    intent.setType("text/plain");
                    intent.putExtra(Intent.EXTRA_TEXT, "https://play.google.com/store/apps/details?id=" + BuildConfig.APPLICATION_ID);
                    intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.app_name));
                    intent.setPackage(packageName);
                    targetShareIntents.add(intent);
                }
            }
            if (!targetShareIntents.isEmpty()) {
                Collections.sort(targetShareIntents, new Comparator<Intent>() {
                    @Override
                    public int compare(Intent o1, Intent o2) {
                        return o1.getStringExtra("AppName").compareTo(o2.getStringExtra("AppName"));
                    }
                });
                Intent chooserIntent = Intent.createChooser(targetShareIntents.remove(0), "Select app to share");
                chooserIntent.putExtra(Intent.EXTRA_INITIAL_INTENTS, targetShareIntents.toArray(new Parcelable[]{}));
                startActivity(chooserIntent);
            } else {
                Toast.makeText(NoteBookActivity.this, "No app to share.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void close() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
    }

    private void getSearchName() {
        edSeach.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (edSeach.getText().toString().length() > 0) {
                    String name = edSeach.getText().toString();
                    ApplicationNote.db.typeBookDAO().findByName("%"+name+"%")
                            .observe(NoteBookActivity.this, new Observer<List<TypeBook>>() {
                                @Override
                                public void onChanged(List<TypeBook> typeBooks) {
                                    layoutManager = new LinearLayoutManager(NoteBookActivity.this);
                                    adapterTypeBook = new AdapterTypeBook(NoteBookActivity.this, typeBooks, NoteBookActivity.this);
                                    rvBook.setLayoutManager(layoutManager);
                                    rvBook.setAdapter(adapterTypeBook);
                                }
                            });
                } else {
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
                if (edSeach.getText().toString().length() > 0) {
                    String name = edSeach.getText().toString();
                    ApplicationNote.db.typeBookDAO().findByDate("%"+name+"%")
                            .observe(NoteBookActivity.this, new Observer<List<TypeBook>>() {
                                @Override
                                public void onChanged(List<TypeBook> typeBooks) {
                                    layoutManager = new LinearLayoutManager(NoteBookActivity.this);
                                    adapterTypeBook = new AdapterTypeBook(NoteBookActivity.this, typeBooks, NoteBookActivity.this);
                                    rvBook.setLayoutManager(layoutManager);
                                    rvBook.setAdapter(adapterTypeBook);
                                }
                            });
                } else {
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
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }


    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if(item.getItemId()== R.id.item){
            SharedPreferences.Editor editor=sharedPreferences.edit();
            if(layoutManager instanceof GridLayoutManager){
                editor.remove("check").apply();
                layoutManager=new LinearLayoutManager(this);
                getData();


            }else {
                editor.putBoolean("check", true).apply();
                layoutManager = new GridLayoutManager(NoteBookActivity.this, 2);
                getData();
            }

        }

        return super.onOptionsItemSelected(item);
    }

    private void getData() {
        LiveData<List<TypeBook>> listLiveData = ApplicationNote.db.typeBookDAO().getListTypeBook();
        listLiveData.observe(this, new Observer<List<TypeBook>>() {
            @Override
            public void onChanged(@Nullable List<TypeBook> typeBooks) {
                adapterTypeBook = new AdapterTypeBook(NoteBookActivity.this, typeBooks, NoteBookActivity.this);
                rvBook.setLayoutManager(layoutManager);
                rvBook.setAdapter(adapterTypeBook);
            }
        });

    }

    @Override
    public void ionClick() {
        overridePendingTransition(R.anim.left_in, R.anim.right_out);
    }

}
