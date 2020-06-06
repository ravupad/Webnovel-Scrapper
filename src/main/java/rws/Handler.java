package rws;

import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.Scanner;

import rws.readlightnovel.RLNSource;
import rws.webnovel.WebnovelSource;
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
		System.out.println("1. WuxiaWorld");
		System.out.println("2. Webnovel");
		System.out.println("3. ReadLightNovels");
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
		default:
			System.out.println("Wrong selection of source.");
			source = null;
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
			out = new BufferedWriter(new OutputStreamWriter(
					new FileOutputStream(book.getTitle() + ".md"), "UTF-8"));
			var counter = 0;
			var progress = 0;
			while (counter < chapter_count) {
				counter += 1;
				writeChapter();
				chapter = chapter.next();
				progress = (counter*100)/chapter_count;
				System.out.print("[");
				for(int i = 0; i < 100; i++) {
					if(i <= progress) {
						System.out.print("=");
					} else {
						System.out.print(" ");
					}
				}
				System.out.print("]\r");
				if (chapter == null) {
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
