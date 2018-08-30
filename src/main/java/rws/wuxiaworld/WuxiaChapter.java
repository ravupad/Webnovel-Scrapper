package rws.wuxiaworld;

import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import rws.Chapter;

public class WuxiaChapter implements Chapter {
	Document doc;
	String title;
	String chapterUrl;

	WuxiaChapter(String chapterUrl, String title) {
		this.chapterUrl = chapterUrl;
		this.title = title;
	}

	@Override
	public String getTitle() {
		return doc.select(".p-15 div > h4").get(0).text();
	}

	@Override
	public List<String> getContent() {
		return doc.select(".fr-view > p").stream()
				.map(element -> element.text()).filter(text -> text.length() > 0)
				.collect(Collectors.toList());
	}

	@Override
	public WuxiaChapter next() {
		var next = doc.select("ul.list-inline > li.next > a").get(0)
				.attr("href");
		if (next.equals("#")) {
			return null;
		} else {
			var chapter = new WuxiaChapter(next, null);
			if(!chapter.fetchChapter()) {
				return null;
			} else {
				return chapter;
			}
		}
	}
	
	public boolean fetchChapter() {
		doc = WuxiaSource.getDocument(chapterUrl);
		if(doc.select(".p-15 div > h4").size() == 0) {
			return false;
		} else {
			return true;
		}
	}
}
