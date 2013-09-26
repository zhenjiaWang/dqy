package com.dqy.util;

import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLStreamHandlerFactory;

/**
 * Created by IntelliJ IDEA.
 * User: Administrator
 * Date: 2010-1-6
 * Time: 14:23:58
 * To change this template use File | Settings | File Templates.
 */
public class LoaderJarUtils extends URLClassLoader {
    public LoaderJarUtils(URL[] urls, ClassLoader parent) {
        super(urls, parent);
    }


    public LoaderJarUtils(URL url) {
        super(new URL[]{url});
    }

    public LoaderJarUtils(URL[] urls) {
        super(urls);
    }

    public LoaderJarUtils(URL[] urls, ClassLoader parent, URLStreamHandlerFactory factory) {
        super(urls, parent, factory);
    }

    public Object execute(String className, String methodName) throws Exception {
        try {
            Class aClass = findClass(className);
            Method method = aClass.getMethod(methodName);
            Object o = aClass.newInstance();
            Object result = method.invoke(o);
            return result;
        } catch (Exception e) {
            return null;
        }
    }
}