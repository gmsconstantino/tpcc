package com.gomes.myhashjdbc;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import com.gomes.myhashjdbc.connection.impl.DummyConnection;
import junit.framework.Assert;

import org.junit.Test;

public final class DummyJdbcDriverTest {

	@Test
	public void testGetGenericConnection() throws ClassNotFoundException, SQLException {

		Class.forName(Driver.class.getCanonicalName());
		Connection connection = DriverManager.getConnection("jdbc:myhashdb");

		Assert.assertTrue(connection instanceof DummyConnection);
	}
}
