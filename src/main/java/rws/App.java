package rws;

import java.io.BufferedWriter;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.Writer;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;
import org.apache.http.client.fluent.Request;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

public class App {
	public static void main(String[] args) 
			throws Exception {
		Scanner scanner = null;	
		scanner = new Scanner(System.in);
		System.out.print("Enter url of first chapter: ");
		String url = scanner.nextLine();
		System.out.print("Enter max chapters to download: ");
		var max = Integer.parseInt(scanner.nextLine());
		WuxiaSource source = new WuxiaSource();
		var book = new Book(source, url, max);
		book.writeBook();
		scanner.close();
	}
	
	public interface Source {
		String getBaseUrl();
		String getBookTitle(Document chapter);
		String getChapterTitle(Document chapter);
		List<String> getChapterContent(Document chapter);
		String getNextChapterUrl(Document chapter);
	}
	
	public static class Book {
		Source source;
		String url;
		int max;
		Document chapter;
		String book_name;
		Writer out;		
		
		public Book(
				Source source, 
				String startUrl, 
				int max) throws Exception {
			this.source = source;
			this.url = startUrl;
			this.max = max;	
			chapter = getDocument();
			book_name = source.getBookTitle(chapter);
			out = new BufferedWriter(
					new OutputStreamWriter(
							new FileOutputStream(book_name + ".md"), 
							"UTF-8"));
		}
		
		public void writeBook() throws Exception {
			while(max > 0) {
				max -= 1;
				writeChapter();
				if(!getNextChapter()) return;				
			}
			out.close();
		}
		
		void writeChapter() throws Exception {
			writeChapterTitle();
			out.write("\n");
			writeChapterContent();
			out.write("\n");
		}
		
		void writeChapterTitle() throws Exception {
			var title = source.getChapterTitle(chapter);
			out.write(title);
			out.write("\n");
			for(int i = 0; i < title.length(); i++) {
				out.write("=");
			}
			out.write("\n");
		}
		
		void writeChapterContent() throws Exception {
			var paras = source.getChapterContent(chapter);
			for(var para: paras) {
				out.write(para);
				out.write("\n\n");
			}
		}
		
		boolean getNextChapter() throws Exception {
			String next = source.getNextChapterUrl(chapter);
			if(next == null) return false;
			url = source.getBaseUrl() + next;
			chapter = getDocument();
			return true;
		}
		
		Document getDocument() 
				throws Exception {
			String ua = 
					"Mozilla/5.0 "
					+ "(X11; Linux x86_64) "
					+ "AppleWebKit/537.36 "
					+ "(KHTML, like Gecko) "
					+ "Chrome/68.0.3440.106 "
					+ "Safari/537.36";
			var content = Request.Get(url)
					.setHeader("User-Agent", ua)
					.execute().returnContent().asString();
			return Jsoup.parse(content);		
		}
	}
	
	public static class WuxiaSource implements Source {

		@Override
		public String getBaseUrl() {
			return "https://www.wuxiaworld.com";
		}

		@Override
		public String getBookTitle(Document chapter) {
			return chapter.select("li.caption a h4").get(0).text();
		}

		@Override
		public String getChapterTitle(Document chapter) {
			return chapter.select(".p-15 div > h4").get(0).text();
		}

		@Override
		public List<String> getChapterContent(Document chapter) {
			return chapter.select(".fr-view > p").stream()
					.map(element -> element.text())
					.filter(text -> text.length() > 0)
					.collect(Collectors.toList());
		}

		@Override
		public String getNextChapterUrl(Document chapter) {
			var next = chapter.select("ul.list-inline > li.next > a").get(0).attr("href");
			if(next.equals("#")) {
				return null;
			} else return next;		
		}		
	}			
}
