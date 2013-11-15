/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package Utils;

import GUI.MainGUIWindow;

/**
 *
 * @author C. Levallois
 */
public class LogUpdate {

    private static String lastString;

    public static void update(String string) {
//        if (string != null && !string.isEmpty() && !string.equals(lastString)) {
        if (string != null && !string.isEmpty()) {
            MainGUIWindow.jTextAreaLog.setText(MainGUIWindow.jTextAreaLog.getText().concat(string).concat("\n"));
            MainGUIWindow.jTextAreaLog.setCaretPosition(MainGUIWindow.jTextAreaLog.getText().length());
            lastString = string;
        }
    }
}
