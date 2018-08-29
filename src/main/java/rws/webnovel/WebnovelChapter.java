package rws.webnovel;

import java.net.URI;
import java.util.Arrays;
import java.util.List;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.util.EntityUtils;
import com.google.gson.JsonParser;
import rws.App;
import rws.Chapter;

public class WebnovelChapter implements Chapter {
	String id;
	String name;
	int isVip;
	String bookId;
	List<String> chapterContent;
	String nextChapterId;
	
	public WebnovelChapter(String bookId, String chapterId) {
		this.bookId = bookId;
		this.id = chapterId;
		fetchContent();
	}
	
	@Override
	public String getTitle() {
		return name;
	}

	@Override
	public List<String> getContent() {		
		return chapterContent;				
	}

	@Override
	public WebnovelChapter next() {
		var next = new WebnovelChapter(bookId, nextChapterId);
		if(next.isVip != 0) {
			return null;
		} else {
			return next;
		}
	}
	
	void fetchContent() {
		try {
			URI uri = new URIBuilder(WebnovelSource.base_url + "/apiajax/chapter/GetContent")
					.setParameter("bookId", bookId)
					.setParameter("chapterId", id).build();
			HttpGet request = new HttpGet(uri);
			var response = App.getClient().execute(request);
			var content = EntityUtils.toString(response.getEntity(), "UTF-8");
			var chapterJson = new JsonParser().parse(content).getAsJsonObject()
					.getAsJsonObject("data").getAsJsonObject("chapterInfo");
			name = chapterJson.getAsJsonPrimitive("chapterName").getAsString();
			isVip = chapterJson.getAsJsonPrimitive("SSPrice").getAsInt();
			chapterContent = Arrays.asList(chapterJson.getAsJsonPrimitive("content").getAsString());
			nextChapterId = chapterJson.getAsJsonPrimitive("nextChapterId").getAsString();			
		} catch(Exception ex) {
			ex.printStackTrace();
		}
	}
}
