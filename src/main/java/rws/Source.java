package rws;

import java.util.List;

public interface Source {
	List<Book> searchBook(String name);
	
	Book selectBook(String selection);
}
