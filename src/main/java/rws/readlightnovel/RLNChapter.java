package rws.readlightnovel;

import org.jsoup.nodes.Document;
import rws.App;
import rws.Chapter;
import java.util.List;
import java.util.stream.Collectors;

public class RLNChapter implements Chapter {
    Document doc;

    public RLNChapter(String url) throws Exception {
        this.doc = App.getDocument(url);
    }

    public String getBookTitle() {
        return doc.select(".chapter .truyen-title").text();
    }

    @Override
    public String getChapterTitle() {
        return doc.select(".chapter .chapter-title").text();
    }

    @Override
    public List<String> getContent() {
        return doc.select(".chapter .chapter-content p")
                .stream()
                .map(e -> e.text())
                .filter(t -> t.length() != 0)
                .collect(Collectors.toList());
    }

    @Override
    public Chapter next() throws Exception {
        String href = doc.getElementById("next_chap").attr("href");
        if (href.equals("javascript:void(0)")) return null;
        return new RLNChapter(href);
    }
}
