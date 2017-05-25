package ld34.scene;

import audio.Sound;
import entity.Player;
import java.awt.Color;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javax.imageio.ImageIO;
import core.Camera;
import ld34.profile.Settings;
import core.Game;
import entity.CageEntity;
import java.util.ArrayList;
import java.util.List;
import ld34.profile.Save;
import level.Level;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * SavesScene class
 * 
 * @version %I%, %G%
 * @author Afendar
 */
public class SavesScene extends Scene {

    private String emptyTxt, title;
    private Font font, fontM, fontS;
    private BufferedImage gui, bgSave, bgMediumBtn,levelIcon, removeIcon, cageIcon, loadIcon, dollardIcon, littlesPandas, backIcon;
    private int selectedItem, selectedSave, selectRemove, selectLoad;
    private int[][] btnCoords;
    private JSONObject jsonSaves;
    
    /**
     * 
     * @param w
     * @param h
     * @param game 
     */
    public SavesScene(int w, int h, Game game){
        super(w, h, game);
        
        try{
            URL url = this.getClass().getResource("/fonts/kaushanscriptregular.ttf");
            this.font = Font.createFont(Font.TRUETYPE_FONT, url.openStream());
            this.font = this.font.deriveFont(Font.PLAIN, 18.0f);
            this.fontS = this.font.deriveFont(Font.PLAIN, 14.0f);
            this.fontM = this.font.deriveFont(Font.PLAIN, 36.0f);
            
            url = this.getClass().getResource("/gui2.png");
            this.gui = ImageIO.read(url);
            this.bgSave = this.gui.getSubimage(235, 127, 217, 216);
            this.bgMediumBtn = this.gui.getSubimage(370, 1, 120, 99);
            this.levelIcon = this.gui.getSubimage(362, 107, 19, 16);
            this.removeIcon = this.gui.getSubimage(211, 92, 33, 35);
            this.cageIcon = this.gui.getSubimage(384, 101, 27, 25);
            this.loadIcon = this.gui.getSubimage(176, 93, 34, 35);
            this.dollardIcon = this.gui.getSubimage(413, 104, 16, 20);
            this.backIcon = this.gui.getSubimage(243, 94, 42, 34);
            
            url = this.getClass().getResource("/littles_pandas.png");
            this.littlesPandas = ImageIO.read(url);
        }
        catch(FontFormatException|IOException e){
            e.getMessage();
        }
        
        int localeIndex = Integer.parseInt(Settings.getInstance().getConfigValue("Lang"));
        
        this.bundle = ResourceBundle.getBundle("lang.lang", this.game.langs[localeIndex]);
        
        this.title = this.bundle.getString("loadGame");
        this.emptyTxt = this.bundle.getString("empty");
        
        int[][] btnCoordsDefines = {
            {190, 420},
            {340, 420},
            {490, 420}
        };
        this.btnCoords = btnCoordsDefines;
        
        this.selectedItem = 0;
        this.selectedSave = 0;
        this.selectRemove = 0;
        this.selectLoad = 0;
        
        this.jsonSaves = Save.getInstance().getSaves();
    }
    
    @Override
    public Scene update(double dt) {
        this.processHover();
        
        return this.processClick();
    }

    /**
     * 
     */
    public void processHover(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;

        int oldSelected = this.selectedItem;
        this.selectedItem = 0;
        
        for(int i=0;i<this.btnCoords.length;i++){
            if(mouseX > this.btnCoords[i][0] && mouseX < this.btnCoords[i][0] + 120 && mouseY > this.btnCoords[i][1] && mouseY < this.btnCoords[i][1] + 99){
                this.selectedItem = i + 1;
                break;
            }
        }
        if(this.selectedItem != 0 && this.selectedItem != oldSelected){
            new Thread(Sound.hover::play).start();
        }
    }
    
