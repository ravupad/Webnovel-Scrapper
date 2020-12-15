package rws;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import rws.sources.ReadNovelFull;
import rws.sources.RoyalRoad;
import rws.sources.WuxiaWorldCo;
import rws.sources.readlightnovel.RLNSource;
import rws.sources.webnovel.WebnovelSource;
import rws.sources.wuxiaworld.WuxiaSource;

public class Handler {
	Scanner scanner;
	ISource source;
	IBook book;
	IChapter chapter;
	int chapter_count;
	Writer out;

	public Handler() {
		scanner = new Scanner(System.in);
	}

	public void selectSource() throws Exception {
		source = null;
		System.out.println("1. WuxiaWorld");
		System.out.println("2. Webnovel");
		System.out.println("3. ReadLightNovels");
		System.out.println("4. WuxiaWorld.Co");
		System.out.println("5. RoyalRoad");
		System.out.println("6. ReadNovelFull");
		System.out.print("Select source: ");
		int selection = scanner.nextInt();
		scanner.nextLine();
		switch (selection) {
		case 1:
			source = new WuxiaSource();
			break;
		case 2:
			source = new WebnovelSource();
			break;
		case 3:
			source = new RLNSource();
			break;
		case 4:
			source = new WuxiaWorldCo();
			break;
		case 5:
			source = new RoyalRoad();
			break;
		case 6:
			source = new ReadNovelFull();
			break;
		default:
			System.out.println("Wrong selection of source: " + selection);
			throw new Exception();
		}
	}

	public void selectBook() throws Exception {
		book = source.selectBook(scanner);
	}

	public void selectChapter() {
		chapter = book.selectChapter(scanner);
		System.out.print("Enter maximum chapters to download: ");
		chapter_count = scanner.nextInt();
		scanner.nextLine();
	}

	public void fetchChapters() {
		try {
			System.out.println("Total chapters to download: " + chapter_count);
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(book.getBookTitle() + ".md"), "UTF-8"));
			var counter = 0;
			while (counter < chapter_count) {
				counter += 1;
				System.out.println(counter + ". " + chapter.getChapterTitle());
				writeChapter();
				chapter = chapter.next();
				if (chapter == null) {
					System.out.println("Next chapter not found");
					return;					
				}
			}
		} catch(Exception ex) {
			ex.printStackTrace();
		} finally {
			if(out != null) {
				try {
					out.close();
				} catch(Exception ex) {
					ex.printStackTrace();
				}
			}
		}		
	}

	void writeChapter() throws Exception {
		writeChapterTitle();
		out.write("\n");
		writeChapterContent();
		out.write("\n");
	}

	void writeChapterTitle() throws Exception {
		var title = chapter.getChapterTitle();
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
