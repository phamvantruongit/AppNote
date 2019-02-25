package vn.com.it.truongpham.appnote.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import vn.com.it.truongpham.appnote.ApplicationNote;
import vn.com.it.truongpham.appnote.R;
import vn.com.it.truongpham.appnote.data.TypeBook;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTypeBook extends Fragment {



    List<TypeBook> bookList;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_book, container, false);
        bookList=new ArrayList<>();

        bookList=ApplicationNote.db.typeBookDAO().getListTypeBook();
        for(TypeBook book: bookList){
            Log.d("TAG",book.name);
        }

        setHasOptionsMenu(true);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.item_add){
            final Dialog dialog=new Dialog(getActivity());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.show_popup_type_book);
            dialog.show();
            TextView tvOK=dialog.findViewById(R.id.tvOK);
            final EditText edTypeBook=dialog.findViewById(R.id.edTypeBook);
            tvOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text=edTypeBook.getText().toString();
                    if(TextUtils.isEmpty(text)){
                        Toast.makeText(getContext(), "Enter name", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    TypeBook typeBook=new TypeBook();
                    typeBook.name=text;
                    ApplicationNote.db.typeBookDAO().insertTypeBook(typeBook);
                    dialog.dismiss();

                }
            });
        }
        return super.onOptionsItemSelected(item);
    }
}
