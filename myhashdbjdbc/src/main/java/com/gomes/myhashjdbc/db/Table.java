package com.gomes.myhashjdbc.db;

/**
 * Created by gomes on 21/03/15.
 */
public class Table {

    private String name;
    private String keyPattern;

    public Table(String name, String keyPattern) {
        this.name = name;
        this.keyPattern = keyPattern;
    }

    public String getName() {
        return name;
    }

    public String getKeyPattern() {
        return keyPattern;
    }
}
