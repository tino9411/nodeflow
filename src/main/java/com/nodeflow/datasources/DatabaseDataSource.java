package com.nodeflow.datasources;

public class DatabaseDataSource implements DataSource {
	private final String connectionString;

	public DatabaseDataSource(String connectionString) {
		this.connectionString = connectionString;
	}

	@Override
	public String fetchData() {
		System.out.println("Fetching data from database with connection: " + connectionString);
        return "Data from database with connection " + connectionString;
	}
}
