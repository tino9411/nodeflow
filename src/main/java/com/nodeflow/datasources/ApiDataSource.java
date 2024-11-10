package com.nodeflow.datasources;

public class ApiDataSource implements DataSource {
	private final String endpoint;

	public ApiDataSource(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String fetchData() {
		System.out.println("fetching data from API endpoint: " + endpoint);
		return "Data from API endpoint " + endpoint;
	}
}
