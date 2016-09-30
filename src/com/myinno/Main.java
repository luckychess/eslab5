package com.myinno;

import com.sun.net.httpserver.HttpServer;

import java.io.IOException;
import java.net.InetSocketAddress;

public class Main {

    private static final int port = 8000;

    public static void main(String[] args) {
        HttpServer server;
        try {
            server = HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        server.createContext(RestHandler.baseUrl, new RestHandler());
        server.setExecutor(java.util.concurrent.Executors.newFixedThreadPool(4));
        System.out.println("Starting listening on port " + port);
        server.start();
    }
}
