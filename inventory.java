import java.awt.*;
import java.util.ArrayList;

public class inventory {
    private static final int InventoryFontSize=20;
    private Font InventoryFont=new Font("arial",Font.BOLD,InventoryFontSize);
    private Font InventoryDescriptionFont=new Font("arial",Font.ITALIC,InventoryFontSize/2);
    private int volume=10;
    private int current=0;
    private ArrayList<Entity> a=new ArrayList<Entity>(){
        @Override
        public boolean contains(Object o) {
            Entity en=(Entity) o;
            for(Entity e:this){
                if(e.EntityNo==en.EntityNo)
                    return true;
            }
            return false;
        }

        @Override
        public int indexOf(Object o) {
            Entity en=(Entity) o;
            for(int i=0;i<this.size();i++){
                if(this.get(i).EntityNo==en.EntityNo){
                    return i;
                }
            }
            return -1;
        }
    };

    inventory(){
        a.add(new Hand());
        if(MainWindow.DEBUG==1){
            a.add(new HandOfGod());
        }
    }

    public boolean isFull(){
        return a.size()==volume;
    }

    public Entity getCurrent(){
        if(current>=a.size()){
            current=a.size()-1;
        }
        return a.get(current);
    }

    public void moveCurrent(int mv){
        if(mv<0){
            for(int i=mv;i<0;i++){
                if(current-1>=0) current--;
            }
        }
        else{
            for(int i=0;i<mv;i++){
                if(current+1<a.size()) current++;
            }
        }
    }

    public void setCurrent(int s){
        System.out.println("serCurrent "+s);
        if(s<a.size() && s>=0){
            current=s;
            System.out.println("successfully set");
        }
    }

    public void addEntity(Entity en){
        /*
        if(a.size()==volume) {
            return false;
        }
        */
        System.out.println("ADD:"+en.symbol);
        if(a.contains(en) && a.get(a.indexOf(en)).stackable ){
            a.get(a.indexOf(en)).Total++;
            System.out.println("Contains:"+en.Total);
            return;
        }
        a.add(en);
        System.out.println("Newly added "+en.symbol);
    }

    public void removeEntity(Entity en){
        if(a.contains(en)) {
            a.remove(a.indexOf(en));
        }
    }

    public void draw(Graphics g){
        //  (*)4 (.)1
        StringBuffer s=new StringBuffer("INV:");
        StringBuffer d=new StringBuffer();
        for(int i=0;i<a.size();i++){
            Entity en=a.get(i);
            if(i==current){
                s.append("<"+en.symbol+">"+en.Total+" ");
                d.append(en.description);
            }
            else {
                s.append("(" + en.symbol + ")" + en.Total + " ");
            }
        }
        g.setFont(InventoryFont);
        g.setColor(Color.black);
        g.drawString(s.toString(),0,15);
        g.setFont(InventoryDescriptionFont);
        g.drawString(d.toString(),0,30);
    }

}
