package com.example.financemanager;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.concurrent.locks.ReentrantLock;


public class MessageDB extends SQLiteOpenHelper
{

    private static final int DB_VERSION=1;

    private final ReentrantLock lock = new ReentrantLock();

    public MessageDB(@Nullable Context context )
    {
        super(context, "MessageDB", null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table if not exists buffer(message varchar2(2000),time long);");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {}

    @SuppressLint("Range")
    public String criticalSection(String message, long time, int code)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String resp="";
        long timestamp;

        lock.lock();

        try
        {
            switch(code)
            {
                case 1:
                    db.execSQL("insert into buffer values('"+message+"',"+time+")");
                    Log.d("SQL","Inserted");
                    db.close();
                    break;
                case 2:
                    Cursor c= db.rawQuery("select * from buffer order by time desc limit 1;",null);

                    if(c.moveToFirst())
                    {
                        do
                        {
                            resp=c.getString(c.getColumnIndex("message"));
                            timestamp=c.getLong(c.getColumnIndex("time"));
                        }while (c.moveToNext());

                        db.execSQL("delete from buffer where time ="+timestamp);
                        db.close();
                    }
                    break;
            }
        }
        catch (SQLException e)
        {
            e.printStackTrace();
        }

        finally
        {
            lock.unlock();
        }
        return resp;
    }

}
