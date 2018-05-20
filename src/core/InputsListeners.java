package core;

import ld34.profile.Settings;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

/**
 * InputsListeners class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class InputsListeners implements KeyListener, MouseMotionListener, MouseListener {

    /**
     * Internal Action class
     */
    public class Action{
        
        public boolean enabled, typed;
        public int pressCpt, absorbCpt;
        
         /**
          * 
          */
        public Action(){
            actions.add(this);
        }
        
        /**
         * 
         * @param enabled 
         */
        public void switched(boolean enabled){
            if(enabled != this.enabled)
                this.enabled = enabled;
            if(enabled){
                this.pressCpt++;
            }
        }
        
        /**
         * 
         */
        public void update(){
            if(this.absorbCpt < this.pressCpt){
                this.absorbCpt++;
                this.typed = true;
            }
            else{
                this.typed = false;
            }
        }
    }
    
    public boolean[] keys;
    public ArrayList<Action> actions = new ArrayList<>();
    public Action jump = new Action();
    public Action slow = new Action();
    public Action next = new Action();
    public Action profiler = new Action();
    public Action pause = new Action();
    public Action minimap = new Action();
    public Action fullscreen = new Action();
    public int mouseX, mouseY, mouseClickCount;
    public boolean mouseExited, mousePressed;
    public KeyEvent e = null;
    
    /**
     * 
     * @param game 
     */
    public InputsListeners(Game game){
        
        game.addKeyListener(this);
        game.addMouseMotionListener(this);
        game.addMouseListener(this);
        
        this.mouseX = 0;
        this.mouseY = 0;
        this.mouseClickCount = 0;
        this.mouseExited = true;
        this.mousePressed = false;
        this.keys = new boolean[KeyEvent.KEY_LAST];
    }
    
    /**
     * 
     */
    public void update(){
        this.mouseClickCount = 0;
        for(int i=0; i<this.actions.size();i++){
            actions.get(i).update();
        }
    }
    
    /**
     * 
     * @param e
     * @param enabled 
     */
    public void processKey(KeyEvent e, boolean enabled){    
        if(e.getKeyCode() == Integer.parseInt(Settings.getInstance().getConfigValue("Jump"))) 
            jump.switched(enabled);
        if(e.getKeyCode() == Integer.parseInt(Settings.getInstance().getConfigValue("Walk")))
            slow.switched(enabled);
        if(e.getKeyCode() == KeyEvent.VK_ENTER) 
            next.switched(enabled);
        if(e.getKeyCode() == KeyEvent.VK_F3)
            profiler.switched(enabled);
        if(e.getKeyCode() == KeyEvent.VK_ESCAPE)
            pause.switched(enabled);
        if(e.getKeyCode() == KeyEvent.VK_M)
            minimap.switched(enabled);
        if(e.getKeyCode() == KeyEvent.VK_F)
            fullscreen.switched(enabled);
    }
    
    @Override
    public void keyTyped(KeyEvent e) {
        
    }

    @Override
    public void keyPressed(KeyEvent e) {
        processKey(e, true);
        this.e = e;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        processKey(e, false);
        this.e = null;
    }
    
    @Override
    public void mouseDragged(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        this.mouseX = e.getX();
        this.mouseY = e.getY();
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        this.mousePressed = true;
        this.mouseClickCount = e.getClickCount();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        this.mousePressed = false;
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        this.mouseExited = false;
    }

    @Override
    public void mouseExited(MouseEvent e) {
        this.mouseExited = true;
    }
}
