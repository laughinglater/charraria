import javafx.concurrent.WorkerStateEvent;
import sun.awt.Symbol;

import java.awt.*;

public abstract class Entity {
    protected static final int Block2EntityOffset=1000;
    protected int EntityNo;
    protected char symbol;
    protected Color color;
    protected int Total;
    protected boolean stackable;
    protected String description;
    public boolean getEntityStackable(){
        return stackable;
    }
    public int getEntityNo(){
        return EntityNo;
    }
    public char getEntitySymbol(){
        return symbol;
    }
    public Color getEntityColor(){
        return color;
    }
    protected double WorkDist;
    public double getWorkDist(){
        return WorkDist;
    }
}

class EntityBlock extends Entity{
    public Block block;
    EntityBlock(Block b){
        super();
        block=b;
        stackable=true;
        symbol=b.symbol;
        EntityNo=b.BlockNo+Block2EntityOffset;
        Total=1;
        if(MainWindow.DEBUG==1) WorkDist=999;
        else WorkDist=10;
    }
}


abstract class Tools extends Entity{
    int Slowness;
    int DestroyAbility;
    Tools(){
        super();
        stackable=false;
        Total=1;
    }
}


abstract class Weapon extends Tools{
    int attack;
    Weapon(){
        super();
    }
}

abstract class LongRangedWeapon extends Weapon{
    public int SPEED;
    LongRangedWeapon(){
        super();
    }
}

class Hand extends Weapon{
    Hand(){
        super();
        DestroyAbility=5;
        attack=1;
        Slowness=100;  //ms
        EntityNo=0;
        symbol='@';
        color=Color.WHITE;
        description="Bare Hand is the best!";
        WorkDist=2;
    }
}

class HandOfGod extends Weapon{
    HandOfGod(){
        super();
        DestroyAbility=999;
        attack=999;
        Slowness=10;
        EntityNo=0;
        symbol='@';
        color=Color.orange;
        description="ONLY for DEBUG!";
        WorkDist=999;
    }
}

class Arrow extends LongRangedWeapon{
    Arrow(){
        super();
        DestroyAbility=0;
        attack=999;
        Slowness=1000;
        EntityNo=1;
        symbol='←';
        WorkDist=999;
        SPEED=1;
    }
}

