package lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.exception.InvalidAccountException;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.Account;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.model.ExpenseType;

public class PersistentAccountDAO implements AccountDAO {

    private Database database;

    public PersistentAccountDAO(Database database) {
        this.database = database;
    }

    @Override
    public List<String> getAccountNumbersList() {

        List<String> accountNumbersList = new ArrayList<>();  // List for store account numbers.

        //Read from the database
        Cursor cursor = database.getWritableDatabase().query(Database.TABLE_ACCOUNTS, new String[] {"accountNo"}, null, null, null, null, null);

        // adding account numbers to the list.
        if (cursor.moveToFirst()) {
            while(true){
                accountNumbersList.add(cursor.getString(0));
                if(!cursor.moveToNext()){
                    break;
                }
            }
        }

        cursor.close();
        return accountNumbersList;
    }

    @Override
    public List<Account> getAccountsList() {
        List<Account> accountList = new ArrayList<>();  // List for store the account objects.

        //projection of the account table
        Cursor cursor = database.getWritableDatabase().query(Database.TABLE_ACCOUNTS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            while (true){
                // Creating account objects
                Account account = new Account(cursor.getString(0), cursor.getString(1),cursor.getString(2), Double.parseDouble(cursor.getString(3)));
                accountList.add(account);   // add account objects to the account list
                if (!cursor.moveToNext()){
                    break;
                }
            }
        }
        cursor.close();
        return accountList;
    }

    @Override
    public Account getAccount(String accountNo) throws InvalidAccountException {

        // Read the database.
        Cursor cursor = database.getWritableDatabase().query(Database.TABLE_ACCOUNTS, null, "accountNo" + "=?", new String[] { accountNo }, null, null, null, null);

        if (cursor != null) {
            cursor.moveToFirst();
            Account account = new Account(cursor.getString(0), cursor.getString(1), cursor.getString(2), Double.parseDouble(cursor.getString(3)));
            cursor.close();
            return account;
        }
        String msg = "Account " + accountNo + " is invalid.";
        throw new InvalidAccountException(msg);
    }

    @Override
    public void addAccount(Account account) {
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("accountNo", account.getAccountNo());
        values.put("bankName", account.getBankName());
        values.put("accountHolderName", account.getAccountHolderName());
        values.put("balance", account.getBalance());

        //inserting
        sqLiteDatabase.insert(Database.TABLE_ACCOUNTS, null, values);
        sqLiteDatabase.close();
    }

    @Override
    public void removeAccount(String accountNo) throws InvalidAccountException {
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();

        //Deleting
        sqLiteDatabase.delete(Database.TABLE_ACCOUNTS, "accountNo"+ " = ?", new String[] { accountNo });
        sqLiteDatabase.close();
    }

    @Override
    public void updateBalance(String accountNo, ExpenseType expenseType, double amount) throws InvalidAccountException {
        Account account = this.getAccount(accountNo);
        SQLiteDatabase sqLiteDatabase = database.getWritableDatabase();

        ContentValues values = new ContentValues();

        //calculating values
        switch (expenseType) {
            case EXPENSE:
                values.put("balance", account.getBalance() - amount);
                break;
            case INCOME:
                values.put("balance", account.getBalance() + amount);
                break;
        }

        //update the table.
        sqLiteDatabase.update(Database.TABLE_ACCOUNTS, values, "accountNo" + " = ?", new String[] { accountNo });
    }
}

