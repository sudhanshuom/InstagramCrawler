package instagramcrawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.URL;
import java.nio.charset.Charset;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 *
 * @author Sudhanshu
 */
public class InstagramCrawler {

    static String hashtag = "fashion";
    public static void main(String[] args) throws IOException, JSONException {
        
        int cc = 0;
        System.out.println("100 users who have used #fashion in any of their Instagram posts:");
        while(cc < 100){
            try{
                JSONObject json = readJsonFromUrl("https://www.instagram.com/explore/tags/" + hashtag + "/?__a=1");
                //System.out.println(json);
                //System.out.println("\n\n\n\n\n\n\n\n\n\n");
                JSONObject j1 = (JSONObject) json.get("graphql");
                JSONObject j2 = (JSONObject) j1.get("hashtag");
                JSONObject j3 = (JSONObject) j2.get("edge_hashtag_to_media");
                JSONArray nodes = j3.getJSONArray("edges");

                for(int i = 0; i < nodes.length() && cc < 100; i++){
                    JSONObject post = (JSONObject) nodes.get(i);
                    JSONObject pos = (JSONObject) post.getJSONObject("node");
                    String po = pos.get("shortcode").toString();

                    //System.out.println(po);
                    JSONObject user = readJsonFromUrl("https://www.instagram.com/p/" + po + "/?__a=1");            
                    String username  = user.getJSONObject("graphql")
                            .getJSONObject("shortcode_media")
                            .getJSONObject("owner")
                            .get("username").toString();
                    System.out.print(cc+1 + " username:  " + username);

                    JSONObject userp = readJsonFromUrl("https://www.instagram.com/" + username + "/?__a=1");            
                    String followercount  = userp.getJSONObject("graphql")
                            .getJSONObject("user")
                            .getJSONObject("edge_followed_by")
                            .get("count").toString();

                    System.out.println("   followers:   " + followercount);

                    cc++;
                    //System.out.println("\n");
                }
            }catch(Exception e){}
        }
        
        
    }
    
    private static String readAll(Reader rd) throws IOException {
    StringBuilder sb = new StringBuilder();
    int cp;
    while ((cp = rd.read()) != -1) {
      sb.append((char) cp);
    }
    return sb.toString();
  }

  public static JSONObject readJsonFromUrl(String url) throws IOException, JSONException {
    InputStream is = new URL(url).openStream();
    try {
      BufferedReader rd = new BufferedReader(new InputStreamReader(is, Charset.forName("UTF-8")));
      String jsonText = readAll(rd);
      JSONObject json = new JSONObject(jsonText);
      return json;
    } finally {
      is.close();
    }
  }
    
}
