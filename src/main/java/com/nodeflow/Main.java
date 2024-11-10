package com.nodeflow;

import java.util.HashMap;
import java.util.Map;

/** Hello world! */
public class Main {
  public static void main(String[] args) {

		Map<String, String> apiConfig = new HashMap<>();
		apiConfig.put("sourceType", "API");
		apiConfig.put("endpoint", "https://api.example.com/data");

		DataFetcher apiFetcher = new DataFetcher(apiConfig);
		Node apiNode = new Node(NodeType.DATA_FETCH);
		apiNode.addBehaviour(apiFetcher);
		apiNode.run();
		System.out.println("API Node Output: " + apiNode.getOutput());

		Map<String, String> dbConfig = new HashMap<>();
		dbConfig.put("sourceType", "Database");
		dbConfig.put("connectionString", "jdbc:mysql://localhost:3306/mydb");

		DataFetcher dbFetcher = new DataFetcher(dbConfig);
		Node dbNode = new Node(NodeType.DATA_FETCH);
		dbNode.addBehaviour(dbFetcher);
		dbNode.run();
		System.out.println("Database Node Output: " + dbNode.getOutput());


	}
}
