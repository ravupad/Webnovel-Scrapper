package rws.sources;

import org.jsoup.nodes.Document;
import rws.App;
import rws.IBook;
import rws.IChapter;
import rws.ISource;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class WuxiaWorldCo implements ISource, IBook, IChapter {
    private Document doc;
    private String url;

    public WuxiaWorldCo() {}

    public WuxiaWorldCo(String href) throws Exception {
        this.url = href;
        this.doc = App.getDocument(href);
    }

    @Override
    public IBook selectBook(Scanner scanner) throws Exception {
        System.out.print("Enter chapter url (eg. https://m.wuxiaworld.co/God-of-Slaughter/1784931.html): ");
        this.url = scanner.nextLine();
        this.doc = App.getDocument(this.url);
        return this;
    }

    @Override
    public String getBookTitle() {
        return doc.select("title").text().split("_")[0];
    }

    @Override
    public String getChapterTitle() {
        return doc.select("title").text().split("_")[1]
                .replace(" - Wuxiaworld", "");
    }

    @Override
    public List<String> getContent() {
        String body = doc.select("#read.read div#chaptercontent").outerHtml();
        body = body.split("</div>")[1];
        return Arrays.asList(body.split("<br>"))
                .stream()
                .map(a -> a.trim())
                .filter(a -> !a.equals(""))
                .filter(a -> !a.startsWith("<"))
                .collect(Collectors.toList());
    }

    @Override
    public IChapter next() throws Exception {
        String href = doc.getElementById("pt_next").attr("href");
        if (href.equals("./")) {
            return null;
        } else {
            String[] parts = url.split("/");
            parts[parts.length-1] = href;
            href = String.join("/", parts);
            return new WuxiaWorldCo(href);
        }
    }

    @Override
    public IChapter selectChapter(Scanner scanner) {
        return this;
    }
}
