package ld34;

import com.sun.glass.events.KeyEvent;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class Configs {
    
    public String[] configsLabels = {"Lang", "Difficulty", "Jump", "Walk", "Sex", "Spicies", "Sound", "Name"};
    public String fileOptions = "settings.dat";
    public String[] configsDefaultValues = {"0", "0", ""+KeyEvent.VK_SPACE, ""+KeyEvent.VK_CONTROL, "0", "0", "100", ""};
    public String[] configsValues;
    
    private Configs(){
        this.loadConfigs();
    }
    
    private static Configs instance = new Configs();
    
    public static Configs getInstance(){
        return instance;
    }
    
    private void loadConfigs(){
        File f = new File(this.fileOptions);
        this.configsValues = this.configsDefaultValues;
         
        if(f.exists() && !f.isDirectory()){
            //read configurations
            try{
                BufferedReader br = new BufferedReader(new FileReader(this.fileOptions));
                String line = null;
                while((line = br.readLine()) != null){
                    String[] strSplited = line.split(":");
                    for(int i=0;i<this.configsLabels.length;i++){
                        if(this.configsLabels[i].equals(strSplited[0])){
                            this.configsValues[i] = strSplited[1];
                        }
                    }
                }
                br.close();
            }
            catch(IOException e){
                e.printStackTrace();
            }
        }
        else{
            //file not exist add default configuration file
            this.saveConfig();
        }
    }
    
    public void setConfigValue(String key, String value){
        for(int i=0;i<this.configsLabels.length;i++){
            if(this.configsLabels[i].equals(key)){
                this.configsValues[i] = value;
            }
        }
    }
    
    public String getConfigValue(String key){
        
        for(int i=0;i<this.configsLabels.length;i++){
            if(this.configsLabels[i].equals(key)){
                if((configsValues[i].toString().substring(0, 1)).equals("\"")){
                    System.out.println("string");
                }
                else
                {
                    return this.configsValues[i];
                }
            }
        }
        //not found...
        return null;
    }
    
    public void saveConfig(){
        try{
            PrintWriter pw = new PrintWriter(
                                new BufferedWriter(
                                    new FileWriter(this.fileOptions)));
            for(int i=0;i<this.configsValues.length;i++){
                pw.println(this.configsLabels[i] + ":" + this.configsValues[i]);
            }
            pw.close();
        }
        catch(IOException e){
            e.printStackTrace();
        }
    }
}
