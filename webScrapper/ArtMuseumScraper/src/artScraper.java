import java.io.FileWriter;
import java.io.IOException;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

/**
 * 
 * @author priyakotwal
 * Wrapper/scrapper to get data from a museum Web site using jsoup. Site: http://www.artbma.org
 */

public class artScraper {
	public static void main(String[] args) {

		Document doc;
		try {

			//	doc = Jsoup.connect("http://collection.artbma.org/emuseum/view/objects/asimages/3210?t:state:flow=06edea01-17f8-49cd-a8e7-c0a92f273778").get();
			
			doc = Jsoup.connect("http://www.artbma.org").get();
			Elements category = doc.select("li.section3");
			Element categoryItem = category.select("a[href]").get(8);
			System.out.println("catItem: "+categoryItem.absUrl("href"));
			doc = Jsoup.connect(categoryItem.absUrl("href")).get();
			Elements selCategory = doc.select("div#eBrowse");
			Element selCategoryItem = selCategory.select("a[href]").get(0);
			System.out.println("selCategory: "+selCategoryItem.absUrl("href"));
			doc = Jsoup.connect(selCategoryItem.absUrl("href")).get();
			Elements mediaLinks = doc.select("div.result");
			Element mediaLink = mediaLinks.select("a[href]").get(0);
			String firstImg = mediaLink.absUrl("href");
			System.out.println("mediaLink: "+firstImg);
		
			doc = Jsoup.connect(firstImg).get();
			// ************working
		//	doc = Jsoup.connect("http://collection.artbma.org/emuseum/view/objects/asitem/3210/0/title-asc?t:state:flow=07b13d46-f84b-45cf-bcc0-c6cfa970fbbd").get();
		//	doc = Jsoup.connect("http://collection.artbma.org/emuseum/view/objects/asitem/search@swg'1700','PAINTING'/0/title-asc?t:state:flow=d8a08dff-440f-4a29-a710-9e8978897e27").get();
			
			// get page title
			String title = doc.title();
			System.out.println("title : " + title);
			
			Elements next = doc.select("div.pagenavright"); 

			Element a1 = next.select("a[href]").get(0); //0 is the index first element increasing to (elems.size()-1)
			System.out.println("url1: "+a1.absUrl("href"));


			Element a2 = next.select("a[href]").get(1); //0 is the index first element increasing to (elems.size()-1)
		//	System.out.println("url2: "+a2.absUrl("href"));

		    FileWriter file = new FileWriter("/Users/priyakotwal/Desktop/dataset3.json");
			JSONObject obj = new JSONObject();
			JSONArray array = new JSONArray();
			
			int counter = 1;
			while(counter!=0){

				Elements info = doc.select("div#singledata"); 	
				Element artistData = info.select("strong:matchesOwn(Artist:)").first();
				if(artistData != null)
					//	if((info.select("strong:matchesOwn(Artist:)").first().nextSibling().toString()!=null)||!(info.select("strong:matchesOwn(Manufacturer:)").isEmpty()))
				{

					JSONObject objItem = new JSONObject();

					objItem.put("Painting", counter);
					objItem.put("Title", info.select("div").get(0).child(0).text());
					objItem.put("Date", info.select("span:matchesOwn(Date:)").first().nextSibling().toString());
					objItem.put("Artist: ",info.select("strong:matchesOwn(Artist:)").first().nextSibling().toString());
					objItem.put("Dimensions: ",info.select("span:matchesOwn(Dimensions:)").first().nextSibling().toString());
					objItem.put("Medium: ",info.select("span:matchesOwn(Medium:)").first().nextSibling().toString());
					objItem.put("Credit Line: ",info.select("span:matchesOwn(Credit Line:)").first().nextSibling().toString());
					objItem.put("Object Number: ",info.select("span:matchesOwn(Object Number:)").first().nextSibling().toString());
				//	file.write("\n"+objItem.toJSONString());


					System.out.println(counter+": Title: "+info.select("div").get(0).child(0).text());
					System.out.println("Date: " + info.select("span:matchesOwn(Date:)").first().nextSibling().toString());
					System.out.println("Artist: " + info.select("strong:matchesOwn(Artist:)").first().nextSibling().toString());
					System.out.println("Dimensions: " + info.select("span:matchesOwn(Dimensions:)").first().nextSibling().toString());
					System.out.println("Medium: " + info.select("span:matchesOwn(Medium:)").first().nextSibling().toString());
					System.out.println("Credit Line: " + info.select("span:matchesOwn(Credit Line:)").first().nextSibling().toString());
					System.out.println("Object Number: " + info.select("span:matchesOwn(Object Number:)").first().nextSibling().toString()); 

					//obj.put("artwork: "+counter, objItem);
					array.add(objItem);
				//	file.write(objItem.toJSONString());



				}
				doc = Jsoup.connect(a1.absUrl("href")).timeout(0).get();
				next = doc.select("div.pagenavright"); 
				a1 = next.select("a[href]").get(0);
				counter++;
				if(counter == 350)
				{
					counter = 0;
				}

			}		
				obj.put("Artwork" , array);
			try {

				//FileWriter file = new FileWriter("/Users/priyakotwal/Desktop/HW1DATA.json");
				file.write(obj.toJSONString());
				file.flush();
				file.close();

			} catch (IOException e) {
				e.printStackTrace();
			} 

			System.out.print(obj);
 
		} catch (IOException e) {
			e.printStackTrace();
		}

	}


}
