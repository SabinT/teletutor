/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.test;

import teletutor.core.test2.KPT;
import teletutor.core.utilities.FieldUtil;


/**
 *
 * @author Sabin Timalsena
 */
public class Test {
    public static void main(String[] args) {
        KPT kpt = new KPT();
        kpt.setField("y", new Integer(20));
        System.out.println(kpt.getY());
        
        
        
    }
    
}
