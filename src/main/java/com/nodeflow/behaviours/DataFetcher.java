package com.nodeflow.behaviours;

import java.io.IOException;
import java.util.Map;

import com.nodeflow.datasources.DataSource;
import com.nodeflow.datasources.DataSourceFactory;
import com.nodeflow.nodes.Node;

public class DataFetcher implements NodeBehaviour {
	private final DataSource dataSource;

	public DataFetcher(Map<String, Object> config) {
		String sourceType = (String) config.get("sourceType");
		if (sourceType == null) {
			throw new IllegalArgumentException("Missing required 'sourceType' in DataFetcher configuration");
		}
		this.dataSource = DataSourceFactory.createDataSource(sourceType, config);
	}

	@Override
	public void execute(Node node) {
		try {
			String data = dataSource.fetchData();
			node.setOutput(data);
		} catch (IOException e) {
			System.err.println("IO Error while fecthing data: " + e.getMessage());
		} catch (InterruptedException e) {
			Thread.currentThread().interrupt();
			System.err.println("Data fetching was interrupted");
		} catch (Exception e) {
			System.err.println("Unexpected error while fetching data " + e.getMessage());
		}
		
	}
}
