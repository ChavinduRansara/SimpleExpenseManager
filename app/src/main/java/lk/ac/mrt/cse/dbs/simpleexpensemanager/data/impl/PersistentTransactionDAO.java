package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Transaction;

public class PersistentTransactionDAO implements TransactionDAO {

    private Database database;

    public PersistentTransactionDAO(Database database) {
        this.database = database;
    }

    @Override
    public void logTransaction(Date date, String accountNo, ExpenseType expenseType, double amount) {
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        ContentValues values = new ContentValues();

        //values of the columns
        values.put("accountNo", accountNo);
        values.put("expenseType", expenseType.name());
        values.put("amount", amount);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            values.put("date", new SimpleDateFormat("dd/MM/YYYY", Locale.getDefault()).format(date));
        }

        // Inserting rows to the Transaction table
        sqLiteDatabase.insert(Database.TABLE_TRANSACTIONS, null, values);

        sqLiteDatabase.close();
    }

    @Override
    public List<Transaction> getAllTransactionLogs() {

        List<Transaction> transactionList = new ArrayList<>(); // List for storing all transaction logs.

        // Reading the database.
        Cursor cursor = database.getReadableDatabase().query(Database.TABLE_TRANSACTIONS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (true){
                //adding all transaction to the list.
                Date date = null;
                try {
                    date = new SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).parse(cursor.getString(1));

                    //Creating Transaction objects.
                    Transaction transaction = new Transaction(date, cursor.getString(2), ExpenseType.valueOf(cursor.getString(3)), Double.parseDouble(cursor.getString(4)));
                    transactionList.add(transaction);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (!cursor.moveToNext()){
                    break;
                }
            }
        }

        cursor.close();

        return transactionList;
    }

    @Override
    public List<Transaction> getPaginatedTransactionLogs(int limit) {

        List<Transaction> transactionList = getAllTransactionLogs();  // all transaction logs.

        int size = transactionList.size();
        if (size <= limit) {
            return transactionList;
        }
        // return the last <code>limit</code> number of transaction logs
        return transactionList.subList(size - limit, size);
    }
}