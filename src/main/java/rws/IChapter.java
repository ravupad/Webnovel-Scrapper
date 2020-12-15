package rws;

import java.util.List;

public interface IChapter {
	String getChapterTitle();
	
	List<String> getContent();

	IChapter next() throws Exception;
}
