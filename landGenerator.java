import java.awt.*;
import java.util.*;

public class landGenerator{
    private static final int WORLD_WID=1000;
    private Random r=new Random(RandSeed);
    private static final int WORLD_HEIGHT=200;
    private static final int RandSeed=42;
    private static final int CloudHeight=40;

    public static int getWorWidth(){
        return WORLD_WID;
    }
    public static int getWorHeight(){
        return WORLD_HEIGHT;
    }

    public static int Focus_x=WORLD_WID/2;
    public static int Focus_y=WORLD_HEIGHT/2;
    private static final int fontSize=15;
    public static int getFontSize(){
        return fontSize;
    }

    private static final int window_offset_x= MainWindow.getWinWidth()/(2*fontSize);
    private static final int window_offset_y= MainWindow.getWinHeight()/(2*fontSize);
    private Font mainFont=new Font("TimesRoman",Font.BOLD,fontSize);

    public static int getWinOffsetX(){
        return window_offset_x;
    }

    public static int getWinOffsetY(){
        return window_offset_y;
    }


    private static Block [][] world = new Block[WORLD_HEIGHT][WORLD_WID];
    public static Block getWorldBlock(int i,int j){
        return world[i][j];
    }
    public static void changeWorldBlock(int i,int j,Block b){
        world[i][j]=b;
    }


    public void draw(Graphics g) {
        for(int i=Focus_x-window_offset_x;i<=Focus_x+window_offset_x;i++){
            for(int j=Focus_y-window_offset_y;j<=Focus_y+window_offset_y;j++){
                if(i>=0 && i<WORLD_WID && j>=0 && j<WORLD_HEIGHT){
                    g.setFont(mainFont);
                    g.setColor(world[j][i].color);
                    g.drawString(String.valueOf(world[j][i].symbol),(i-(Focus_x-window_offset_x))*fontSize,MainWindow.getWinHeight()-(j-(Focus_y-window_offset_y))*fontSize);
                }
            }
        }
    }

    public void basicGenesis(){
        for(int i=0;i<WORLD_HEIGHT/2;i++) {
            for(int j=0;j<WORLD_WID;j++){
                world[i][j]=new Dirt();
            }
        }
        for(int i=WORLD_HEIGHT/2;i<WORLD_HEIGHT;i++) {
            Arrays.fill(world[i], new Air((i-WORLD_HEIGHT/2-30>0)?i-WORLD_HEIGHT/2-30:0));
        }
//        System.out.println(Arrays.deepToString(world));
    }
    public void GrassLandGenesis(){
        int oldChange=0;
        for(int j=0;j<WORLD_WID;j++){
            if(j>=WORLD_WID/2-20 && j<=WORLD_WID/2+20){
                continue;
            }
            else{
                int change=(int)(r.nextGaussian());
                if(change+oldChange>0){
                    for(int i=0;i<change+oldChange;i++){
                        if(i==change+oldChange-1) world[WORLD_HEIGHT/2+i][j]=new grassDirt();
                        else world[WORLD_HEIGHT/2+i][j]=new Dirt();
                    }
                }
                else if(change+oldChange<0){
                    for(int i=change+oldChange;i<0;i++) {
                        if(i==-1) world[WORLD_HEIGHT/2+i][j]=new grassDirt();
                        else world[WORLD_HEIGHT/2+i][j]=new Dirt();
                    }
                }
                oldChange=change;
            }
        }
    }
    public void EndOfWorldGenesis(){
        for(int i=0;i<WORLD_HEIGHT;i++){
            world[i][0]=new SideEndStone();
        }
        for(int i=0;i<WORLD_HEIGHT;i++){
            world[i][WORLD_WID-1]=new SideEndStone();
        }
        for(int i=0;i<WORLD_WID;i++){
            world[0][i]=new BaseStone();
        }
        for(int i=0;i<WORLD_WID;i++){
            world[WORLD_HEIGHT-1][i]=new TopAir();
        }
    }
    public void SlimTreeGenesis(){
        int TreeTotal=150;
        int TreeWidthToHeight=8;
        for(int z=0;z<TreeTotal;z++){
            int TreeHeight=(int)(r.nextGaussian()*10);
            int TreeWidth=(int)Math.ceil(TreeHeight/TreeWidthToHeight);
            int TreeX=z*WORLD_WID/TreeTotal+(int)r.nextGaussian()*5;
            int TreeY=0;
            int flag_Vacant=0;
            for(int j=0;j<WORLD_HEIGHT;j++){
                if(world[j][TreeX].getBlockNo()==0){
                    flag_Vacant++;
                    if(flag_Vacant>=1.5*TreeHeight){
                        TreeY=j-flag_Vacant;
                        break;
                    }
                }
            }
            if(TreeY!=0) {
                for (int i = TreeX; i < TreeX+TreeWidth; i++) {
                    for (int j = TreeY; j < TreeY+ TreeHeight; j++) {
                        world[j][i] = new Wood(1);
                    }
                }
                for (int i = TreeX + TreeWidth / 2; i < TreeX + TreeWidth / 2 + TreeWidth; i++) {
                    for (int j = TreeY + TreeHeight / 2; j < TreeHeight + TreeY; j++) {
                        int tmp = r.nextInt(10);
                        if (tmp > 8) {
                            world[j][i] = new Wood(3);
                        } else if (tmp > 3) {
                            world[j][i] = new Leaf();
                        }
                    }
                }
                for (int i = TreeX + TreeWidth / 2; i > TreeX + TreeWidth / 2 - TreeWidth; i--) {
                    for (int j = TreeY + TreeHeight / 2; j < TreeHeight + TreeY; j++) {
                        int tmp = r.nextInt(10);
                        if (tmp > 8) {
                            world[j][i] = new Wood(2);
                        } else if (tmp > 3) {
                            world[j][i] = new Leaf();
                        }
                    }
                }
            }
        }
    }
    public void CloudGenesis(){
        for(int j=WORLD_HEIGHT/2+CloudHeight;j<WORLD_HEIGHT-1;j++){
            for(int i=1;i<WORLD_WID-1;i++){
                if( world[j][i-1].BlockNo==9 || world[j][i+1].BlockNo==9){
                    if(r.nextInt(100)>50){
                        world[j][i]=new cloud();
                    }
                }
                else if(world[j-1][i].BlockNo==9 || world[j+1][i].BlockNo==9){
                    if(r.nextInt(100)>80){
                        world[j][i]=new cloud();
                    }
                }
                else {
                    if (r.nextInt(100) > 95) {
                        world[j][i] = new cloud();
                    }
                }
            }
        }
    }


    landGenerator(){
        basicGenesis();
        GrassLandGenesis();
        EndOfWorldGenesis();
        SlimTreeGenesis();
        CloudGenesis();
    }



}
