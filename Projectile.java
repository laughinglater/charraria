import java.awt.*;
import java.util.ArrayList;

public class Projectile {
    public int x;
    public int y;
    public int attack;
    public Color ProjectileColor;
    public char Symbol;
    public int SPEED;
    public int JUMPING_ABILITY=2;
    public Direction dir;
    public boolean isGood;
    private int Flag_hit=0;  //only hit and cause damage once, then disappear
    private void move(){
        switch(dir){
            case L:
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x-1);
                    if (b.permeable) {
                        x -= 1;
                    }
                    else{
                        if(b instanceof SolidMobsBlock) {
                            ((SolidMobsBlock) b).m.modifyHP(-attack);
                        }
                        Flag_hit=1;
                    }
                }
                break;
            case R:
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x+1);
                    if (b.permeable) {
                        x += 1;
                    }
                    else{
                        if(b instanceof SolidMobsBlock) {
                            ((SolidMobsBlock) b).m.modifyHP(-attack);
                        }
                        Flag_hit=1;
                    }
                }
                break;
            case JUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    if (b.permeable) {
                        y += 1;
                    }
                    else{
                        if(b instanceof SolidMobsBlock) {
                            ((SolidMobsBlock) b).m.modifyHP(-attack);
                        }
                        Flag_hit=1;
                    }
                }
                break;
            case LJUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    if (b.permeable) {
                        y += 1;
                    }
                    else{
                        if(b instanceof SolidMobsBlock) {
                            ((SolidMobsBlock) b).m.modifyHP(-attack);
                        }
                        Flag_hit=1;
                    }
                }
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x-1);
                    if (b.permeable) {
                        x -= 1;
                    }
                    else{
                        if(b instanceof SolidMobsBlock) {
                            ((SolidMobsBlock) b).m.modifyHP(-attack);
                        }
                        Flag_hit=1;
                    }
                }
                break;
            case RJUMP:
                for(int s=0;s<JUMPING_ABILITY;s++) {
                    Block b=landGenerator.getWorldBlock(y+1,x);
                    if (b.permeable) {
                        y += 1;
                    }
                    else{
                        if(b instanceof SolidMobsBlock) {
                            ((SolidMobsBlock) b).m.modifyHP(-attack);
                        }
                        Flag_hit=1;
                    }
                }
                for(int s=0;s<SPEED;s++) {
                    Block b=landGenerator.getWorldBlock(y,x+1);
                    if (b.permeable) {
                        x += 1;
                    }
                    else{
                        if(b instanceof SolidMobsBlock) {
                            ((SolidMobsBlock) b).m.modifyHP(-attack);
                        }
                        Flag_hit=1;
                    }
                }
                break;
            case STOP:
                break;
        }
        //gravity
        /*
        Block b=landGenerator.getWorldBlock(y-1,x);
        if(b.permeable){
            y--;
        }
        else{
            if(b instanceof SolidMobsBlock) {
                ((SolidMobsBlock) b).m.modifyHP(-attack);
            }
            Flag_hit=1;
        }
        */
    }
    public void draw(Graphics g){
        System.out.println("draw projectile");
        if(x>=landGenerator.Focus_x-landGenerator.getWinOffsetX() && x<= landGenerator.Focus_x+landGenerator.getWinOffsetX() && y>=landGenerator.Focus_y-landGenerator.getWinOffsetY() && y<=landGenerator.Focus_y+landGenerator.getWinOffsetY()){
            if(Flag_hit==0) {
                g.setColor(ProjectileColor);
                g.drawString(String.valueOf(Symbol), (x - (landGenerator.Focus_x - landGenerator.getWinOffsetX())) * landGenerator.getFontSize(), MainWindow.getWinHeight() - (y - (landGenerator.Focus_y - landGenerator.getWinOffsetY())) * landGenerator.getFontSize());
            }
        }
        move();
    }
    Projectile(int _x,int _y,int _atk, char _sym, int _Spd, Direction _dir, Color _color){
        x=_x;
        y=_y;
        attack=_atk;
        ProjectileColor=_color;
        dir=_dir;
        SPEED=_Spd;
        Symbol=_sym;
    }
}

