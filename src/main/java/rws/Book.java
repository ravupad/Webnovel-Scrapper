package rws;

import java.util.Scanner;

public interface Book {	
	String getTitle();
	
	Chapter selectChapter(Scanner scanner);
}
