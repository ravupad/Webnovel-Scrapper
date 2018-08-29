package rws.wuxiaworld;

import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import rws.Chapter;

public class WuxiaChapter implements Chapter {
	Document doc;

	WuxiaChapter(Document doc) {
		this.doc = doc;
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
			return new WuxiaChapter(WuxiaSource.getDocument(next));
		}
	}
}
