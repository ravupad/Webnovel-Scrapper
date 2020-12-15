package rws.sources.webnovel;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import com.google.gson.JsonParser;
import rws.App;
import rws.IChapter;

public class WebnovelChapter implements IChapter {
	String id;
	String name;
	int isVip;
	String bookId;
	List<String> chapterContent;
	String nextChapterId;
	int isRichFormat;

	public WebnovelChapter(String bookId, String chapterId) {
		this.bookId = bookId;
		this.id = chapterId;
		fetchContent();
	}

	@Override
	public String getChapterTitle() {
		return name;
	}

	@Override
	public List<String> getContent() {
		return chapterContent;
	}

	@Override
	public WebnovelChapter next() {
		var next = new WebnovelChapter(bookId, nextChapterId);
		if (next.isVip != 0) {
			return null;
		} else {
			return next;
		}
	}

	void fetchContent() {
		try {
			URI uri = new URIBuilder(
					WebnovelSource.base_url + "/apiajax/chapter/GetContent")
							.setParameter("bookId", bookId).setParameter("chapterId", id)
							.build();
			HttpGet request = new HttpGet(uri);
			var response = App.getClient().execute(request);
			var content = EntityUtils.toString(response.getEntity(), "UTF-8");
			var chapterJson = new JsonParser().parse(content).getAsJsonObject()
					.getAsJsonObject("data").getAsJsonObject("chapterInfo");
			name = chapterJson.getAsJsonPrimitive("chapterName").getAsString();
			isVip = chapterJson.getAsJsonPrimitive("SSPrice").getAsInt();
			var initialContent = chapterJson.getAsJsonPrimitive("content")
					.getAsString();
			nextChapterId = chapterJson.getAsJsonPrimitive("nextChapterId")
					.getAsString();
			isRichFormat = chapterJson.getAsJsonPrimitive("isRichFormat").getAsInt();
			chapterContent = new ArrayList<>();
			if (isRichFormat == 0) {
				var split = initialContent.split("\\r\\n");
				for (var line : split) {
					chapterContent.add(line);
				}
			} else {
				var doc = Jsoup.parse(initialContent);
				chapterContent = doc.select("p").stream().map(element -> element.text())
						.collect(Collectors.toList());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
