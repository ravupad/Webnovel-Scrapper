package rws;

import java.util.Scanner;

public interface IBook {
	String getBookTitle();
	
	IChapter selectChapter(Scanner scanner);
}
