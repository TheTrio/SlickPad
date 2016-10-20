package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Formatter;

/**
 * Created by Shashwat on 10/19/2016.
 */
public class WriteFile {
    private File f;
    private String s;
    private Formatter x;

    public void OpenFile(String text, String s) throws FileNotFoundException {
        this.s = text;
        f = new File(s);
        x = new Formatter(f);
    }

    public void WriteFile(){
        System.out.println(s);
        x.format("%s\n", s);
    }

    public void CloseFile(){
        x.close();
    }
}
