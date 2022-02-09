package com.fyp.face;

import org.bytedeco.opencv.presets.opencv_core;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;

public class Lables {

    String mPath;
    ArrayList<label> theList = new ArrayList<>();

    class label{
        int num;
        String theLabel;

        public label(String s, int n){
            theLabel = s;
            num = n;
        }
    }

    public Lables(String mPath) {
        this.mPath = mPath;
    }

    public boolean isEmpty(){
        return theList.isEmpty();
    }

    //TODO optimize the code
    public String get(int i) {
        Iterator<label> it = theList.iterator();
        while(it.hasNext()){
            label l = it.next();
            if(l.num == i){
                return l.theLabel;
            }
        }
        return null;
    }

    public int get(String s){
        for(label l : theList){
            if(l.theLabel.equalsIgnoreCase(s)){
                return l.num;
            }
        }

        return -1;
    }

    public void add(String s, int n){
        theList.add(new label(s, n));
    }

    //TODO change the store function to database
    public void Save(){
        try{
            File f = new File(mPath + "faces.txt");
            f.createNewFile();
            BufferedWriter bw = new BufferedWriter(new FileWriter(f));
            Iterator<label> it = theList.iterator();
            while (it.hasNext()){
                label l = it.next();
                bw.write(l.theLabel + "," + l.num);
                bw.newLine();
            }
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    //TODO change the read function to database
    public void Read(){
        try{
            FileInputStream fStream = new FileInputStream(mPath + "faces.txt");
            BufferedReader br = new BufferedReader(new InputStreamReader(fStream));

            String strLine;
            theList = new ArrayList<>();

            //Read File line by line
            while((strLine = br.readLine()) != null){
                StringTokenizer tokens = new StringTokenizer(strLine, ",");
                String s = tokens.nextToken();
                String sn = tokens.nextToken();

                theList.add(new label(s, Integer.parseInt(sn)));
            }

            br.close();
            fStream.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public int max(){
        int m = 0;
        Iterator<label> it = theList.iterator();
        while (it.hasNext()){
            label l = it.next();
            if(l.num > m){
                m = l.num;
            }
        }
        return m;
    }
}
