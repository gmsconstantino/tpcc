package com.gomes.myhashjdbc.connection.impl;

import java.io.File;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Map;

import com.yahoo.ycsb.*;

import com.gomes.myhashjdbc.connection.ConnectionAdapter;
import com.gomes.myhashjdbc.statement.impl.myPreparedStatement;
import com.gomes.myhashjdbc.statement.impl.myStatement;
import database.Database;

/**
 * Connection which implements the methods {@link #createStatement()} and {@link #prepareStatement(String)}. The
 * {@link DummyConnection} tries to open a CSV file in the directory <code>./table/</code> with the name
 * <code>&lt;tablename&gt;.csv</code> and returns the contained values. The query result will contain all values from
 * the file, it will not be narrowed by the query. The first line of the CSV file has to contain the column names.
 *
 * @author Kai Winter
 */
public class DummyConnection extends ConnectionAdapter {

    private Database<String, HashMap<String, ByteIterator>> db;

	/**
	 * Constructs a new {@link DummyConnection}.
	 */
	public DummyConnection() {

	}

	@Override
	public Statement createStatement() throws SQLException {
		return new myStatement();
	}

	@Override
	public PreparedStatement prepareStatement(String sql) throws SQLException {
		return new myPreparedStatement(sql);
	};
}
