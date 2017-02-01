package ld34;

import java.util.List;
import java.util.ArrayList;

import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.JRootPane;
import javax.swing.JLabel;

public class CustomDialog {
    private List<JComponent> components;
    
    private String title;
    private int messageType;
    private JRootPane rootPane;
    private String[] options;
    private int optionIndex;
    
    public CustomDialog(){
        components = new ArrayList<>();
        
        this.setTitle("Custom dialog");
        this.setMessageType(JOptionPane.PLAIN_MESSAGE);
        this.setRootPane(null);
        this.setOptions(new String[] {"Oui", "Non"});
        this.setOptionSelected(0);
    }
    
    public void setTitle(String title){
        this.title = title;
    }
    
    public void setMessageType(int messageType){
        this.messageType = messageType;
    }
    
    public void addComponent(JComponent component){
        this.components.add(component);
    }
    
    public void addMessageText(String messageText){
        JLabel label = new JLabel(messageText);
        this.components.add(label);
    }
    
    public void setRootPane(JRootPane rootPane){
        this.rootPane = rootPane;
    }
    
    public void setOptions(String[] options){
        this.options = options;
    }
    
    public void setOptionSelected(int optionIndex){
        this.optionIndex = optionIndex;
    }
    
    public int show(){
        int optionType = JOptionPane.OK_CANCEL_OPTION;
        Object optionSelection = null;
        
        if(this.options.length != 0){
            optionSelection = this.options[this.optionIndex];
        }
        
        int selection = JOptionPane.showOptionDialog(
                this.rootPane, 
                this.components.toArray(), 
                this.title, 
                optionType, 
                this.messageType, 
                null, 
                this.options, 
                optionSelection);
        
        return selection;
    }
}
