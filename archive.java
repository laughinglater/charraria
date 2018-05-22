
import java.awt.Color;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;






public class archive {
	static ObjectOutputStream saveStream = null;
    static ObjectInputStream loadStream = null;
    static Archive archive = null;
    
    public static boolean readArchive() {
        boolean success = false;
        try {
            loadStream = new ObjectInputStream(
                    new BufferedInputStream(
                    new FileInputStream("./.game_save.sav")));
            archive = (Archive)loadStream.readObject();
            loadStream.close();
//            ConsoleWindow.println("Archive has been read.");
            success = true;
        } catch (FileNotFoundException e) {
            //ConsoleWindow.println("(File not found)");
        } catch (InvalidClassException e) {
           // ConsoleWindow.println("(Version error)");
        } catch (Exception e) {
           // ExceptionManager.handleException(e);
//            ConsoleWindow.println("Failed.");
        } finally {
            try {
                if (loadStream != null)
                    loadStream.close();
            } catch (IOException e) {
                //ExceptionManager.handleException(e);
            }
        }
        return success;
    }
    
    public static boolean writeArchive() {
//        ConsoleWindow.println("Writing archive to disk...");
        if (archive == null) {
           // ConsoleWindow.println("Null archive.");
            return false;
        }
        try {
            saveStream = new ObjectOutputStream(
                    new BufferedOutputStream(
                    new FileOutputStream("./.game_save.sav")));
            saveStream.writeObject(archive);
            saveStream.close();
//            ConsoleWindow.println("Archive has been written.");
            return true;
        } catch (Exception e) {
            //ExceptionManager.handleException(e);
            return false;
        } finally {
            try {
                saveStream.close();
            } catch (IOException e) {
                //ExceptionManager.handleException(e);
            }
        }

    }
    
    
    
    
    public static void saveArchive() {
      archive.save();
  }
  
    public static void loadArchive() {
      archive.load();
  }
  //调用saveGame和loadGame完成存储和读取
    public static void saveGame() {
    	if (MainPanel.stat == MainPanel.STAT_GAME ||
                MainPanel.stat == MainPanel.STAT_PAUSE) {
            archive = new Archive();
            saveArchive();
            if (!writeArchive()) {
                archive = null;
            }
            archive = null;
        }
    }
    public static void loadGame() {
    	MainPanel.stat = MainPanel.STAT_GAME;
        if (!readArchive()) {
            return;
        }
        loadArchive();
        archive = null;
    }
    //存档类
    private static class Archive implements Serializable {
    	//将需要存储的内容添加为私有变量
        private int focus_x;
        private int focus_y;
        private int x;
        private int y;
        private static Block [][] world;
        private static Block[][] backWorld;
        private static Entity[][] frontWorld;
        private static inventory Inventory;
        
        //此处需要使用全局变量初始化地图
        public Archive() {
        	world=new Block[landGenerator.getWorHeight()][landGenerator.getWorWidth()];
        	backWorld = new Block[landGenerator.getWorHeight()][landGenerator.getWorWidth()];
        	frontWorld = new Entity[landGenerator.getWorHeight()][landGenerator.getWorWidth()];
        }
        //变量等于存档时的状态
        void save() {
            focus_x=landGenerator.Focus_x;
            focus_y=landGenerator.Focus_y;
            x=hero.getX();
            y=hero.getY();
            Inventory=MainPanel.getInventory();
            for(int i=0;i<landGenerator.getWorHeight();i++) {
            	for(int j=0;j<landGenerator.getWorWidth();j++) {
            		world[i][j]=landGenerator.getWorldBlock(i,j);
            		frontWorld[i][j]=landGenerator.getWorldEntity(i, j);
            		backWorld[i][j]=landGenerator.getbackWorldBlock(i, j);

            	}
            }
        }
        //将现在的状态修改为存档中的状态
        void load() {
            landGenerator.Focus_x=focus_x;
            landGenerator.Focus_y=focus_y;
            hero.x=x;
            hero.y=y;
            MainPanel.setInventory(Inventory);
            for(int i=0;i<landGenerator.getWorHeight();i++) {
            	for(int j=0;j<landGenerator.getWorWidth();j++) {
            		landGenerator.changeWorldBlock(i,j,world[i][j]);
            		landGenerator.changeWorldEntity(i,j,frontWorld[i][j]);
            		landGenerator.changebackWorldBlock(i, j, backWorld[i][j]);
            	}
            }
        }
        
    }
}
