package lk.ac.mrt.cse.dbs.simpleexpensemanager.control;

import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.AccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.Database;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.TransactionDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentAccountDAO;
import lk.ac.mrt.cse.dbs.simpleexpensemanager.data.impl.PersistentTransactionDAO;

public class PersistentExpenseManager extends ExpenseManager{

    private Database database;
    public PersistentExpenseManager(Database database) {
        this.database = database;
        setup();
    }

    @Override
    public void setup() {

        TransactionDAO transactionDAO = new PersistentTransactionDAO(database);
        setTransactionsDAO(transactionDAO);

        AccountDAO accountDAO = new PersistentAccountDAO(database);
        setAccountsDAO(accountDAO);

    }
}