    /**
     * 
     * @return 
     */
    public Scene processClick(){
        int mouseX = this.game.listener.mouseX;
        int mouseY = this.game.listener.mouseY;
        
        Scene currentScene = this;
        
        if(this.game.listener.mousePressed && this.game.listener.mouseClickCount == 1){
            switch(this.selectedItem){
                case 1:
                    if(this.selectedSave != 0){
                        JSONObject save = Save.getInstance().getSave(this.selectedSave - 1);
                        if(save != null && !save.isEmpty()){
                            JSONObject jsonLevel = (JSONObject)save.get("level");
                            JSONObject jsonPlayer = (JSONObject)save.get("player");
                            int nbLevel = Integer.parseInt((String)jsonLevel.get("number"));
                            Level level = new Level(nbLevel);
                            JSONArray jsonUnlockedLevels = (JSONArray)jsonLevel.get("unlockedLevels");
                            boolean[] unlockedLevels = new boolean[jsonUnlockedLevels.size()];
                            for(int i = 0; i< jsonUnlockedLevels.size();i++){
                                unlockedLevels[i] = ((String)jsonUnlockedLevels.get(i)).equals("true") ? true : false;
                            }
                            
                            level.setUnlockedLevels(unlockedLevels);
                            
                            JSONArray coords = (JSONArray)jsonPlayer.get("coords");
                            Camera cam = new Camera(Integer.parseInt((String)coords.get(0)), Integer.parseInt((String)coords.get(1)), this.w, this.h, level);
                            Player player = new Player(Integer.parseInt((String)coords.get(0)), Integer.parseInt((String)coords.get(1)), level, this.game.listener, cam, Integer.parseInt((String)jsonLevel.get("difficulty")));
                            player.score = Integer.parseInt((String)jsonPlayer.get("score"));
                            GameScene gs = new GameScene(this.w, this.h, this.game, level, player);

                            JSONObject cagesMap = (JSONObject)jsonLevel.get("cages");
                            List<List<CageEntity>> cageMap = new ArrayList<>();
                            for(int i=1;i<cagesMap.size() + 1;i++){
                                JSONArray cageInLevelDatas = (JSONArray)cagesMap.get(Integer.toString(i));
                                List<CageEntity> cagesList = new ArrayList<>();
                                for(int j=0;j<cageInLevelDatas.size();j++){
                                    JSONArray cageDatas = (JSONArray)cageInLevelDatas.get(j);
                                    CageEntity ce = new CageEntity(level, Integer.parseInt((String)cageDatas.get(0)), Integer.parseInt((String)cageDatas.get(1)));
                                    ce.setBroken((boolean)cageDatas.get(2));
                                    cagesList.add(ce);
                                }
                                cageMap.add(cagesList);
                            }
                            gs.setLevelCagesMap(cageMap);
                            currentScene = gs;
                        }
                    }
                    break;
                case 2:
                    if(this.selectedSave != 0){
                        Save.getInstance().removeSave(this.selectedSave - 1);
                    }
                    break;
                case 3:
                    currentScene = new MenuScene(this.w, this.h, this.game);
                    break;
                default:
                    currentScene = this;
                    break;
            }

            if(mouseX > (0 * 230) + 70 && mouseX < (0 * 230) + 70 + 217 && mouseY > 155 && mouseY < 155 + 216){
                this.selectedSave = 1;
            }
            else if(mouseX > (1 * 230) + 70 && mouseX < (1 * 230) + 70 + 217 && mouseY > 155 && mouseY < 155 + 216){
                this.selectedSave = 2;
            }
            else if(mouseX > (2 * 230) + 70 && mouseX < (2 * 230) + 70 + 217 && mouseY > 155 && mouseY < 155 + 216){
                this.selectedSave = 3;
            }
            else{
                this.selectedSave = 0;
            }
        }
        
        return currentScene;
    }
    
