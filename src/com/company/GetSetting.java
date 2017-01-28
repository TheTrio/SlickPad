package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class GetSetting {
    private File file;
    private Scanner x;
    private int y = 0;
    private String val[] = new String[6];

    private boolean b = false;
    public void OpenFile() {
        file = new File("Settings.data");
        if(file.exists()){
            b = true;
        }
        try {
            if(b)
                x = new Scanner(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public String[] GiveSetting() {
        if(b){
        while (x.hasNextLine()) {
            System.out.println(y);
            if (y >= 6)
                break;
            val[y] = x.nextLine();
            y++;
        }

        y = 0;

        return val;
        }else return null;

    }

    public void CloseFile() {
        x.close();
    }


}
