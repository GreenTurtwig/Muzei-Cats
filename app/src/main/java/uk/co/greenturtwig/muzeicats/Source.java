package uk.co.greenturtwig.muzeicats;

import android.content.Intent;
import android.net.Uri;

import com.google.android.apps.muzei.api.Artwork;
import com.google.android.apps.muzei.api.MuzeiArtSource;
import com.google.android.apps.muzei.api.RemoteMuzeiArtSource;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.Random;

public class Source extends RemoteMuzeiArtSource {

    String key = "oR6jyM7Ws3eqnrzoKCqMIhHyKJ1U4NUR0ROz2DTj";

    public Source() {
        super("Muzei Cats");
    }

    @Override
    public void onCreate() {
        super.onCreate();
        setUserCommands(BUILTIN_COMMAND_ID_NEXT_ARTWORK);
    }

    protected void onTryUpdate(int reason) {

        String url = "";
        String title = "";
        String by = "";
        String link = "";

        Random r = new Random();
        int imageRandom = r.nextInt(20 - 0) + 0;
        int pageRandom = r.nextInt(200 - 1) + 1;


        try {
            HttpGet httppost = new HttpGet("https://api.500px.com/v1/photos/search?consumer_key=oR6jyM7Ws3eqnrzoKCqMIhHyKJ1U4NUR0ROz2DTj&term=cat&image_size=6&page=" + pageRandom);
            HttpClient httpclient = new DefaultHttpClient();
            HttpResponse response = httpclient.execute(httppost);

            // StatusLine stat = response.getStatusLine();
            int status = response.getStatusLine().getStatusCode();

            if (status == 200) {
                HttpEntity entity = response.getEntity();
                String data = EntityUtils.toString(entity);

                JSONObject jsono = new JSONObject(data);
                JSONArray jsona = jsono.getJSONArray("photos");

                JSONObject image = jsona.getJSONObject(imageRandom);
                JSONObject user = image.getJSONObject("user");




                url = image.getString("image_url");
                title = image.getString("name");
                by = user.getString("username");
                link = "https://500px.com/photo/" + image.getString("id");
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }



        publishArtwork(new Artwork.Builder()
                .imageUri(Uri.parse(url))
                .title(title)
                .byline(by)
                .viewIntent(new Intent(Intent.ACTION_VIEW, Uri.parse(link)))
                .build());
    }

}
