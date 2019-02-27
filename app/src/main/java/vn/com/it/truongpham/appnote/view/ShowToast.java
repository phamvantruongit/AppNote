package vn.com.it.truongpham.appnote.view;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import vn.com.it.truongpham.appnote.R;

public class ShowToast {
    public static void showToast(Context context ,int layout){
        View view=LayoutInflater.from(context).inflate(layout,null);
        TextView textView=view.findViewById(R.id.tvTitle);
        textView.setPadding(70,0 ,0 ,0);
        Toast toast=new Toast(context);
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.BOTTOM|Gravity.CENTER_VERTICAL,0,100);
        view.setPadding(5,10, 80,10);
        toast.setView(view);
        toast.show();
    }
}
