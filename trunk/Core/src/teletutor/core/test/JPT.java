/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.test;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Sabin Timalsena
 */
public class JPT {

    private Integer x;

    public void setField( String fieldName, Object value) {
        Class cls = this.getClass();
        
        while (cls.getSuperclass() != null) {
            System.out.println(cls.getName());
            
            try {
                Field field = cls.getDeclaredField(fieldName);
                field.set(this, value);
                break;
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(JPT.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(JPT.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(JPT.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(JPT.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // if field not found in this class, check the superclass
            cls = cls.getSuperclass();
        }
    }
    
    public Integer getX() {
        return x;
    }

    public void setX(Integer x) {
        this.x = x;
    }
}
