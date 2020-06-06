package rws.readlightnovel;

import rws.Book;
import rws.Source;
import java.util.Scanner;

public class RLNSource implements Source {
    @Override
    public Book selectBook(Scanner scanner) throws Exception {
        System.out.print("Enter book url (eg. /god-of-slaughter.html): ");
        var chapterUrl = scanner.nextLine();
        return new RLNBook(chapterUrl);
    }
}
