package rws.wuxiaworld;

import java.util.Arrays;
import java.util.List;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
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
			String ua = "Mozilla/5.0 " + "(X11; Linux x86_64) "
					+ "AppleWebKit/537.36 " + "(KHTML, like Gecko) "
					+ "Chrome/68.0.3440.106 " + "Safari/537.36";
			var content = Request.Get(url).setHeader("User-Agent", ua).execute()
					.returnContent().asString();
			return Jsoup.parse(content);
		} catch (Exception ex) {
			return null;
		}
	}
}
