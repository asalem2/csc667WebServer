/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package webserver;

import Config.HttpdConf;
import Config.MimeTypes;
import Response.ResponseBase;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.ProcessBuilder.Redirect;
import java.net.Socket;

/**
 *
 * @author NoLimitProduction
 */
public class Worker implements Runnable{
    PrintWriter out;
    BufferedReader in;
    Socket clientSocket = null;
    MimeTypes mime_object;
    HttpdConf httpd_object;
    
    Worker(Socket clientSocket, HttpdConf httpd_object, MimeTypes mime_object) {
        this.clientSocket = clientSocket;
        this.httpd_object = httpd_object;
        this.mime_object = mime_object;
     }
        @Override
    public void run() {
        try {
            out = new PrintWriter(clientSocket.getOutputStream(), true);
            in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));

            System.out.print("(1) Got To Worker Body!\n");
            Request request = new Request(in);
            System.out.print("(2)Got To Worker Body!\n");
            Resource resource = new Resource(request, httpd_object.Aliases, httpd_object.ScriptAliases);
            System.out.print("(3)Got To Worker Body!\n");
            
            ResponseFactory responseFactory = new ResponseFactory(resource);
            System.out.print("(4)Got To Worker Body!\n");
            ResponseBase response = responseFactory.create(resource);
            
            System.out.print("(5)Got To Worker Body!\n");
            if (response.byteData != null) { //checking if requested file has images
                clientSocket.getOutputStream().write(response.writeString().getBytes());
                clientSocket.getOutputStream().write(response.byteData);
                clientSocket.getOutputStream().flush();
            } 
            else{
                out.print(response.writeString());
                out.flush();
            }
            out.close();
            }
        catch (Exception e) {
            System.out.println(e);
        }
    }
}
