package vn.com.it.truongpham.appnote.data;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;

@Entity
public class Book implements Serializable {
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
    public int id_type_book;


    @ColumnInfo(name = "test")
    public String test;


}
