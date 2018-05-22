import java.awt.*;
import java.util.*;

public class landGenerator{
    private static final int WORLD_WID=1000;
    private Random r=new Random(RandSeed);
    private static final int WORLD_HEIGHT=200;
    private static final int RandSeed=42;
    private static final int CloudHeight=40;
    private static final int HoleHeight=20;
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
    private Font mainFont=new Font("TimesNewRoman",Font.BOLD,fontSize);
    private Font entityFont=new Font("TimesNewRoman",Font.PLAIN,fontSize/2);
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
    public static Entity getWorldEntity(int i,int j){
        return frontWorld[i][j];
    }
    public static void changeWorldBlock(int i,int j,Block b){
        world[i][j]=b;
    }
    public static void changeWorldEntity(int i,int j,Entity e){
        frontWorld[i][j]=e;
    }


    private static Block[][] backWorld = new Block[WORLD_HEIGHT][WORLD_WID];
    private static Entity[][] frontWorld = new Entity[WORLD_HEIGHT][WORLD_WID];



    public void draw(Graphics g) {
        for(int i=Focus_x-window_offset_x;i<=Focus_x+window_offset_x;i++){
            for(int j=Focus_y-window_offset_y ;j<=Focus_y+window_offset_y;j++){
                if(i>=0 && i<WORLD_WID && j>=0 && j<WORLD_HEIGHT){
                    //backgroud layer
                    if(backWorld[j][i]!=null) {
                        Block b=backWorld[j][i];
                        g.setFont(mainFont);
                        g.setColor(b.color);
                        g.drawString(String.valueOf(b.symbol),(i-(Focus_x-window_offset_x))*fontSize,MainWindow.getWinHeight()-(j-(Focus_y-window_offset_y))*fontSize);
                    }
                    //solid&&fluid layer
                    if(world[j][i]!=null) {
                        g.setFont(mainFont);
                        Block b = world[j][i];
                        g.setColor(b.color);
                        //正在被破坏，闪烁。
                        if (b instanceof solidBlock && ((solidBlock) b).Flag_damaged != 0) {
                            System.out.println("block blinking");
                            if (--((solidBlock) b).Flag_damaged % 2 == 0)
                                continue;
                        }
                        g.drawString(String.valueOf(b.symbol), (i - (Focus_x - window_offset_x)) * fontSize, MainWindow.getWinHeight() - (j - (Focus_y - window_offset_y)) * fontSize);
                    }
                    //entity layer
                    if(frontWorld[j][i]!=null)  {
                        Entity e=frontWorld[j][i];
                        g.setFont(entityFont);
                        g.setColor(e.color);
                        g.drawString(String.valueOf(e.symbol),(i-(Focus_x-window_offset_x))*fontSize,MainWindow.getWinHeight()-(j-(Focus_y-window_offset_y))*fontSize);
                    }
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
    }
    public void GrassLandGenesis(){
        for(int j=WORLD_WID/2+10;j<WORLD_WID;j++){
            int jj=j-WORLD_WID/2-10;
            double n=(ImprovedNoise.noise(jj*(1.0/300.0),0,0)+
                    ImprovedNoise.noise(jj*(1.0/150.0),0,0)*0.5+
                    ImprovedNoise.noise(jj*(1.0/75.0),0,0)*0.25+
                    ImprovedNoise.noise(jj*(1.0/37.5),0,0)*0.125)*100;
            if(n>0){
                for(int i=0;i<n;i++){
                    if(i+1>n) world[WORLD_HEIGHT/2+i][j]=new grassDirt();
                    else world[WORLD_HEIGHT/2+i][j]=new Dirt();
                }
            }
            else{
                for(int i=0;i>n;i--){
                    if(i-1<n)world[WORLD_HEIGHT/2+i][j]=new grassDirt();
                    else world[WORLD_HEIGHT/2+i][j]=new Air(0);
                }
            }
        }
        for(int j=WORLD_WID/2-10;j>=0;j--){
            int jj=j-WORLD_WID/2+10;
            double n=(ImprovedNoise.noise(jj*(1.0/300.0),0,0)+
                    ImprovedNoise.noise(jj*(1.0/150.0),0,0)*0.5+
                    ImprovedNoise.noise(jj*(1.0/75.0),0,0)*0.25+
                    ImprovedNoise.noise(jj*(1.0/37.5),0,0)*0.125)*100;
            if(n>0){
                for(int i=0;i<n;i++){
                    if(i+1>n) world[WORLD_HEIGHT/2+i][j]=new grassDirt();
                    else world[WORLD_HEIGHT/2+i][j]=new Dirt();
                }
            }
            else{
                for(int i=0;i>n;i--){
                    if(i-1<n)world[WORLD_HEIGHT/2+i][j]=new grassDirt();
                    else world[WORLD_HEIGHT/2+i][j]=new Air(0);
                }
            }
        }
    }
    public void PoolGenesis(){
        int Pooltotal=10;
        for(int z=0;z<Pooltotal;z++){
            int PoolX=z*WORLD_WID/Pooltotal+(int)r.nextGaussian()*5;
            int PoolY=(int)r.nextGaussian()*7;
            int PoolW=(int)r.nextGaussian()*10;
            for(int j=0;j>-PoolY;j--){
                for(int i=PoolX;i<PoolW;i++){
                    world[j][i]=new Water();
                }
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
                if( backWorld[j][i-1]!=null || backWorld[j][i+1]!=null){
                    if(r.nextInt(100)>50){
                        backWorld[j][i]=new cloud();
                    }
                }
                else if(backWorld[j-1][i]!=null || backWorld[j+1][i]!=null){
                    if(r.nextInt(100)>80){
                        backWorld[j][i]=new cloud();
                    }
                }
                else {
                    if (r.nextInt(100) > 95) {
                        backWorld[j][i] = new cloud();
                    }
                }
            }
        }
    }
    public void HoleGenesis(){
        //perlin worms
    }
    public void PyramidGenesis(){

    }


    landGenerator(){
        basicGenesis();
        GrassLandGenesis();
        EndOfWorldGenesis();
        SlimTreeGenesis();
        CloudGenesis();
        //HoleGenesis();
        //PoolGenesis();
    }



}
