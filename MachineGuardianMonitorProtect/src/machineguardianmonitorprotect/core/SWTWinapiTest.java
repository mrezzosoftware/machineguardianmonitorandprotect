package machineguardianmonitorprotect.core;

import org.sf.feeling.swt.win32.extension.hook.Hook;
import org.sf.feeling.swt.win32.extension.hook.data.HookData;
import org.sf.feeling.swt.win32.extension.hook.data.KeyboardHookData;
import org.sf.feeling.swt.win32.extension.hook.data.Keyboard_LLHookData;
import org.sf.feeling.swt.win32.extension.hook.listener.HookEventListener;

public class SWTWinapiTest {

    public static void main(String args[]) {
        new SWTWinapiTest().adicionarHook();
    }

    private void adicionarHook() {
        
        Hook.KEYBOARD.addListener(this, new HookEventListener() {

            @Override
            public void acceptHookData(HookData hd) {
                KeyboardHookData hdll = (KeyboardHookData) hd;

                System.out.println("hdll.vkCode(): " + hdll.getVirtualKeyCode());
            }
        });

        Hook.KEYBOARD.install(this, true);
    }
}
