package com.enigma.platform;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassLoder {

    public static URLClassLoader getclassloader() throws MalformedURLException {
        String file_name = "resources/todolast.jar";
        File file = new File(file_name);
        if (!file.exists()){
            System.out.println("null");
        }
        URL u = file.toURI().toURL();
        URL[] urls1 = {u};
        URLClassLoader ucl1 = new URLClassLoader(urls1);
        return ucl1;
    }
}
