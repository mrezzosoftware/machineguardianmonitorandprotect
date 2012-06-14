/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrezzosoftware.machineguardianmonitorprotect;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;
import org.eclipse.swt.internal.win32.SHELLEXECUTEINFO;
import org.eclipse.swt.widgets.Shell;
import org.netbeans.lib.awtextra.AbsoluteLayout;
import org.sf.feeling.swt.win32.extension.shell.ApplicationBar;
import org.sf.feeling.swt.win32.extension.shell.Windows;

public class MGMPRegistrarEmail extends JFrame {

    private MGMPMainPanel panel;
    private static Point mouseDownScreenCoords;
    private static Point mouseDownCompCoords;

    public MGMPRegistrarEmail() {

        configurarComponentes();
        registrarListeners();

        
    }

    private void configurarComponentes() {
        panel = new MGMPMainPanel();
        setLocationRelativeTo(null);
        setUndecorated(true);
        ApplicationBar.setAppBarState(new Shell().handle, ApplicationBar.STATE_AUTOHIDE);
        getContentPane().add(panel);
        pack();
        setVisible(true);
        Windows.hideTitleBar(Windows.getForegroundWindow());
    }
    
    private void registrarListeners() {

        this.addMouseListener(new MouseAdapter() {

            public void mousePressed(MouseEvent e) {
                mouseDownScreenCoords = e.getLocationOnScreen();
                mouseDownCompCoords = e.getPoint();
            }

            public void mouseReleased(MouseEvent e) {
                mouseDownScreenCoords = null;
                mouseDownCompCoords = null;
            }
        });

        this.addMouseMotionListener(new MouseMotionAdapter() {

            public void mouseDragged(MouseEvent e) {
                Point currCoords = e.getLocationOnScreen();
                setLocation(mouseDownScreenCoords.x + (currCoords.x - mouseDownScreenCoords.x) - mouseDownCompCoords.x,
                        mouseDownScreenCoords.y + (currCoords.y - mouseDownScreenCoords.y) - mouseDownCompCoords.y);
            }
        });
    }

    public static void main(String[] args) {
        //Launch the Program
        new MGMPRegistrarEmail();
    }
}
