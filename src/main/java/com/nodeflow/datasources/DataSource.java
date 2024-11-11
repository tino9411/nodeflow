package com.nodeflow.datasources;

import java.io.IOException;
import java.net.http.HttpTimeoutException;

public interface DataSource {
	String fetchData() throws IOException, InterruptedException, HttpTimeoutException;
}
