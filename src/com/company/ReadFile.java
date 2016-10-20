package com.company;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class ReadFile {
    private File f;
    private Scanner x;
    private String data = "";

    public void OpenFile(String s){
        f = new File(s);
        try {
            x = new Scanner(f);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void GetFiles(){
        while(x.hasNextLine()){
            data +=x.nextLine() + "\n";
        }

    }

    public String GiveFiles(){
        return data;
    }
    public void CloseFile(){
        x.close();
    }
}
