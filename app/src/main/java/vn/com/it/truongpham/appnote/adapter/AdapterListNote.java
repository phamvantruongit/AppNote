package vn.com.it.truongpham.appnote.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.TextView;

import java.util.List;

import vn.com.it.truongpham.appnote.ApplicationNote;
import vn.com.it.truongpham.appnote.DetailNoteActivity;
import vn.com.it.truongpham.appnote.ListNoteActivity;
import vn.com.it.truongpham.appnote.R;
import vn.com.it.truongpham.appnote.data.Book;

public class AdapterListNote extends RecyclerView.Adapter<AdapterListNote.ViewHolder> {
    List<Book> list;
    Context context;
    public AdapterListNote(List<Book> list) {
        this.list = list;
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
        tvTitle.setText(list.get(i).chapter);

        WebView tvContent=viewHolder.itemView.findViewById(R.id.tvContent);
        String content="<body style=\"text-align:justify;text-indent:5px;font-size:15px\">" + list.get(i).content + "</body>";
        tvContent.loadData(content ,"text/html", "UTF-8");

        viewHolder.itemView.findViewById(R.id.item).setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
                menu.setHeaderTitle("Select The Action");
                menu.add("Chi tiet").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        Intent intent = new Intent(context, DetailNoteActivity.class);
                        intent.putExtra("note",list.get(i));
                        context.startActivity(intent);
                        return true;
                    }
                });

                menu.add("Xoa").setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        ApplicationNote.db.bookDAO().delete(list.get(i).id);
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

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
