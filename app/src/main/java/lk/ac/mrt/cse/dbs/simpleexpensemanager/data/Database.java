package lk.ac.mrt.cse.dbs.simpleexpensemanager.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.Nullable;

public class Database extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "200524M";  //name of the Database
    private static final int DATABASE_VERSION = 1;

    public static Database instance;

    // Tables of the database
    public static final String TABLE_ACCOUNTS = "accounts";
    public static final String TABLE_TRANSACTIONS = "transactions";

    // Account table
    private static final String ACCOUNT_TABLE_CREATE = "CREATE TABLE " + TABLE_ACCOUNTS + "(" + "accountNo" + " TEXT PRIMARY KEY," + "bankName" + " TEXT," + "accountHolderName" + " TEXT," + "balance" + " REAL" + ")";

    // Transaction table
    private static final String TRANSACTIONS_TABLE_CREATE = "CREATE TABLE " + TABLE_TRANSACTIONS + "(" + "transactionId" + " INTEGER PRIMARY KEY AUTOINCREMENT," + "date" + " TEXT," + "accountNo" + " TEXT," + "expenseType" + " TEXT," + "amount" + " REAL," + "FOREIGN KEY(" + "accountNo" + ") REFERENCES "+ TABLE_ACCOUNTS +"(" + "accountNo" + ") )";

    public Database(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Create tables
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(ACCOUNT_TABLE_CREATE);
        sqLiteDatabase.execSQL(TRANSACTIONS_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        // discard the table if exist
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TABLE_ACCOUNTS + "'");
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS '" + TABLE_TRANSACTIONS + "'");
        onCreate(sqLiteDatabase);
    }

    public static Database getInstance(Context context) {
        if (instance == null) {
            instance = new Database(context);
        }
        return instance;
    };

}
