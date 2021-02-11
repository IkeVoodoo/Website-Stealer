package semmieboy_yt.website_stealer.webserver;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

import static semmieboy_yt.website_stealer.Main.log;

public class Webserver {
    public Webserver(String website, int port, String runDirectory) {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
            server.createContext("/", new HttpHandler(website, runDirectory));
            server.setExecutor(null);
            server.start();
            log("Webserver started on port "+port);
        } catch (IOException exception) {
            exception.printStackTrace();
        }
    }
}
