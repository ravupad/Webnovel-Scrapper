package rws.sources.webnovel;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.JsonParser;
import rws.App;
import rws.IBook;

public class WebnovelBook implements IBook {
	String bookName;
	String bookId;
	
	@Override
	public String getBookTitle() {
		return bookName;
	}

	@Override
	public WebnovelChapter selectChapter(Scanner scanner) {
		var chapters = getChapters();
		var counter = 0;
		for(var chapter: chapters) {
			if(chapter.isVip != 0) {
				break;
			}
			counter += 1;
			System.out.println(String.format("%d. %s", counter, chapter.getChapterTitle()));
		}
		System.out.print("Select index of chpater to start download from: ");
		var selection = scanner.nextInt();
		scanner.nextLine();
		return new WebnovelChapter(bookId, chapters.get(selection - 1).id);
	}

	public String getId() {
		return bookId;
	}
	
	List<WebnovelChapter> getChapters() {
		try {
			URI uri = new URIBuilder(WebnovelSource.base_url + "/apiajax/chapter/GetChapterList")
					.setParameter("bookId", bookId).build();
			HttpGet request = new HttpGet(uri);
			var response = App.getClient().execute(request);
			var content = EntityUtils.toString(response.getEntity(), "UTF-8");
			var volumesJson = new JsonParser().parse(content).getAsJsonObject()
					.getAsJsonObject("data").getAsJsonArray("volumeItems");
			List<WebnovelChapter> chapters = new ArrayList<>();
			for(var volume: volumesJson) {
				for(var chapter: volume.getAsJsonObject().getAsJsonArray("chapterItems")) {
					chapters.add(new Gson().fromJson(chapter, WebnovelChapter.class));
				}
			}
			return chapters;
		} catch(Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
