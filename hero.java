import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

abstract class Mobs{
    protected int x;
    protected int y;
    protected int HP;
    public int getHP(){
        return HP;
    }
    public void modifyHP(int x){
        HP+=x;
    }
    public int getX(){
        return x;
    }
    public int getY(){
        return y;
    }
}


public class hero extends Mobs{
    private static final char Symbol='@';
    private static final Color heroColor=Color.RED;
    private int MAX_JUMP_HEIGHT;
    private int SPEED;
    private int JUMPING_ABILITY;
    private int Defense=0;
    private int Attack=0;
    private static final int LifeProtectionTimeAfterDamaged=10;
    private int LifeRegenerationSpeed=100; //every 5s
    private static final char HeartSymbol='\u2665';
    hero(){
        MAX_JUMP_HEIGHT=10;
        SPEED=1;
        JUMPING_ABILITY=2;
        HP=10;
        x=landGenerator.getWorWidth()/2;
        y=landGenerator.getWorHeight()/2;
    }
    private int Flag_HPModified=0;
    public void modifyHP(int x){
        if(x<=0 && Flag_HPModified!=0) return;
        HP+=x;
        if(x<0) {
            Flag_HPModified = LifeProtectionTimeAfterDamaged;  //0.5s protected
        }
    }
    public boolean isAlive(){
        return HP>0;
    }
    private int Timer_LifeRegeneration=0;
    public void draw(Graphics g){
        Timer_LifeRegeneration++;
        if(Timer_LifeRegeneration==LifeRegenerationSpeed){
            if(HP<10) HP++;
            Timer_LifeRegeneration=0;
        }
        g.setColor(heroColor);
        //count down Protect and blink
        if(Flag_HPModified!=0){
            Flag_HPModified--;
            if(Flag_HPModified%2==0)
                g.drawString(String.valueOf(Symbol),MainWindow.getWinWidth()/2-landGenerator.getFontSize()/2-2,MainWindow.getWinHeight()/2+6); //????????
        }
        else g.drawString(String.valueOf(Symbol),MainWindow.getWinWidth()/2-landGenerator.getFontSize()/2-2,MainWindow.getWinHeight()/2+6); //????????

        g.setColor(Color.RED);
        StringBuffer hearts=new StringBuffer();
        for(int i=0;i<HP;i++) hearts.append(HeartSymbol);
        g.drawString(hearts.toString(),MainWindow.getWinWidth() - 150, 20);
        //move and change world
        landGenerator.changeWorldBlock(y,x,new Air(0));
        move();
        landGenerator.changeWorldBlock(y,x,new SolidHeroBlock(this));
    }
    private Direction dir=Direction.STOP;
    private int JUMPloop=0;
    private int JUMP_flag=0;
    private int fallingDistance=0;
    public void setDir(Direction d){
        dir=d;
    }
    public void PrintDeath(){

    }
    private void move(){
        if(JUMP_flag==0) {
            if (Direction.containDir(dir, Direction.JUMP)) {
                JUMPloop++;
            }
        }
        if(JUMPloop>=MAX_JUMP_HEIGHT) {
            JUMP_flag=1;
        }
        if(JUMP_flag==1){
            if(JUMPloop==0) {
                JUMP_flag=0;
            }
            if(Direction.containDir(dir,Direction.JUMP)) {
                dir = Direction.dispose(dir,Direction.JUMP);
            }
        }
        switch(dir){
            case L:
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x-1);
                    if (b.permeable) {
                        x -= 1;
                    }
                    else if(b instanceof SolidMonsterBlock){
                        modifyHP(-((SolidMonsterBlock) b).damage);
                    }
                }
                break;
            case R:
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x+1);
                    if (b.permeable) {
                        x += 1;
                    }
                    else if(b instanceof SolidMonsterBlock){
                        modifyHP(-((SolidMonsterBlock) b).damage);
                    }
                }
                break;
            case JUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    if (b.permeable) {
                        y += 1;
                    }
                    else if(b instanceof SolidMonsterBlock){
                        modifyHP(-((SolidMonsterBlock) b).damage);
                    }
                }
                break;
            case LJUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    if (b.permeable) {
                        y += 1;
                    }
                    else if(b instanceof SolidMonsterBlock){
                        modifyHP(-((SolidMonsterBlock) b).damage);
                    }
                }
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x-1);
                    if (b.permeable) {
                        x -= 1;
                    }
                    else if(b instanceof SolidMonsterBlock){
                        modifyHP(-((SolidMonsterBlock) b).damage);
                    }
                }
                break;
            case RJUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    if (b.permeable) {
                        y += 1;
                    }
                    else if(b instanceof SolidMonsterBlock){
                        modifyHP(-((SolidMonsterBlock) b).damage);
                    }
                }
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x+1);
                    if (b.permeable) {
                        x += 1;
                    }
                    else if(b instanceof SolidMonsterBlock){
                        modifyHP(-((SolidMonsterBlock) b).damage);
                    }
                }
                break;
            case STOP:
                break;
        }
        //gravity
        Block b=landGenerator.getWorldBlock(y-1,x);
        if(b.permeable){
            y--;
            fallingDistance++;
        }
        else if(b instanceof SolidMonsterBlock){
            modifyHP(-((SolidMonsterBlock) b).damage);
        }
        else {
            //when landed, reset jump lock.
            JUMPloop=0;
            JUMP_flag=0;
            //drop damage
            int fallDamage=fallingDistance>30?-fallingDistance/(2*MAX_JUMP_HEIGHT):0;
            if(fallDamage<0) modifyHP(fallDamage);
            fallingDistance=0;
        }
    }
}
