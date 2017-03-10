package com.iceq.dictionary.sql;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class SQLiteDriver
{
	Connection	connection	= null;

	Statement	statement	= null;
	ResultSet	resultSet	= null;

	public boolean openDatabaseConnection( String databaseName, boolean autoCommit)
	{
		try
		{
			connection = DriverManager.getConnection("jdbc:sqlite:" + databaseName);
			connection.setAutoCommit(autoCommit);
		} catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean commit()
	{
		try
		{
			connection.commit();
		} catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}

		return true;
	}

	public boolean closeDatabaseConnection()
	{
		dropResults();
		try
		{
			connection.close();
		} catch (SQLException e)
		{
			e.printStackTrace();
			return false;
		}
		connection = null;
		return true;
	}

	public ResultSet runQuery( String query)
	{
		try
		{
			statement = connection.createStatement();
			resultSet = statement.executeQuery(query);

			return resultSet;
		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return null;
	}

	public int runInsertOrUpdate( String query)
	{
		try
		{
			Statement updateStatement = connection.createStatement();
			updateStatement.executeUpdate(query);
			int id = updateStatement.getGeneratedKeys().getInt(1);
			updateStatement.close();

			return id;

		} catch (SQLException e)
		{
			e.printStackTrace();
		}

		return -1;
	}

	public void dropResults()
	{
		try
		{
			if (resultSet != null)
				resultSet.close();

			if (statement != null)
				statement.close();
		} catch (Exception e)
		{
			System.err.println(e.getClass().getName() + ": " + e.getMessage());
			System.exit(0);
		}
		resultSet = null;
		statement = null;
	}
}