package com.nodeflow.datasources;

import java.util.Map;

public class DataSourceFactory {

  public static DataSource createDataSource(Map<String, String> config) {
    String sourceType = config.get("sourceType");

    switch (sourceType) {
      case "API":
        String endpoint = config.get("endpoint");
        return new ApiDataSource(endpoint);

      case "Database":
        String connectionString = config.get("connectionString");
        return new DatabaseDataSource(connectionString);

      default:
        throw new IllegalArgumentException("Unknown data source type: " + sourceType);
    }
  }
}
