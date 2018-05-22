import javax.swing.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class MainCmd extends JTextField {
    MainCmd(){

    }
    private class cmdKeyAdaptor extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            super.keyPressed(e);


        }
    }
}
