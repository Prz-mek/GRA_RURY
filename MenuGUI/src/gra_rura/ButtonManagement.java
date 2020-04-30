/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package gra_rura;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
 *
 * @author User
 */
public class ButtonManagement {

    public static Icon b1Icon;
    public static Icon b2Icon;
    
    public static JButton button1 = null;
    public static JButton button2 = null;

    public ButtonManagement() {
    }

    public void swapImages(JButton button) {

        if (button1 == null) {
            button1 = button;
            b1Icon = button.getIcon();
        } else {
            button2 = button;
            b2Icon = button.getIcon();
            button1.setIcon(b2Icon);
            button2.setIcon(b1Icon);
            button1 = null;
            button2 = null;
        }
    }
}
