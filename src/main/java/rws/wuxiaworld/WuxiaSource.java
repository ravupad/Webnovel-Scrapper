package rws.wuxiaworld;

import java.util.Arrays;
import java.util.List;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import rws.App;
import rws.Book;
import rws.Source;

public class WuxiaSource implements Source {
	public static final String base_url = "https://www.wuxiaworld.com";

	Document chapter;	

	@Override
	public List<Book> searchBook(String name) {
		chapter = getDocument(name);
		var book = new WuxiaBook(chapter);
		return Arrays.asList(book);
	}

	@Override
	public Book selectBook(String selection) {
		return new WuxiaBook(chapter);
	}

	public static Document getDocument(String url) {
		try {
			HttpGet request = new HttpGet(url);
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
}
