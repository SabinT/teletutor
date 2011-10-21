/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.test;


/**
 *
 * @author Sabin Timalsena
 */
public class JPT_Child extends JPT {

    protected Integer y;

    public Integer getY() {
        return y;
    }

    public void setY(Integer y) {
        this.y = y;
    }
    
    public static void main(String[] args) {
        JPT_Child kpt = new JPT_Child();
        kpt.setField("y", new Integer(20));
        System.out.println(kpt.getY());
        
    }
}