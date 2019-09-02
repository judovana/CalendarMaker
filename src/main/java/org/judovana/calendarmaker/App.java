package org.judovana.calendarmaker;

import javax.swing.*;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainFrame().setVisible(true);
                }catch(IOException ex){
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
