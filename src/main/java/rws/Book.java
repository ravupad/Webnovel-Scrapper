package rws;

public interface Book {
	String getTitle();

	int getChapterCount();

	Chapter selectChapter(String selector);
}
