package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GetSetting {
    private File file;
    private Scanner x;
    private int y = 0;
    private String val[] = new String[4];

    public void OpenFile(){
        file = new File("Settings.data");
        try {
            x = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String[] GiveSetting(){
        while(x.hasNext()){
            val[y] = x.next();
            y++;
        }

        return val;
    }

    public void CloseFile(){
        x.close();
    }


}
