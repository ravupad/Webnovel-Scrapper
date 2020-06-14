package rws;

import java.util.List;

public interface Chapter {
	String getChapterTitle();
	
	List<String> getContent();

	Chapter next() throws Exception;
}
