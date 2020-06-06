package rws.readlightnovel;

import rws.Book;
import java.util.Scanner;

public class RLNBook implements Book {
    RLNChapter chapter;

    public RLNBook(String chapterUrl) throws Exception {
        this.chapter = new RLNChapter(chapterUrl);
    }

    @Override
    public String getTitle() {
        return chapter.getBookTitle();
    }

    @Override
    public RLNChapter selectChapter(Scanner scanner) {
        return chapter;
    }
}
