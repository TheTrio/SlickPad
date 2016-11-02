package com.company;

import java.io.File;
import java.util.Formatter;

public class WriteSetting {

    private File file;
    private Formatter x;

    public void OpenFile(){
        file = new File("Settings.data");

        try {
            x = new Formatter(file);
        }catch (Exception e){
            System.out.println(e);
        }
    }

    public void AddRecords(String val[]){
        x.format("%s\n%s\n%s\n%s", val[0], val[1],val[2], val[3]);
    }

    public void CloseFile(){
        x.close();
    }

}
