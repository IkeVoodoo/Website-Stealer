package semmieboy_yt.website_stealer.webserver;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.file.Files;

import static semmieboy_yt.website_stealer.Main.log;

public class HttpHandler implements com.sun.net.httpserver.HttpHandler {
    String website;
    String runDirectory;
    Headers headers;

    public HttpHandler(String website, String runDirectory) {
        this.website = website;
        this.runDirectory = runDirectory;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String requestURI = httpExchange.getRequestURI().getPath();
        String sep = File.separator;
        File requestedFile = new File(runDirectory+requestURI.replace("/", sep));

        log("["+httpExchange.getRemoteAddress()+"] GET "+requestURI);

        if (requestURI.equals("/")) requestedFile = new File(runDirectory+sep+"index.html");

        if (!requestedFile.exists() | requestedFile.isDirectory()) {
            log("Don't have it yet, downloading...");
            String path = requestedFile.getPath();
            new File(path.substring(0, path.lastIndexOf(sep))).mkdirs();

            HttpURLConnection connection = (HttpURLConnection)new URL(website+requestURI).openConnection();
            headers = httpExchange.getRequestHeaders();
            for (String key:headers.keySet()) {
                String value = String.valueOf(headers.get(key));
                //connection.addRequestProperty(key, value.substring(1, value.length()-1));
                System.out.println(key+": "+value.substring(1, value.length()-1));
            }
            //Doesnt send any response headers
            connection.setDoOutput(true);
            ReadableByteChannel readableByteChannel = Channels.newChannel(connection.getInputStream());
            FileOutputStream fileOutputStream = new FileOutputStream(requestedFile.getPath());
            fileOutputStream.getChannel().transferFrom(readableByteChannel, 0, Long.MAX_VALUE);

            if (requestedFile.exists()){
                log("Done downloading");
            } else {
                log("Unable to download "+requestURI);
            }
        }

        byte[] fileBytes = Files.readAllBytes(requestedFile.toPath());
        httpExchange.sendResponseHeaders(200, fileBytes.length);
        OutputStream outputStream = httpExchange.getResponseBody();
        outputStream.write(fileBytes);
        outputStream.close();
    }
}
