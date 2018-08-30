package rws.wuxiaworld;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import rws.Book;

public class WuxiaBook implements Book {
	Document doc;
	String bookUrl;

	public WuxiaBook(String bookUrl) {
		this.bookUrl = bookUrl;
	}

	@Override
	public String getTitle() {
		return doc.select("div.p-15 > h4").get(0).text();
	}

	@Override
	public WuxiaChapter selectChapter(Scanner scanner) {
		try {
			doc = WuxiaSource.getDocument(bookUrl);
			var chapters = getChapters();
			var counter = 0;
			for(var chapter: chapters) {
				counter += 1;
				System.out.println(String.format("%d. %s", counter, chapter.title));				
			}
			System.out.print("Enter chapter to start download from: ");
			var selection = scanner.nextInt();
			scanner.nextLine();
			var chapter = chapters.get(selection - 1);
			if(!chapter.fetchChapter()) {
				return null;
			} else {
				return chapter;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	public List<WuxiaChapter> getChapters() {
		return doc.select("li.chapter-item a").stream()
				.map(element -> new WuxiaChapter(element.attr("href"),
						element.select("span").text()))
				.collect(Collectors.toList());
	}
}
