package com.nodeflow;

import java.util.Map;

public class DataFetcher implements NodeBehaviour {
	private DataSource dataSource;

	public DataFetcher(Map<String, String> config) {
		this.dataSource = DataSourceFactory.createDataSource(config);
	}

	@Override
	public void execute(Node node) {
		// Simulate fetching fetching data	
		String data = dataSource.fetchData();
		node.setOutput(data);
		System.out.println("DataFetcher executed and set output: " + data);
	}
}
