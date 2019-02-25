package vn.com.it.truongpham.appnote;

import android.app.Application;
import android.support.annotation.NonNull;
import android.support.design.bottomnavigation.LabelVisibilityMode;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import vn.com.it.truongpham.appnote.data.TypeBook;
import vn.com.it.truongpham.appnote.fragment.FragmentBook;
import vn.com.it.truongpham.appnote.fragment.FragmentChapter;
import vn.com.it.truongpham.appnote.fragment.FragmentMore;
import vn.com.it.truongpham.appnote.fragment.FragmentTypeBook;

public class MainActivity extends AppCompatActivity {

    Fragment fragment;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    fragment=new FragmentTypeBook();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_dashboard:
                    fragment=new FragmentBook();
                    loadFragment(fragment);
                    return true;
                case R.id.navigation_notifications:
                    fragment=new FragmentChapter();
                    loadFragment(fragment);
                    return true;

                case R.id.navigation_more:
                    fragment=new FragmentMore();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        BottomNavigationView navigation = findViewById(R.id.navigation);
        navigation.setLabelVisibilityMode(LabelVisibilityMode.LABEL_VISIBILITY_LABELED);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        fragment=new FragmentTypeBook();
        loadFragment(fragment);
    }

    private void loadFragment(Fragment fragment){
        FragmentManager fragmentManager=getSupportFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.main,fragment).addToBackStack(null).commit();
    }
}
