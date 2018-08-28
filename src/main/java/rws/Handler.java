package rws;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;
import rws.wuxiaworld.WuxiaSource;

public class Handler {
	Scanner scanner;
	Source source;
	Book book;
	Chapter chapter;
	int chapter_count;
	Writer out;

	public Handler() {
		scanner = new Scanner(System.in);
	}

	public void selectSource() {
		source = null;
		System.out.println("Select source:");
		System.out.println("1. WuxiaWorld");
		System.out.println("2. Webnovel");
		int selection = scanner.nextInt();
		scanner.nextLine();
		if (selection == 1) {
			source = new WuxiaSource();
		}
	}

	public void selectBook() {
		System.out.print("Enter search string for book: ");
		var search_string = scanner.nextLine();
		var books = source.searchBook(search_string);
		System.out.println("Select book: ");
		int counter = 1;
		for (var book : books) {
			System.out.println(String.format("%d. %s", counter, book.getTitle()));
		}
		int selection = scanner.nextInt();
		scanner.nextLine();
		book = books.get(selection - 1);
	}

	public void selectChapter() {
		System.out
				.println("Number of chapters in book is: " + book.getChapterCount());
		System.out.print("Select starting chapter: ");
		String start = scanner.nextLine();
		chapter = book.selectChapter(start);
		System.out.print("Maximum chapters to download: ");
		chapter_count = scanner.nextInt();
		scanner.nextLine();
	}

	public void fetchChapters() throws Exception {
		out = new BufferedWriter(new OutputStreamWriter(
				new FileOutputStream(book.getTitle() + ".md"), "UTF-8"));
		while (chapter_count > 0) {
			chapter_count -= 1;
			writeChapter();
			chapter = chapter.next();
			if (chapter == null)
				return;
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
		var title = chapter.getTitle();
		out.write(title);
		out.write("\n");
		for (int i = 0; i < title.length(); i++) {
			out.write("=");
		}
		out.write("\n");
	}

	void writeChapterContent() throws Exception {
		var paras = chapter.getContent();
		for (var para : paras) {
			out.write(para);
			out.write("\n\n");
		}
	}
}
