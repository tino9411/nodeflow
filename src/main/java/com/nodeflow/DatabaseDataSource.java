package com.nodeflow;

public class DatabaseDataSource implements DataSource {
	private String connectionString;

	public DatabaseDataSource(String connectionString) {
		this.connectionString = connectionString;
	}

	@Override
	public String fetchData() {
		System.out.println("Fetching data from database with connection: " + connectionString);
        return "Data from database with connection " + connectionString;
	}
}