    @Override
    public void render(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        
        g.drawImage(this.background, 0, 0, null);
        g.setColor(Scene.BLACKSHADOW);
        g.fillRect(0, 35, this.w, 60);
        
        FontMetrics metrics = g.getFontMetrics(this.fontM);
        g.setFont(this.fontM);
        g.setColor(Color.BLACK);
        int titlewidth = metrics.stringWidth(this.title);
        g.drawString(this.title, this.w/2 - titlewidth/2, 75);
        
        metrics = g.getFontMetrics(this.font);
        g.setFont(this.font);
        
        for(int i=0;i<this.jsonSaves.size();i++){
            if(this.selectedSave == i + 1){
                g.drawImage(this.gui.getSubimage(453, 125, 221, 220), (i * 230) + 68, 153, null);
            }
            else{
                g.drawImage(this.bgSave, i * 230 + 70, 155, null);
            }
            
            //get save datas
            JSONObject save = (JSONObject) this.jsonSaves.get("Slot" + i);
            if(save.isEmpty()){
                int emptyTxtWidth = metrics.stringWidth(this.emptyTxt);
                g.setColor(Color.BLACK);
                g.drawString(this.emptyTxt, i * 230 + 178 - emptyTxtWidth/2, 256 + metrics.getAscent()/2);
            }
            else{
                JSONObject player = (JSONObject) save.get("player");
                String name = (String) player.get("name");
                int sex = Integer.parseInt((String) player.get("sex"));
                int spicies = Integer.parseInt((String) player.get("spicies"));
                g.setFont(this.font);
                g.setColor(Color.BLACK);
                metrics = g.getFontMetrics(this.font);
                int nameW = metrics.stringWidth(name);
                g.drawString(name, i * 230 + 178 - nameW/2, 192 + (metrics.getAscent()/2));
                
                int x = 0;
                int y = 160;
                if(sex == 0){
                    x = 50;
                }
                if(spicies == 1){
                    y = 208;
                }
                
                g.drawImage(this.dollardIcon, i * 230 + 181, 232 ,null);
                g.setFont(this.fontS);
                String score =  (String) player.get("score");
                int scoreWidth = metrics.stringWidth(score);
                g.drawString(score, i * 230 + 199, 237 + metrics.getAscent()/2);
                
                g.drawImage(this.levelIcon, i * 230 + 107, 315, null);
                JSONObject jsonLevel = (JSONObject) save.get("level");
                String levelNumber = (String) jsonLevel.get("number");
                int levelNum = Integer.parseInt((String) jsonLevel.get("number"));
                int levelNumberW = metrics.stringWidth(levelNumber);
                g.drawString(levelNumber, i * 230 + 130, 318 + metrics.getAscent()/2);
                
                g.drawImage(this.cageIcon, i * 230 + 176, 266, null);
                String cageNumbers = (String) jsonLevel.get("freeCages") + "/30";
                g.drawString(cageNumbers, i * 230 + 208, 274 + metrics.getAscent()/2);
                
                String complete = (String) jsonLevel.get("complete") + "%";
                int completeW = metrics.stringWidth(complete);
                g.drawString(complete, i * 230 + 178 - completeW/2, 318 + metrics.getAscent()/2);
                
                int difficulty = Integer.parseInt((String) jsonLevel.get("difficulty"));
                
                switch(difficulty){
                    case 0:
                        g.drawImage(this.gui.getSubimage(285, 69, 17, 16), i * 230 + 226, 305, null);
                        break;
                    case 2:
                        g.drawImage(this.gui.getSubimage(325, 69, 35, 16), i * 230 + 226, 305, null);
                        break;
                    case 4:
                        g.drawImage(this.gui.getSubimage(285, 89, 33, 32), i * 230 + 226, 305, null);
                        break;
                    case 5:
                        g.drawImage(this.gui.getSubimage(326, 90, 33, 32), i * 230 + 226, 305, null);
                        break;
                    default:
                        break;
                }
                
                g.setFont(this.font);
                metrics = g.getFontMetrics(this.font);
                
                int offset = 0;
                switch(levelNum){
                    case 1:
                    case 2:
                        offset = 0;
                        break;
                    case 3:
                    case 4:
                        offset = 2;
                        break;
                    case 5:
                    case 6:
                        offset = 4;
                        break;
                }
                g.setColor(new Color(193, 182, 129));
                g.fillRoundRect(i * 230 + 102, 225, 70, 70, 8, 8);
                g.drawImage(this.littlesPandas.getSubimage(((sex + offset ) + ( 6 * spicies)) * 64, 0, 64, 64), i * 230 + 105, 228, null);
            }
        }
        
        for(int i=0;i<this.btnCoords.length;i++){
            if(this.selectedItem == i + 1){
                this.bgMediumBtn = this.gui.getSubimage(491, 1, 120, 99);
            }
            else{
                this.bgMediumBtn = this.gui.getSubimage(370, 1, 120, 99);
            }
            g.drawImage(this.bgMediumBtn, i * 150 + 190, 420, null);
            switch(i){
                case 0:
                    g.drawImage(this.loadIcon, i * 150 + 234, 450, null);
                    break;
                case 1:
                    g.drawImage(this.removeIcon, i * 150 + 232, 450, null);
                    break;
                case 2:
                    g.drawImage(this.backIcon, i * 150 + 227, 450, null);
                    break;
            }
        }

        g.drawImage(this.foreground2, 0, 0, null);
    }
}