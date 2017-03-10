/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package jsontoui;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author Thanura
 */
public class JSONUI_File {

    private String JSON = "";
    private File JSON_File;

    public JSONUI_File() {
    }
    public JSONUI_File(String file) {
        JSON_File = new File(file);
        try {
            JSON = readFile(file);
        } catch (Exception e) {
            //e.printStackTrace();
        }
    }

    public void setJSON(String JSON) {
        this.JSON = JSON;
    }

    public void setJSON_File(File JSON_File) {
        this.JSON_File = JSON_File;
    }

    public File getJSON_File() {
        return JSON_File;
    }
    
    public void saveFile() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(JSON_File.getAbsolutePath()))) {
            bw.write(JSON);
            bw.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getJSON() {
        return JSON;
    }

    private String readFile(String file) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String line = null;
        StringBuilder stringBuilder = new StringBuilder();
        String ls = System.getProperty("line.separator");

        try {
            while ((line = reader.readLine()) != null) {
                stringBuilder.append(line);
                stringBuilder.append(ls);
            }

            return stringBuilder.toString();
        } finally {
            reader.close();
        }
    }
}
