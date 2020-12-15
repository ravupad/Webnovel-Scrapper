package rws.sources.webnovel;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import org.apache.http.NameValuePair;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import rws.App;
import rws.ISource;

public class WebnovelSource implements ISource {
	public static final String base_url = "https://www.webnovel.com";

	public List<WebnovelBook> searchBook(String name) {
		try {
			List<NameValuePair> form = new ArrayList<>();
			form.add(new BasicNameValuePair("keywords", name));
			UrlEncodedFormEntity entity = new UrlEncodedFormEntity(form, "UTF-8");
			HttpPost request = new HttpPost(
					base_url + "/apiajax/search/AutoCompleteAjax");
			request.setEntity(entity);
			var response = App.getClient().execute(request);
			var content = EntityUtils.toString(response.getEntity(), "UTF-8");
			var booksJson = new JsonParser().parse(content).getAsJsonObject()
					.getAsJsonObject("data").getAsJsonArray("bookItems");
			var books = new ArrayList<WebnovelBook>();
			for(JsonElement book: booksJson) {
				books.add(new Gson().fromJson(book, WebnovelBook.class));
			}
			return books;
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@Override
	public WebnovelBook selectBook(Scanner scanner) {
		System.out.print("Search book: ");
		var name = scanner.nextLine();
		var books = searchBook(name);
		var counter = 0;
		for(var book: books) {
			counter += 1;
			System.out.println(String.format("%d. %s", counter, book.getBookTitle()));
		}
		System.out.print("Select book: ");
		var selection = scanner.nextInt();
		scanner.nextLine();
		return books.get(selection - 1);
	}
}
