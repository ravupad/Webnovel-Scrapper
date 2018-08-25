package rws;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
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
		Scanner scanner = null;
		Writer out = null;
		try {
			scanner = new Scanner(System.in);
			System.out.print("Enter url of first chapter: ");
			String url = scanner.nextLine();
			System.out.print("Enter max chapters to download: ");
			long max = Long.parseLong(scanner.nextLine());
			out = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream("test.html"), 
							"UTF-8"));
			String next = null;
			out.write("<html>\n<head><meta charset='utf-8'></head>\n<body>\n<div class='book'>\n");
	    while(max > 0) {
	    	var doc = getDocument(url);
	    	var title = getTitleFromWuxia(doc);
	    	var lines = getBodyFromWuxia(doc);
	    	next = getNextChapterFromWuxia(doc);
	    	url = wuxia_base + next;
	    	out.write("<div class='chapter'>\n");
	    	out.write("<h4>" + title + "</h4>\n");
	    	for(String line: lines) {
	    		out.write("<p>" + line + "</p>\n");
	    	}	    	
	    	out.write("</div>\n");
	    	if(next == null) break;
	    	max -= 1;
	    }
	    out.write("</div>\n</body>\n</html>\n");
		} catch(Exception ex) {
			throw ex;
		} finally {
			if(scanner != null) scanner.close();
			if(out != null) out.close();
		}
	}
	
	public static Document getDocument(String url) 
			throws ClientProtocolException, IOException {
		var content = Request.Get(url)
				.setHeader("User-Agent", ua)
				.execute().returnContent().asString();
		return Jsoup.parse(content);		
	}
	
	public static List<String> getBodyFromWuxia(Document doc) {
		return doc.select(".fr-view > p").stream()
				.map(element -> element.text())
				.collect(Collectors.toList());
	}
	
	public static String getTitleFromWuxia(Document doc) {
		return doc.select(".p-15 div > h4").get(0).text();
	}
	
	public static String getNextChapterFromWuxia(Document doc) {
		var next = doc.select("ul.list-inline > li.next > a").get(0).attr("href");
		if(next.equals("#")) {
			return null;
		} else return next;			
	}
}
