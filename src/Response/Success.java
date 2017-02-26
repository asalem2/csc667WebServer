/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Response;

import Config.MimeTypes;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import webserver.Resource;

/**
 *
 * @author NoLimitProduction
 */
public class Success extends ResponseBase {

    SimpleDateFormat sdf = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz");
    String path, extension;
    byte[] file_content;

    public Success(Resource resource, int code_option) throws FileNotFoundException, IOException {
        super(resource, 200);
        path = resource.resolved_path;

        //File class called, which starts reading file content
        File file = new File(path);
        if (file.exists()) {
            FileInputStream httpd = new FileInputStream(file);
            file_content = new byte[(int) file.length()];
            httpd.read(file_content);
            httpd.close();
            //body = new String(file_content, "UTF-8");
            byteData = file_content;

        }
        String extension = path.substring(path.lastIndexOf(".") + 1, path.length());
        default_headers.put("Content-Type", MimeTypes.mime_types.get(extension));
        default_headers.put("Last-Modified", sdf.format(file.lastModified()));
        default_headers.put("Content-Length", String.valueOf(byteData.length));
        if (default_headers.containsKey("Content-Type")) {     // //add additional data to headers
            if (default_headers.get("Content-Type").equals("")) {
                default_headers.put("Content-Type", "text/plain");
            }
        }
    }

}
