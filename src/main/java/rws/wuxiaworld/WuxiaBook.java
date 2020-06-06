package rws.wuxiaworld;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import rws.Book;

public class WuxiaBook implements Book {
	Document doc;
	String bookUrl;
    List<WuxiaChapter> chapters;

	public WuxiaBook(String bookUrl) {
		this.bookUrl = bookUrl;
        this.doc = WuxiaSource.getDocument(bookUrl);
        this.chapters = getChapters();
	}

	@Override
	public String getTitle() {
		return doc.select(".novel-top h2").get(0).text();
	}

	@Override
	public WuxiaChapter selectChapter(Scanner scanner) {
		try {
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
				.map(element -> new WuxiaChapter(element.attr("href"), element.text()))
				.collect(Collectors.toList());
	}
}
