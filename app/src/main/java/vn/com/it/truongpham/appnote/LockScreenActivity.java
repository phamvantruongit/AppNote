package vn.com.it.truongpham.appnote;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.andrognito.patternlockview.PatternLockView;
import com.andrognito.patternlockview.listener.PatternLockViewListener;
import com.andrognito.patternlockview.utils.PatternLockUtils;

import java.util.List;

import io.paperdb.Paper;

public class LockScreenActivity extends AppCompatActivity {

    PatternLockView patternLockView;
    private String save_pattern_key = "pattern_code";
    String final_pattern = "patternLockView";
    SharedPreferences preferences;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        preferences=getSharedPreferences("LockScreen",MODE_PRIVATE);
        String check_lock=preferences.getString("lock","");

            Paper.init(this);

            final String save_pattern = Paper.book().read(save_pattern_key);
            if (save_pattern != null && !save_pattern.equals("null")) {
                start(save_pattern);
            } else {
                setContentView(R.layout.parent_enter_pass);
                patternLockView = findViewById(R.id.patternLockView);
                patternLockView.addPatternLockListener(new PatternLockViewListener() {
                    @Override
                    public void onStarted() {

                    }

                    @Override
                    public void onProgress(List<PatternLockView.Dot> progressPattern) {

                    }

                    @Override
                    public void onComplete(List<PatternLockView.Dot> pattern) {
                        final_pattern = PatternLockUtils.patternToString(patternLockView, pattern);
                    }

                    @Override
                    public void onCleared() {

                    }
                });
                findViewById(R.id.btnAccept).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!final_pattern.equals("patternLockView")) {
                            Paper.book().write(save_pattern_key, final_pattern);
                            Paper.init(LockScreenActivity.this);
                            final String save_pattern = Paper.book().read(save_pattern_key);
                            start(save_pattern);
                            SharedPreferences.Editor editor=preferences.edit();
                            editor.putString("lock","true");
                            editor.commit();
                        }
                    }
                });
            }



    }


    private void start(final String save_pattern) {
        setContentView(R.layout.parent_screen);
        patternLockView = findViewById(R.id.patternLockView);
        patternLockView.addPatternLockListener(new PatternLockViewListener() {
            @Override
            public void onStarted() {

            }

            @Override
            public void onProgress(List<PatternLockView.Dot> progressPattern) {

            }

            @Override
            public void onComplete(final List<PatternLockView.Dot> pattern) {

                final_pattern = PatternLockUtils.patternToString(patternLockView, pattern);
                if (final_pattern.equals(save_pattern)) {
                    startActivity(new Intent(LockScreenActivity.this, NoteBookActivity.class));
                    finish();
                } else {
                    Toast.makeText(LockScreenActivity.this, "Incorrect password ", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onCleared() {

            }
        });

    }
}
