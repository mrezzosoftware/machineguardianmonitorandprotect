/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package br.com.mrezzosoftware.machineguardianmonitorprotect.core;

import br.com.mrezzosoftware.machineguardianmonitorprotect.windows.MGMPWindows;

/**
 *
 * @author 01837484333
 */
public class Teclas {

    public enum VkCode {

        Back(8, "[BACKSPACE]"),
        Tab(9, "[TAB]"),
        Clear(12, "[CLEAR]"),
        Enter(13, "[ENTER]"),
        Shift(16, "[SHIFT]"),
        Control(17, "[CTRL]"),
        Menu(18, "[MENU]"),
        Pause(19, "[PAUSE]"),
        Capslock(20, "[CAPS_LOCK]"),
        Escape(27, "[ESC]"),
        Space(32, "[SPACEBAR]"),
        PageUp(33, "[PAGE_UP]"),
        PageDown(34, "[PAGE_DOWN]"),
        End(35, "[END]"),
        Home(36, "[HOME]"),
        Left(37, "[LEFT_ARROW]"),
        Up(38, "[UP_ARROW]"),
        Right(39, "[RIGHT_ARROW]"),
        Down(40, "[DOWN_ARROW]"),
        Select(41, "[SELECT]"),
        PrintScreen(42, "[PRINT_SCREEN]"),
        Execute(43, "[EXECUTE]"),
        Snapshot(44, "[SNAPSHOT]"),
        Insert(45, "[INS]"),
        Delete(46, "[DEL]"),
        Help(47, "[HELP]"),
        Numlock(144, "[NUM_LOCK]"),
        A(65, "A"),
        B(66, "B"),
        C(67, "C"),
        D(68, "D"),
        E(69, "E"),
        F(70, "F"),
        G(71, "G"),
        H(72, "H"),
        I(73, "I"),
        J(74, "J"),
        K(75, "K"),
        L(76, "L"),
        M(77, "M"),
        N(78, "N"),
        O(79, "O"),
        P(80, "P"),
        Q(81, "Q"),
        R(82, "R"),
        S(83, "S"),
        T(84, "T"),
        U(85, "U"),
        V(86, "V"),
        W(87, "W"),
        X(88, "X"),
        Y(89, "Y"),
        Z(90, "Z");
        
        private final int vkCode;
        private final String descricaoTecla;

        private VkCode(int vkCode, String descricaoTecla) {
            this.vkCode = vkCode;
            this.descricaoTecla = descricaoTecla;
        }

        public int getVkCode() {
            return vkCode;
        }

        public String getDescricaoTecla() {
            MGMPWindows w = new MGMPWindows();
            return descricaoTecla;
        }
    }
}
