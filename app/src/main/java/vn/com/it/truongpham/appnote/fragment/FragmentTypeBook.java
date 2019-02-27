package vn.com.it.truongpham.appnote.fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
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
import vn.com.it.truongpham.appnote.adapter.AdapterTypeBook;
import vn.com.it.truongpham.appnote.data.TypeBook;
import vn.com.it.truongpham.appnote.view.IOnClick;
import vn.com.it.truongpham.appnote.view.ShowToast;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentTypeBook extends Fragment implements IOnClick.IOnClickAdapter {



    List<TypeBook> bookList;
    RecyclerView rvBook;
    AdapterTypeBook adapterTypeBook;
    RecyclerView.LayoutManager layoutManager;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_type_book, container, false);
        rvBook=view.findViewById(R.id.rvbook);

        bookList=new ArrayList<>();

        getData();


        setHasOptionsMenu(true);
        return view;
    }

    private void getData(){
        bookList=ApplicationNote.db.typeBookDAO().getListTypeBook();
        layoutManager=new LinearLayoutManager(getActivity());

        adapterTypeBook=new AdapterTypeBook(getActivity(),bookList ,this);
        rvBook.setLayoutManager(layoutManager);
        rvBook.setAdapter(adapterTypeBook);
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
            dialog.setCancelable(false);
            dialog.show();
            TextView tvOK=dialog.findViewById(R.id.tvOK);
            final EditText edTypeBook=dialog.findViewById(R.id.edTypeBook);
            tvOK.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String text=edTypeBook.getText().toString();
                    if(TextUtils.isEmpty(text)){
                        ShowToast.showToast(getActivity(),R.layout.show_toast_error);
                        return;
                    }
                    TypeBook typeBook=new TypeBook();
                    typeBook.name=text;
                    ApplicationNote.db.typeBookDAO().insertTypeBook(typeBook);
                    dialog.dismiss();
                    getData();

                }
            });
           TextView tvCancel=dialog.findViewById(R.id.tvCancel);
            tvCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dialog.dismiss();
                }
            });
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void OnClickItem(TypeBook typeBook) {
        FragmentManager fragmentManager=getChildFragmentManager();
        FragmentTransaction fragmentTransaction=fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.main,new FragmentBook()).addToBackStack(null).commit();
    }
}
