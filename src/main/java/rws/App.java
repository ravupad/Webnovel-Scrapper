package rws;

public class App {
	public static void main(String[] args) 
			throws Exception {
		Handler handler = new Handler();
		handler.selectSource();
		handler.selectBook();
		handler.selectChapter();
		handler.fetchChapters();
	}
	 
}
