package org.judovana.calendarmaker;

import javax.swing.*;
import java.io.IOException;

/**
 * Hello world!
 *
 */
public class App 
{
    private static class Args {
        boolean week = false;
        Integer year = null;
    }

    public static void main( String[] args )
    {
        final Args a = new Args();
        for(String arg: args){
            if (arg.matches("^-+type=.+$")){
                if (arg.split("=")[1].toUpperCase().equals("WEEK")) {
                    a.week=true;
                }
            }
            if (arg.matches("^-+year=.+$")){
                a.year=Integer.valueOf(arg.split("=")[1]);
            }
        }
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {
                try {
                    new MainFrame(a.week, a.year).setVisible(true);
                }catch(IOException ex){
                    throw new RuntimeException(ex);
                }
            }
        });
    }
}
