package com.nodeflow;

public class DataFetcher implements NodeBehaviour {

	@Override
	public void execute(Node node) {
		// Simulate fetching fetching data	
		String fetchedData = "Fetched data from source";
		node.setOutput(fetchedData);
		System.out.println("DataFetcher executed: " + fetchedData);
	}
}
