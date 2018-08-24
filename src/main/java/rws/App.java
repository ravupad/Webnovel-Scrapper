package rws;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class App {
	public static final String ua = 
			"Mozilla/5.0 (X11; Linux x86_64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/68.0.3440.106 Safari/537.36";
	public static final String wuxia_base = "https://www.wuxiaworld.com";
	
	public static void main(String[] args) 
			throws IOException {
		BufferedWriter out = null;
		FileWriter fstream = new FileWriter("out.html", true);
		var url = "https://www.wuxiaworld.com/novel/emperors-domination/emperor-chapter-1197";
		try {
			out = new BufferedWriter(fstream);
	    while(true) {
	    	var doc = getDocument(url);
	    	var body_as_string = getBodyFromWuxia(doc);
	    	out.write(body_as_string);
	    	var next = getNextChapterFromWuxia(doc);
	    	if(next == null) break;
	    	url = wuxia_base + next;
	    }
		} catch(Exception ex) {
			System.out.println("Exception caught: " + ex.toString());
		}
		finally {
			if(out != null) {
				out.close();
			}
		}
	}
	
	public static Document getDocument(String url) 
			throws ClientProtocolException, IOException {
		var content = Request.Get(url)
				.setHeader("User-Agent", ua)
				.execute().returnContent().asString();
		return Jsoup.parse(content);		
	}
	
	public static String getBodyFromWuxia(Document doc) {
		return doc.getElementsByClass("fr-view").get(0).toString();
	}
	
	public static String getNextChapterFromWuxia(Document doc) {
		var next = doc.select("ul.list-inline > li.next > a").get(0).attr("href");
		if(next.equals("#")) {
			return null;
		} else return next;			
	}
}
