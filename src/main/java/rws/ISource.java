package rws;

import java.util.Scanner;

public interface ISource {
	IBook selectBook(Scanner scanner) throws Exception;
}
