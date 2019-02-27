package vn.com.it.truongpham.appnote.view;

import vn.com.it.truongpham.appnote.data.TypeBook;

public interface IOnClick {
    public interface IOnClickAdapter{
        void OnClickItem(TypeBook typeBook);
    }
}
