package vn.com.it.truongpham.appnote.adapter;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebView;
import android.widget.TextView;


import java.util.List;

import vn.com.it.truongpham.appnote.ApplicationNote;
import vn.com.it.truongpham.appnote.DetailNoteActivity;
import vn.com.it.truongpham.appnote.R;
import vn.com.it.truongpham.appnote.data.Book;

public class AdapterListNote extends RecyclerView.Adapter<AdapterListNote.ViewHolder> {
    List<Book> list;
    Context context;
    IOnClick iOnClick;
    public AdapterListNote(List<Book> list,IOnClick iOnClick) {
        this.list = list;
        this.iOnClick=iOnClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        context=viewGroup.getContext();
        View view= LayoutInflater.from(context).inflate(R.layout.item_book,viewGroup,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder viewHolder, final int i) {
        TextView tvTitle=viewHolder.itemView.findViewById(R.id.tvTitle);
        TextView tvTimeDate= viewHolder.itemView.findViewById(R.id.tvTimDate);
        tvTitle.setText(list.get(i).chapter);
        tvTimeDate.setText(list.get(i).date + "\t"  + list.get(i).time);
        Log.d("PPPP",list.get(i).date + list.get(i).time);



        WebView tvContent=viewHolder.itemView.findViewById(R.id.tvContent);
        final String content="<body style=\"text-align:justify;text-indent:5px;font-size:15px\">" + list.get(i).content + "</body>";
        tvContent.loadData(content ,"text/html", "UTF-8");
        viewHolder.itemView.findViewById(R.id.item).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final Dialog dialog=new Dialog(context);
                dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                dialog.setContentView(R.layout.layout_item);
                dialog.show();

                if (dialog.getWindow() != null) {
                    dialog.getWindow().setGravity(Gravity.BOTTOM);
                    dialog.getWindow().setLayout(
                            WindowManager.LayoutParams.MATCH_PARENT,
                            WindowManager.LayoutParams.WRAP_CONTENT
                    );
                    dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
                }

                TextView tvDetail=dialog.findViewById(R.id.tvAdd);
                TextView tvDelete=dialog.findViewById(R.id.tvListNote);
                tvDetail.setText(context.getString(R.string.detail));
                tvDelete.setText(context.getString(R.string.delete));

                dialog.findViewById(R.id.tvEdit).setVisibility(View.GONE);

                tvDetail.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, DetailNoteActivity.class);
                        intent.putExtra("note",list.get(i));
                        context.startActivity(intent);
                        iOnClick.ionClick();
                    }
                });

                tvDelete.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        ApplicationNote.db.bookDAO().delete(list.get(i).id);
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }

    public  interface IOnClick{
        void ionClick();
    }
}
