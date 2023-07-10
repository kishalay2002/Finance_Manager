package com.example.financemanager;

//For database management
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBHandler extends SQLiteOpenHelper
{
    // creating a constant variables for our database.
    // below variable is for our database name.
    private static final String DB_NAME = "coursedb";

    // below int is our database version
    private static final int DB_VERSION = 1;

    // below variable is for our table name.
    private static final String TABLE_NAME = "transactions";

    // below variable is for our id column.
    private static final String ID_COL = "id";

    // below variable is for our course name column
    private static final String NAME_COL = "name";

    // below variable id for our course duration column.
    private static final String DURATION_COL = "duration";

    // below variable for our course description column.
    private static final String DESCRIPTION_COL = "description";

    // below variable is for our course tracks column.
    private static final String TRACKS_COL = "tracks";

    public DBHandler(@Nullable Context context ) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("create table if not exists category(ID interget primary key autoincrement, name varchar(20), icon varchar(20))");

        db.execSQL("create table if not exists currencyTbl(shortForm varchar(4) primary key ,symbol varchar(1), country varchar(70))");

        db.execSQL("create table if not exists Accounts(Accno varchar(20) primary key, debitAccountNo varchar(20), creditCardAccountNo varchar(20), balance float not null)");

        db.execSQL("create table if not exists transaction_hist" +
        "(TransID integer primary key autoincrement ,Amount float not null, Date datetime not null,transType integer check(transType in (-1,1)),currency varchar(4),categoryID integer,note varchar(100),msg varchar(2000)," +
        "note varchar(200),PaymentMethod varchar(4) check(PaymentMethod in ('Cash','Bank','Card')), Accno varchar(20)," +
        "foreign key(categoryID) references category(ID), foreign key(currency) references currencyTbl(shortForm), foreign key(Accno) references Accounts(Accno));");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }
}
