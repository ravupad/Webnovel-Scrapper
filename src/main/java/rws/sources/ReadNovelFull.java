package rws.sources;

import org.jsoup.nodes.Document;
import rws.App;
import rws.IBook;
import rws.IChapter;
import rws.ISource;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class ReadNovelFull implements ISource, IBook, IChapter {
    private String url;
    private Document doc;

    @Override
    public String getBookTitle() {
        return doc.select("a.novel-title").text();
    }

    @Override
    public ReadNovelFull selectChapter(Scanner scanner) {
        return this;
    }

    @Override
    public String getChapterTitle() {
        return doc.select("span.chr-text").text();
    }

    @Override
    public List<String> getContent() {
        return doc.select("#chr-content p").stream()
                .map(e -> e.text())
                .collect(Collectors.toList());
    }

    @Override
    public ReadNovelFull next() throws Exception {
        var el = doc.select("#next_chap");
        if (el == null || el.hasAttr("disabled")) return null;
        this.url = "https://readnovelfull.com" + el.attr("href");
        this.doc = App.getDocument(this.url);
        return this;
    }

    @Override
    public ReadNovelFull selectBook(Scanner scanner) throws Exception {
        System.out.print("Enter chapter url: ");
        this.url = scanner.nextLine();
        this.doc = App.getDocument(this.url);
        return this;
    }
}
