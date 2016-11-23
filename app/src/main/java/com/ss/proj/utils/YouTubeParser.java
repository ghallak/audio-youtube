package com.ss.proj.utils;

import org.apache.commons.io.IOUtils;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class YouTubeParser {
    private static final String DOMAIN = "https://www.youtube.com/";
    private static final String WATCH_CMD = "watch?v=";

    private final String pageSource;

    public YouTubeParser(String videoId) throws IOException {
        try {
            URL url = new URL(DOMAIN + WATCH_CMD + videoId);
            InputStream inputStream = url.openStream();
            pageSource = IOUtils.toString(inputStream, "UTF-8");
        } catch (MalformedURLException e) {
            System.err.println("song url is not correct");
            throw e;
        } catch (IOException e) {
            System.err.println("ioexception");
            throw e;
        }
    }

    public List<URL> getAudioURLs() throws MalformedURLException, UnsupportedEncodingException {
        final String pattern = "(?:url=)(https((?!\\s|\\\\).)*?(?:googlevideo)(?:(?!\\s|\\\\).)*?audio.*?)(?:\\\\)";

        Matcher matcher = Pattern.compile(pattern).matcher(pageSource);

        List<URL> audioURLs = new ArrayList<URL>();
        while (matcher.find()) {
            String url = matcher.group(1);
            try {
                url = URLDecoder.decode(url, "UTF-8");
                audioURLs.add(new URL(url));
            } catch (UnsupportedEncodingException e) {
                System.err.println("unsuppoted url encoding");
                throw e;
            } catch (MalformedURLException e) {
                System.err.println("audio url is not correct");
                throw e;
            }
        }
        return audioURLs;
    }
}
