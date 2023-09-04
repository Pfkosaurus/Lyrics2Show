package com.matus.lyrics;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.http.HttpClient;
import java.util.ArrayList;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.json.JSONException;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;

import com.matus.lyrics.proceed.ImageGetter;
import com.matus.lyrics.proceed.ImageSequenceToGif;

public class RestCall {
	

	
	LyricsSelection lyricsSelection = new LyricsSelection();
	String song = null;
	String artist = null;
	private String lyrics;
	private List<String> lyricsAsList;
	ImageGetter im = new ImageGetter();
	
	public RestCall()
	{
		this.lyrics = lyrics;
		this.lyricsAsList = lyricsAsList;
	}
	
	@SuppressWarnings("static-access")
	public void fetchLyrics() throws IOException, JSONException {


		String apiKey =""; //not neccessary
		
		artist = lyricsSelection.getArtist();
		song = lyricsSelection.getSong();
		System.out.println("Artist: " +artist + " and song: " + song);
		String apiUrl = "http://api.chartlyrics.com/apiv1.asmx/SearchLyricDirect?artist=" + artist + "&song=" + song;

        URL url = new URL(apiUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        connection.setRequestProperty("Authorization", "Bearer " + apiKey);

        int responseCode = connection.getResponseCode();

        if (responseCode == HttpURLConnection.HTTP_OK) {
            BufferedReader in = new BufferedReader(new InputStreamReader(connection.getInputStream()));
            StringBuilder response = new StringBuilder();
            String line;

            while ((line = in.readLine()) != null) {
                response.append(line);
            }
            in.close();

            lyrics = response.toString();
            lyrics = convertResponse(lyrics);
            LyricsController.fixLyrics(lyrics);
            System.out.println("Full lyrics: ");
            System.out.println(lyrics);
            setLyrics(lyrics);
            LyricsController.eachWordToList(lyrics);
        } else {
        	System.out.println("There is no song such a " + song + " from" + artist + ". Are you sure you did not made a mistake?");
            throw new IOException("Failed to fetch lyrics. Response code: " + responseCode);
        }
    }

	private String convertResponse(String justLyrics) {
		try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(justLyrics));
            Document doc = builder.parse(is);

            NodeList lyricNodes = doc.getElementsByTagName("Lyric");
            if (lyricNodes.getLength() > 0) {
                Node lyricNode = lyricNodes.item(0);
                return lyricNode.getTextContent().trim();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;		
	}
	
	public String getLyrics() {
		return lyrics;
	}

	public void setLyrics(String lyrics) {
		this.lyrics = lyrics;
	}
	
	public List<String> getLyricsAsList() {
		return lyricsAsList;
	}

	public void setLyricsAsList(List<String> lyricsAsList) {
		this.lyricsAsList = lyricsAsList;
	}
	
	
}
