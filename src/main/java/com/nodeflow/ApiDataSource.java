package com.nodeflow;

public class ApiDataSource implements DataSource {
	private String endpoint;

	public ApiDataSource(String endpoint) {
		this.endpoint = endpoint;
	}

	@Override
	public String fetchData() {
		System.out.println("fetching data from API endpoint: " + endpoint);
		return "Data from API endpoint " + endpoint;
	}
}
