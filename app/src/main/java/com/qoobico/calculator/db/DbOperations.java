package com.qoobico.calculator.db;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class DbOperations {
    private static final Pattern EMAIL_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);
    private static final Pattern LOGIN_PASSWORD_REGEX = Pattern.compile("^[a-zA-Z0-9._]*$", Pattern.CASE_INSENSITIVE);


    private static boolean checkEmail(String email) {
        Matcher matcher = EMAIL_REGEX.matcher(email);
        return matcher.find();
    }

    private static boolean checkLoginAndPassword(String input) {
        Matcher matcher = LOGIN_PASSWORD_REGEX.matcher(input);
        return matcher.find();
    }


    public static int addUser(UserModel user, DbHelper dbHelper) {

        ContentValues cv = new ContentValues();
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        if (checkLoginAndPassword(user.getLogin()) && checkLoginAndPassword(user.getPassword()) && checkEmail(user.getEmail())) {
            cv.put("login", user.getLogin());
            cv.put("password", user.getPassword());
            cv.put("email", user.getEmail());
            return (int) db.insert("user", null, cv);
        }
        return 0;
    }


    public static String checkEmail(UserModel user, DbHelper dbHelper){
        String password="";
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("user", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int emailColIndex = c.getColumnIndex("email");
            int passColIndex = c.getColumnIndex("password");
            do {
                if (c.getString(emailColIndex).equals(user.getEmail())) {
                    password=c.getString(passColIndex);
                    c.close();
                        return password;
                }

            } while (c.moveToNext());
        }
        c.close();

        return "";
    }


    public static boolean checkLogin(UserModel user, DbHelper dbHelper){

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("user", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int loginColIndex = c.getColumnIndex("login");
            do {
                if (c.getString(loginColIndex).equals(user.getLogin())) {
                    c.close();
                    return true;
                }

            } while (c.moveToNext());
        }
        c.close();

        return false;
    }


    public static boolean checkUserForLogin(UserModel user, DbHelper dbHelper) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();
        Cursor c = db.query("user", null, null, null, null, null, null);
        if (c.moveToFirst()) {
            int loginColIndex = c.getColumnIndex("login");
            int passColIndex = c.getColumnIndex("password");
            do {
                if (c.getString(loginColIndex).equals(user.getLogin()) && c.getString(passColIndex).equals(user.getPassword())) {
                    c.close();
                    return true;
                }

            } while (c.moveToNext());
        }
        c.close();

        return false;
    }
}


