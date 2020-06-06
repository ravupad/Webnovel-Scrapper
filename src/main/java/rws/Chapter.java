package rws;

import java.util.List;

public interface Chapter {
	String getTitle();
	
	List<String> getContent();

	Chapter next() throws Exception;
}
