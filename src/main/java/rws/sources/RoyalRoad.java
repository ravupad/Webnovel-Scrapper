package rws.sources;

import org.jsoup.nodes.Document;
import rws.App;
import rws.IBook;
import rws.IChapter;
import rws.ISource;

import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class RoyalRoad implements ISource, IBook, IChapter {
    private Document doc;
    private String url;

    public RoyalRoad() {}

    public RoyalRoad(String href) throws Exception {
        this.url = href;
        this.doc = App.getDocument(href);
    }

    @Override
    public IBook selectBook(Scanner scanner) throws Exception {
        System.out.print("Enter chapter url (eg. https://www.royalroad.com/fiction/21220/mother-of-learning/chapter/301778/1-good-morning-brother): ");
        this.url = scanner.nextLine();
        this.doc = App.getDocument(this.url);
        return this;
    }

    @Override
    public String getBookTitle() {
        return doc
                .select(".fic-header a h2")
                .text();
    }

    @Override
    public String getChapterTitle() {
        return doc
                .select(".fic-header .text-center h1")
                .text();
    }

    @Override
    public List<String> getContent() {
        return doc
                .select(".chapter-content p")
                .stream()
                .map(a -> a.text())
                .map(a -> a.trim())
                .filter(a -> !a.equals(""))
                .collect(Collectors.toList());
    }

    @Override
    public IChapter next() throws Exception {
        if (doc.select(".chapter a:nth-child(3)") == null) return null;
        String href = doc.select(".chapter a:nth-child(3)")
                .attr("href");
        String url = this.url.replaceFirst("/fiction/.*", href);
        return new RoyalRoad(url);
    }

    @Override
    public IChapter selectChapter(Scanner scanner) {
        return this;
    }
}
