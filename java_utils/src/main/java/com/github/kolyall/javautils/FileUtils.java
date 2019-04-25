package com.github.kolyall.javautils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Nick Unuchek (skype: kolyall) on 09.03.2017.
 */

public class FileUtils {
    public static List<File> getListFiles(File parentDir, String extension) {
        ArrayList<File> inFiles = new ArrayList<File>();
        if (!parentDir.isDirectory()){
            if(parentDir.getName().endsWith(extension)){
                inFiles.add(parentDir);
            }
            return inFiles;
        }
        File[] files = parentDir.listFiles();
        for (File file : files) {
            if (file.isDirectory()) {
                inFiles.addAll(getListFiles(file,extension));
            } else {
                if(file.getName().endsWith(extension)){
                    inFiles.add(file);
                }
            }
        }
        return inFiles;
    }

    public static File getFile(String filePath, String extension) {
        File file = new File(filePath);
        if (file.exists() && !file.isDirectory() && file.getName().endsWith(extension)) {
            return file;
        }
        return null;
    }

    public static String stripExtension (String str) {
        // Handle null case specially.

        if (str == null) return null;

        // Get position of last '.'.

        int pos = str.lastIndexOf(".");

        // If there wasn't any '.' just return the string as is.

        if (pos == -1) return str;

        // Otherwise return the string, up to the dot.

        return str.substring(0, pos);
    }

    public static void writeStringAsFile(File dir, final String fileContents, String fileName) {
        try {
            FileWriter out = new FileWriter(new File(dir, fileName));
            out.write(fileContents);
            out.close();
        } catch (IOException e) {

        }
    }

    public static String readFileAsString(File dir, String fileName) {
        StringBuilder stringBuilder = new StringBuilder();
        String line;
        BufferedReader in = null;

        try {
            in = new BufferedReader(new FileReader(new File(dir, fileName)));
            while ((line = in.readLine()) != null) stringBuilder.append(line);

        } catch (FileNotFoundException e) {
            return null;

        } catch (IOException e) {
            return null;
        }

        return stringBuilder.toString();
    }
}
