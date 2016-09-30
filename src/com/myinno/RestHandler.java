package com.myinno;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.org.apache.xpath.internal.functions.WrongNumberArgsException;

import javax.naming.NameNotFoundException;
import java.io.BufferedOutputStream;
import java.io.IOException;

/**
 * Processing REST requests.
 * Created by luckychess on 26/09/16.
 */
class RestHandler implements HttpHandler {

    static final String baseUrl = "/";

    @Override
    public void handle(HttpExchange httpExchange) {
        System.out.println("Got request " + httpExchange.getRequestURI().toString());
        String path = httpExchange.getRequestURI().getPath();
        String command = path.substring(path.indexOf(baseUrl) + baseUrl.length());
        System.out.println(command);
        String result;

        try {
            String splittedQuery[] = splitQuery(httpExchange.getRequestURI().getQuery());
            int n1 = parseArg(splittedQuery[0]);
            int n2 = parseArg(splittedQuery[1]);

            switch (command) {
                case "add":
                    result = Integer.toString(add(n1, n2));
                    break;
                case "sub":
                    result = Integer.toString(sub(n1, n2));
                    break;
                case "mul":
                    result = Integer.toString(mul(n1, n2));
                    break;
                case "div":
                    result = Integer.toString(div(n1, n2));
                    break;
                default:
                    result = "Command " + command + " is unknown. I know only add, sub, mul and div.";
                    break;
            }
        }

        catch (Exception e) {
            result = "Please use queries like add?n1=1&n2=2";
        }

        response(httpExchange, result);
    }

    private void response(HttpExchange httpExchange, String text) {
        try {
            httpExchange.sendResponseHeaders(200, 0);
            try (BufferedOutputStream out = new BufferedOutputStream(httpExchange.getResponseBody())) {
                out.write(text.getBytes());
                out.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    private String[] splitQuery(String input) throws WrongNumberArgsException {
        String[] args = input.split("&");
        if (args.length != 2) throw  new WrongNumberArgsException("2");
        return args;
    }

    private int parseArg(String arg) throws NumberFormatException, NameNotFoundException {
        String[] pair = arg.split("=");
        if (pair[0].equals("n1") || pair[0].equals("n2")) {
            return Integer.parseInt(pair[1]);
        }
        throw new NameNotFoundException();
    }

    private int add(int n1, int n2) {
        return n1 + n2;
    }

    private int sub(int n1, int n2) {
        return n1 - n2;
    }

    private int mul(int n1, int n2) {
        return n1 * n2;
    }

    private int div(int n1, int n2) {
        return n1 / n2;
    }
}
