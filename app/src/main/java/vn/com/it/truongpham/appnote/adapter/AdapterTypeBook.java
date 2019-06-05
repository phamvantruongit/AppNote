package vn.com.it.truongpham.appnote.adapter;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import vn.com.it.truongpham.appnote.ApplicationNote;
import vn.com.it.truongpham.appnote.DetailNoteActivity;
import vn.com.it.truongpham.appnote.ListNoteActivity;
import vn.com.it.truongpham.appnote.NoteBookActivity;
import vn.com.it.truongpham.appnote.R;
import vn.com.it.truongpham.appnote.data.TypeBook;
import vn.com.it.truongpham.appnote.view.ShowToast;

public class AdapterTypeBook extends RecyclerView.Adapter<AdapterTypeBook.ViewHolder>  {
    Context context;
    List<TypeBook> list;

    public AdapterTypeBook(Context context, List<TypeBook> list) {
        this.context = context;
        this.list = list;
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


        viewHolder.itemView.findViewById(R.id.item).setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Select The Action");
                menu.add("Them").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(context, DetailNoteActivity.class);
                        intent.putExtra("id",list.get(i).id);
                        context.startActivity(intent);
                        return true;
                    }
                });

                menu.add("Danh sach").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(context, ListNoteActivity.class);
                        intent.putExtra("id",list.get(i).id);
                        context.startActivity(intent);
                        return true;
                    }
                });


                menu.add("Sua").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        final Dialog dialog = new Dialog(context);
                        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
                        dialog.setContentView(R.layout.show_popup_type_book);
                        dialog.setCancelable(false);
                        dialog.show();
                        TextView tvOK = dialog.findViewById(R.id.tvOK);

                        final EditText edTypeBook = dialog.findViewById(R.id.edTypeBook);
                        edTypeBook.setText(list.get(i).name);
                        tvOK.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                String text = edTypeBook.getText().toString();
                                if (TextUtils.isEmpty(text)) {
                                    ShowToast.showToast(context, R.layout.show_toast_error);
                                    return;
                                }
                                ApplicationNote.db.typeBookDAO().update(text,list.get(i).id);
                                dialog.dismiss();

                            }
                        });
                        TextView tvCancel = dialog.findViewById(R.id.tvCancel);
                        tvCancel.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                dialog.dismiss();
                            }
                        });


                        return true;
                    }
                });
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder  {
        TextView tvName;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvBook);

        }


    }

}
