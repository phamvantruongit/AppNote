package vn.com.it.truongpham.appnote.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import java.util.ArrayList;
import java.util.List;

import vn.com.it.truongpham.appnote.ApplicationNote;
import vn.com.it.truongpham.appnote.DetailNoteActivity;
import vn.com.it.truongpham.appnote.ListNoteActivity;
import vn.com.it.truongpham.appnote.NoteBookActivity;
import vn.com.it.truongpham.appnote.R;
import vn.com.it.truongpham.appnote.data.TypeBook;
import vn.com.it.truongpham.appnote.view.ShowToast;

public class AdapterTypeBook extends RecyclerView.Adapter<AdapterTypeBook.ViewHolder> {
    Context context;
    List<TypeBook> list;
    IOnClick onClick;

    public AdapterTypeBook(Context context, List<TypeBook> list, IOnClick onClick) {
        this.context = context;
        this.list = list;
        this.onClick=onClick;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_type_book, viewGroup, false);
        return new ViewHolder(view);
    }




    @Override
    public void onBindViewHolder(@NonNull final ViewHolder viewHolder, final int i) {
        viewHolder.tvName.setText(list.get(i).name);

        viewHolder.tvDate.setText(list.get(i).date + "\t" + list.get(i).time);

        
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

                dialog.findViewById(R.id.tvAdd).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, DetailNoteActivity.class);
                        intent.putExtra("id", list.get(i).id);
                        context.startActivity(intent);
                        onClick.ionClick();
                    }
                });

                dialog.findViewById(R.id.tvListNote).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        Intent intent = new Intent(context, ListNoteActivity.class);
                        intent.putExtra("id", list.get(i).id);
                        context.startActivity(intent);
                        onClick.ionClick();
                    }
                });

                dialog.findViewById(R.id.tvEdit).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        dialog.dismiss();
                        final Dialog dia = new Dialog(context);
                        dia.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dia.setContentView(R.layout.show_popup_type_book);
                        dia.show();
                        TextView tvOK = dia.findViewById(R.id.tvOK);
                        TextView tvTitle=dia.findViewById(R.id.tvTitle);

                        tvTitle.setText(context.getString(R.string.title_edit));

                        final EditText edTypeBook = dia.findViewById(R.id.edTypeBook);
                        edTypeBook.setText(list.get(i).name);
                        tvOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String text = edTypeBook.getText().toString();
                                if (TextUtils.isEmpty(text)) {
                                    dia.dismiss();
                                    return;
                                }
                                ApplicationNote.db.typeBookDAO().update(text, list.get(i).id);
                                dia.dismiss();

                            }
                        });

                        dia.findViewById(R.id.tvExit).setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dia.dismiss();
                            }
                        });
                    }
                });

                dialog.findViewById(R.id.tvDelete).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                          ApplicationNote.db.typeBookDAO().deleteTypeNote(list.get(i).id);
                          ApplicationNote.db.bookDAO().deleteNote(list.get(i).id);
                          dialog.dismiss();
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
        TextView tvName ,tvDate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvBook);
            tvDate= itemView.findViewById(R.id.tvDate);

        }

    }

 public interface IOnClick{
        void ionClick();
    }

}
