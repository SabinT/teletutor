/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.utilities;

import java.lang.reflect.Field;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Convenience class to handle manipulating object properties using reflection
 *
 * @author Sabin Timalsena
 */
public class FieldUtil {
    /**
     * Attempts to set the given value for the given field of the given object
     * 
     * @param obj The object to set the values on
     * @param fieldName The field for which to set the value
     * @param value 
     */
    public static void setField(Object obj, String fieldName, Object value) {
        Class cls = obj.getClass();
        
        while (cls.getSuperclass() != null) {
            System.out.println(cls.getName());
            
            try {
                Field field = cls.getDeclaredField(fieldName);
                field.set(obj, value);
                break;
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(FieldUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(FieldUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(FieldUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(FieldUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            // if field not found in this class, check the superclass
            cls = cls.getSuperclass();
        }
    }
    
    public static Object getField(Object obj, String fieldName) {
        Class cls = obj.getClass();
        
        // check for the requested field int the superclasses as well
        while (cls.getSuperclass() != null) {
            System.out.println(cls.getName());
            
            try {
                Field field = cls.getDeclaredField(fieldName);
                return field.get(obj);
            } catch (IllegalArgumentException ex) {
                Logger.getLogger(FieldUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (IllegalAccessException ex) {
                Logger.getLogger(FieldUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (NoSuchFieldException ex) {
                Logger.getLogger(FieldUtil.class.getName()).log(Level.SEVERE, null, ex);
            } catch (SecurityException ex) {
                Logger.getLogger(FieldUtil.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            cls = cls.getSuperclass();
        }
        
        return null;
    }
}
