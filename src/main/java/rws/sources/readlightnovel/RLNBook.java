package rws.sources.readlightnovel;

import rws.IBook;
import java.util.Scanner;

public class RLNBook implements IBook {
    RLNChapter chapter;

    public RLNBook(String chapterUrl) throws Exception {
        this.chapter = new RLNChapter(chapterUrl);
    }

    @Override
    public String getBookTitle() {
        return chapter.getBookTitle();
    }

    @Override
    public RLNChapter selectChapter(Scanner scanner) {
        return chapter;
    }
}
