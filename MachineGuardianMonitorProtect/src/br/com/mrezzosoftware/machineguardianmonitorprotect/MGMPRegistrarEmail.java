/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrezzosoftware.machineguardianmonitorprotect;

import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JFrame;
import javax.swing.UnsupportedLookAndFeelException;

public class MGMPRegistrarEmail extends JFrame {

    private MGMPMainPanel panel;
    private static Point mouseDownScreenCoords;
    private static Point mouseDownCompCoords;

    public MGMPRegistrarEmail() {

        definirAparencia();
        configurarComponentes();
        registrarListeners();

        
    }
    
    private void definirAparencia() {
        try {
            
            javax.swing.UIManager.setLookAndFeel(javax.swing.UIManager.getSystemLookAndFeelClassName());
            
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MGMPRegistrarEmail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            Logger.getLogger(MGMPRegistrarEmail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            Logger.getLogger(MGMPRegistrarEmail.class.getName()).log(Level.SEVERE, null, ex);
        } catch (UnsupportedLookAndFeelException ex) {
            Logger.getLogger(MGMPRegistrarEmail.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    private void configurarComponentes() {
        panel = new MGMPMainPanel();
        getContentPane().add(panel);
        setLocationRelativeTo(null);
        setUndecorated(true);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pack();
        setVisible(true);
    }
    
    private void registrarListeners() {
        
//        panel.getLblFechar().addMouseListener(new MouseAdapter() {
//
//            public void mousePressed(MouseEvent e) {
//                MGMPRegistrarEmail.this.dispose();
//            }
//        });

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
