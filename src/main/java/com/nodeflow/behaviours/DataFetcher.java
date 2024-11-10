package com.nodeflow.behaviours;

import java.util.Map;

import com.nodeflow.datasources.DataSource;
import com.nodeflow.datasources.DataSourceFactory;
import com.nodeflow.nodes.Node;

public class DataFetcher implements NodeBehaviour {
	private final DataSource dataSource;

	public DataFetcher(Map<String, Object> config) {
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
