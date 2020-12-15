package rws.sources.wuxiaworld;

import java.util.Scanner;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import rws.App;
import rws.ISource;

public class WuxiaSource implements ISource {
	public static final String baseUrl = "https://www.wuxiaworld.com";

	public static Document getDocument(String url) {
		try {
			HttpGet request = new HttpGet(baseUrl + url);
			var response = App.getClient().execute(request);
			if (response.getStatusLine().getStatusCode() != 200) {
				return null;
			}
			var content = EntityUtils.toString(response.getEntity(), "UTF-8");
			return Jsoup.parse(content);
		} catch (Exception ex) {
			return null;
		}
	}

	@Override
	public WuxiaBook selectBook(Scanner scanner) {
		System.out.print("Enter book url (eg. /novel/against-the-gods): ");
		var bookUrl = scanner.nextLine();
		return new WuxiaBook(bookUrl);
	}
}
