package rws;

import java.util.Scanner;

public interface Book {	
	String getBookTitle();
	
	Chapter selectChapter(Scanner scanner);
}
