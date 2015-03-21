package com.gomes.myhashjdbc.statement.impl;

import java.io.File;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Map;

import com.gomes.myhashjdbc.statement.PreparedStatementAdapter;

/**
 * Wraps the {@link myStatement} as a prepared statement.
 *
 * @author Kai Winter
 */
public class myPreparedStatement extends PreparedStatementAdapter {

	private final myStatement statement;
	private final String sql;

	/**
	 * Constructs a new {@link myPreparedStatement}.
	 *
	 * @param sql
	 *            the SQL statement.
	 */
	public myPreparedStatement(String sql) {
		this.statement = new myStatement();
		this.sql = sql;
	}

	@Override
	public ResultSet executeQuery() throws SQLException {
		return statement.executeQuery(sql);
	}

	@Override
	public ResultSet executeQuery(String sql) throws SQLException {
		return statement.executeQuery(sql);
	}

}
