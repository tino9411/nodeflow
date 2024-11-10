package com.nodeflow.datasources;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class DataSourceFactory {

    private static final Map<String, Function<Map<String, Object>, DataSource>> dataSourceRegistry = new HashMap<>();
    
    static {
      // Register default data sources
      dataSourceRegistry.put("API", config -> {
        String endpoint = (String) config.get("endpoint");
        Map<String, String> headers = DataSourceConfigParser.extractHeaders(config);
        Map<String, String> parameters = DataSourceConfigParser.extractParameters(config);
        int timeout = DataSourceConfigParser.extractTimeout(config);
        return new APIDataSource(endpoint, headers, parameters, timeout);
      });

      dataSourceRegistry.put("Database", config -> {
        String connectionString = (String) config.get("connectionString");
        return new DatabaseDataSource(connectionString);
      });
    }

    public static DataSource createDataSource(String sourceType, Map<String, Object> config) {
      Function<Map<String, Object>, DataSource> creator = dataSourceRegistry.get(sourceType);
      if (creator == null) {
        throw new IllegalArgumentException("Unknown data source type: " + sourceType);
      }
      return creator.apply(config);
    }

    public static void registerDataSource(String type, Function<Map<String, Object>, DataSource> creator) {
      dataSourceRegistry.put(type, creator);
    }

   
}