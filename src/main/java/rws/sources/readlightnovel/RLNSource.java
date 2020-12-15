package rws.sources.readlightnovel;

import rws.IBook;
import rws.ISource;
import java.util.Scanner;

public class RLNSource implements ISource {
    @Override
    public IBook selectBook(Scanner scanner) throws Exception {
        System.out.print("Enter book url (eg. /god-of-slaughter.html): ");
        var chapterUrl = scanner.nextLine();
        return new RLNBook(chapterUrl);
    }
}
