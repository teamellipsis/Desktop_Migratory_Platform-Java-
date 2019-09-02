package com.enigma.platform;

import java.io.*;

public class ObjectInputStreamWithLoader extends ObjectInputStream
{
    private ClassLoader loader;

    /**
     * Loader must be non-null;
     */

    public ObjectInputStreamWithLoader(InputStream in, ClassLoader loader)
            throws IOException, StreamCorruptedException {

        super(in);
        if (loader == null) {
            throw new IllegalArgumentException("Illegal null argument to ObjectInputStreamWithLoader");
        }
        this.loader = loader;
    }

    /**
     * Use the given ClassLoader rather than using the system class
     */
    @SuppressWarnings("rawtypes")
    protected Class resolveClass(ObjectStreamClass classDesc)
            throws IOException, ClassNotFoundException {

        String cname = classDesc.getName();
//        return ClassFinder.resolveClass(cname, this.loader);
        return Class.forName(classDesc.getName() , false , loader);
    }
}