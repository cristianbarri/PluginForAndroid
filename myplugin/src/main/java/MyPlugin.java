import android.app.Application;
import android.content.Context;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.judith.myplugin.CustomMediaPlayer;
import com.example.judith.myplugin.CustomVideoView;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by Judith on 15/11/2015.
 */
public class MyPlugin {
    private int PlayCounter = 0;
    private int PauseCounter = 0;
    private long pause = 0;
    private long start = 0;
    private long TimeCounter = 0;

    boolean firstPlay = true;
    boolean firstFrame = true;
    private String video_event;
    private static final String DEBUG_TAG = "HttpExample";

    public MyPlugin(final Application app, CustomMediaPlayer mp) {
        //PlayPause Listener
        mp.setPlayPauseListener(new CustomMediaPlayer.PlayPauseListener1() {
            @Override
            public void onPlay() {
                //Play listener
                if (firstPlay) { //FIRST PLAY (HTTP Request)
                    firstPlay = false;
                    video_event = "START";
                    String stringUrl = "http://fakeweb.com/event?param=" + video_event;
                    ConnectivityManager connMgr = (ConnectivityManager)
                            app.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        new DownloadWebpageTask().execute(stringUrl);
                    } else {
                        //"No network connection available.
                        Toast.makeText(app, "No network connection available", Toast.LENGTH_LONG).show();
                    }
                    //HTTP Request done in first play
                }
                CountPlay(app.getApplicationContext());
            }

            @Override
            public void onPause() {
                //Pause listener
                CountPause(app.getApplicationContext());
            }
        });
        //FIRST FRAME (HTTP Request)
        mp.setOnBufferingUpdateListener(new MediaPlayer.OnBufferingUpdateListener() {
            @Override
            public void onBufferingUpdate(MediaPlayer mp, int percent) {
                if (firstFrame) {
                    firstFrame = false;
                    video_event = "FRAME";
                    String stringUrl = "http://fakeweb.com/event?param=" + video_event;
                    ConnectivityManager connMgr = (ConnectivityManager)
                            app.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        new DownloadWebpageTask().execute(stringUrl);
                    } else {
                        //"No network connection available.
                        Toast.makeText(app, "No network connection available", Toast.LENGTH_LONG).show();
                    }
                    //HTTP Request done in FIRST FRAME
                }
            }
        });

        //VIDEO FINISH (HTTP Request and show Plays/Pauses)
        mp.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Gets the URL from the UI's text field.
                video_event = "FINISH";
                String stringUrl = "http://fakeweb.com/event?param="+video_event;
                ConnectivityManager connMgr = (ConnectivityManager)
                        app.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadWebpageTask().execute(stringUrl);
                } else {
                    //"No network connection available.
                    Toast.makeText(app, "No network connection available", Toast.LENGTH_LONG).show();
                }
                //HTTP Request done in video finish
                Toast.makeText(app, "Plays: " + PlayCounter + " Pauses: " + PauseCounter, Toast.LENGTH_LONG).show();
            }
        });
    }

    public MyPlugin(final Application app, CustomVideoView v) {

        //PlayPause Listener
        v.setPlayPauseListener(new CustomVideoView.PlayPauseListener() {
            @Override
            public void onPlay() {
                //Play listener
                if (firstPlay) {//FIRST PLAY (HTTP Request)
                    firstPlay = false;
                    video_event = "START";
                    String stringUrl = "http://fakeweb.com/event?param=" + video_event;
                    ConnectivityManager connMgr = (ConnectivityManager)
                            app.getSystemService(Context.CONNECTIVITY_SERVICE);

                    NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                    if (networkInfo != null && networkInfo.isConnected()) {
                        new DownloadWebpageTask().execute(stringUrl);
                    } else {
                        //"No network connection available.
                        Toast.makeText(app, "No network connection available", Toast.LENGTH_LONG).show();
                    }
                    //HTTP Request done in first play
                }
                CountPlay(app.getApplicationContext());
            }

            @Override
            public void onPause() {
                //Pause listener
                CountPause(app.getApplicationContext());
            }
        });
        //FIRST FRAME (HTTP Request)
        //How??

        //VIDEO FINISH
        v.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                // Gets the URL from the UI's text field.
                video_event = "FINISH";
                String stringUrl = "http://fakeweb.com/event?param="+video_event;
                ConnectivityManager connMgr = (ConnectivityManager)
                        app.getSystemService(Context.CONNECTIVITY_SERVICE);

                NetworkInfo networkInfo = connMgr.getActiveNetworkInfo();

                if (networkInfo != null && networkInfo.isConnected()) {
                    new DownloadWebpageTask().execute(stringUrl);
                } else {
                    //"No network connection available.
                    Toast.makeText(app, "No network connection available", Toast.LENGTH_LONG).show();
                }
                //HTTP Request done in video finish
                Toast.makeText(app, "Plays: " + PlayCounter + " Pauses: " + PauseCounter, Toast.LENGTH_LONG).show();
            }
        });

    }

    // Uses AsyncTask to create a task away from the main UI thread. This task takes a
    // URL string and uses it to create an HttpUrlConnection. Once the connection
    // has been established, the AsyncTask downloads the contents of the webpage as
    // an InputStream. Finally, the InputStream is converted into a string, which is
    // displayed in the UI by the AsyncTask's onPostExecute method.
    private class DownloadWebpageTask extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... urls) {

            // params comes from the execute() call: params[0] is the url.
            try {
                return downloadUrl(urls[0]);
            } catch (IOException e) {
                return "Unable to retrieve web page. URL may be invalid.";
            }
        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {
            Log.d("Result", "The result is: " + result);
        }
    }

    // Given a URL, establishes an HttpUrlConnection and retrieves
    // the web page content as a InputStream, which it returns as
    // a string.
    private String downloadUrl(String myurl) throws IOException {
        InputStream is = null;
        // Only display the first 500 characters of the retrieved
        // web page content.
        int len = 500;

        try {
            URL url = new URL(myurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            // Starts the query
            conn.connect();
            int response = conn.getResponseCode();
            Log.d(DEBUG_TAG, "The response is: " + response);
            is = conn.getInputStream();

            // Convert the InputStream into a string
            String contentAsString = readIt(is, len);
            return contentAsString;

            // Makes sure that the InputStream is closed after the app is
            // finished using it.
        } finally {
            if (is != null) {
                is.close();
            }
        }
    }

    // Reads an InputStream and converts it to a String.
    public String readIt(InputStream stream, int len) throws IOException, UnsupportedEncodingException {
        Reader reader = null;
        reader = new InputStreamReader(stream, "UTF-8");
        char[] buffer = new char[len];
        reader.read(buffer);
        return new String(buffer);
    }

    public void CountPlay(Context context) {
        if (pause > 0) {
            PlayCounter++;
            start = System.currentTimeMillis();
            TimeCounter = start - pause;
            Toast.makeText(context, "Plays: " + PlayCounter + " / Elapsed time: " + TimeCounter + "ms", Toast.LENGTH_SHORT).show();
        }
    }

    public void CountPause(Context context) {
        PauseCounter++;
        pause = System.currentTimeMillis();
        Toast.makeText(context, "Pauses: " + PauseCounter, Toast.LENGTH_SHORT).show();
    }
}