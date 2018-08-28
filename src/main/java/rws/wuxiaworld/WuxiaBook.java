package rws.wuxiaworld;

import org.jsoup.nodes.Document;
import rws.Book;
import rws.Chapter;

public class WuxiaBook implements Book {
	Document book;
	
	public WuxiaBook(Document book) {
		this.book = book;
	}
	
	@Override
	public String getTitle() {
		return book.select("li.caption a h4").get(0).text();
	}

	@Override
	public int getChapterCount() {
		return -1;
	}

	@Override
	public Chapter selectChapter(String selector) {
		return new WuxiaChapter(book);
	}
}
