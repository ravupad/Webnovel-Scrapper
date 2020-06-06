package rws;

import org.apache.http.client.HttpClient;
import org.apache.http.client.config.CookieSpecs;
import org.apache.http.client.config.RequestConfig;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicCookieStore;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class App {
	public static HttpClient http_client = HttpClients.custom()
			.setDefaultCookieStore(new BasicCookieStore())
			.setUserAgent("Mozilla/5.0 Firefox/26.0")
			.setDefaultRequestConfig(RequestConfig.custom()
					.setCookieSpec(CookieSpecs.STANDARD).build())
			.build();

	public static void main(String[] args) throws Exception {
		Handler handler = new Handler();
		handler.selectSource();
		handler.selectBook();
		handler.selectChapter();
		handler.fetchChapters();
		System.out.println("\nExiting");
	}

	public static HttpClient getClient() {
		return http_client;
	}

	public static Document getDocument(String url) throws Exception {
		HttpGet request = new HttpGet(url);
		var response = App.getClient().execute(request);
		if (response.getStatusLine().getStatusCode() != 200) {
			throw new Exception(
					url + ": " + response.getStatusLine().getReasonPhrase());
		}
		var content = EntityUtils.toString(
				response.getEntity(), "UTF-8");
		return Jsoup.parse(content);
	}
}
