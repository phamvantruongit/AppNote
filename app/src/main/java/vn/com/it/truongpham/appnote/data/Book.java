package vn.com.it.truongpham.appnote.data;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity
public class Book {
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    public int id;

    @ColumnInfo(name = "name")
    public String name;


    @ColumnInfo(name = "chapter")
    public String chapter;


    @ColumnInfo(name = "content")
    public String content;


    @ColumnInfo(name = "id_type_book")
    public String id_type_book;





}
