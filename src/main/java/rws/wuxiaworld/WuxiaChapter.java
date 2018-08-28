package rws.wuxiaworld;

import java.util.List;
import java.util.stream.Collectors;
import org.jsoup.nodes.Document;
import rws.Chapter;

public class WuxiaChapter implements Chapter {
	Document chapter;

	WuxiaChapter(Document chapter) {
		this.chapter = chapter;
	}

	@Override
	public String getTitle() {
		return chapter.select(".p-15 div > h4").get(0).text();
	}

	@Override
	public List<String> getContent() {
		return chapter.select(".fr-view > p").stream()
				.map(element -> element.text()).filter(text -> text.length() > 0)
				.collect(Collectors.toList());
	}

	@Override
	public WuxiaChapter next() {
		var next = chapter.select("ul.list-inline > li.next > a").get(0)
				.attr("href");
		if (next.equals("#")) {
			return null;
		} else {
			return new WuxiaChapter(
					WuxiaSource.getDocument(WuxiaSource.base_url + next));
		}
	}
}
