package vn.com.it.truongpham.appnote.data;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TypeBookDAO {

    @Insert void insertTypeBook(TypeBook... typeBook);

    @Query("SELECT * FROM typebook ")
    List<TypeBook> getListTypeBook();




//
//    @Query("select * from UserEntity where uid in (:userIds)")
//    List<UserEntity> loadAllDataByIds(int [] userIds);
//
//    @Query("select * from UserEntity where first_name LIKE :first_name AND " + "last_name LIKE :last_name LIMIT 1")
//    UserEntity findByName(String first_name , String last_name);
//
//
//    @Insert
//    void insertAll(UserEntity... userEntities);
//
//    @Query("delete from UserEntity where uid in (:uid)")
//    void delete(int uid);
//
//
//    @Query("update userentity set first_name= :first_name ,last_name= :last_name where uid in (:uid)")
//    void update(String first_name ,String last_name,int uid);
//
//    @Insert
//    void insertStudent(StudentEntity... studentEntity);
//
//    @Query("select userentity.uid AS id, userentity.first_name AS first_name  ,userentity.last_name AS last_name  ,studententity.address " +
//            " FROM userentity, studententity " + " where userentity.uid = studententity.id")
//    List<User> loadAllUser();




}
