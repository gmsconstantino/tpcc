package com.gomes.myhashjdbc.db;

import com.yahoo.ycsb.ByteIterator;
import database.Database;
import database.Transaction;
import database.TransactionFactory;

import java.util.HashMap;

/**
 * Created by gomes on 21/03/15.
 */
public class DBSingleton {

    private static DBSingleton singleton;

    static final TransactionFactory.type TYPE = TransactionFactory.type.TWOPL;
    private Database<String, HashMap<String, String>> db;

    // Should be concurrent hash map?
    private HashMap<String, Table> tablesHashMap;


    private DBSingleton() {
        db = new Database<>();
        tablesHashMap = new HashMap<>();
    }

    public static DBSingleton getInstance() {
        if (singleton == null){
            singleton = new DBSingleton();
        }
        return singleton;
    }

    public Database<String, HashMap<String, String>> getDb() {
        return db;
    }

    public void addTable(String nameTable, String keyPattern) {
        tablesHashMap.put(nameTable, new Table(nameTable,keyPattern));
    }

    public Table getTable(String name) {
        return tablesHashMap.get(name);
    }

    public Transaction<String, HashMap<String, String>> newTransaction(){
        return db.newTransaction(TYPE);
    }
}
