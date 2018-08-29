package rws.wuxiaworld;

import java.util.Scanner;
import org.jsoup.nodes.Document;
import rws.Book;
import rws.Chapter;

public class WuxiaBook implements Book {
	Document doc;
	
	public WuxiaBook(String chapterUrl) {
		this.doc = WuxiaSource.getDocument(chapterUrl);
	}
	
	@Override
	public String getTitle() {
		return doc.select("li.caption a h4").get(0).text();
	}

	@Override
	public Chapter selectChapter(Scanner scanner) {
		return new WuxiaChapter(doc);
	}
}
