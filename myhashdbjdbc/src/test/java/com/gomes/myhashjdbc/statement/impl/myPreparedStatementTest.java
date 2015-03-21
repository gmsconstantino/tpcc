package com.gomes.myhashjdbc.statement.impl;

import java.io.File;
import java.net.URISyntaxException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import junit.framework.Assert;

import org.junit.Before;
import org.junit.Test;

import com.gomes.myhashjdbc.Driver;

public final class myPreparedStatementTest {

	private ResultSet resultSet;

	@Before
	public void setup() throws ClassNotFoundException, SQLException, URISyntaxException {
		Class.forName(Driver.class.getCanonicalName());

		Driver.addTableResource("test_table", new File(CsvGenericStatementTest.class.getResource(
                "test_table.csv").toURI()));
		Connection connection = DriverManager.getConnection("jdbc:myhashdb");
		PreparedStatement statement = connection.prepareStatement("SELECT * FROM test_table");

		Assert.assertTrue(statement instanceof myPreparedStatement);
		resultSet = statement.executeQuery();
	}

	@Test
	public void testGetByColumnName() throws SQLException {

		Assert.assertTrue(resultSet.next());

		Assert.assertEquals(1, resultSet.getInt("id"));
		Assert.assertEquals("Germany", resultSet.getString("country_name"));
		Assert.assertEquals("DE", resultSet.getString("country_iso"));
	}

	@Test
	public void testGetByColumnIndex() throws SQLException {

		Assert.assertTrue(resultSet.next());

		Assert.assertEquals(1, resultSet.getInt(1));
		Assert.assertEquals("Germany", resultSet.getString(2));
		Assert.assertEquals("DE", resultSet.getString(3));
	}

	@Test(expected = SQLException.class)
	public void testGetInvalidColumnName() throws SQLException {

		Assert.assertTrue(resultSet.next());

		resultSet.getInt("undefined");

		Assert.fail("Expected exception not thrown");
	}

	@Test(expected = SQLException.class)
	public void testGetInvalidColumnindex() throws SQLException {

		Assert.assertTrue(resultSet.next());
		resultSet.getInt(17);

		Assert.fail("Expected exception not thrown");
	}
}
