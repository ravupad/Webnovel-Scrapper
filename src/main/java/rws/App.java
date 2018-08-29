package rws;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;

public class App {
	public static HttpClient http_client;

	public static HttpClient getClient() {
		if (http_client == null) {
			http_client = HttpClients.custom()
					.setDefaultCookieStore(new BasicCookieStore())
					.setUserAgent("Mozilla/5.0 Firefox/26.0")
					.setDefaultRequestConfig(RequestConfig.custom()
							.setCookieSpec(CookieSpecs.STANDARD).build())
					.build();
		}
		return http_client;
	}
	
	public static void main(String[] args) 
			throws Exception {
		Handler handler = new Handler();
		handler.selectSource();
		handler.selectBook();
		handler.selectChapter();
		handler.fetchChapters();
	}
}
