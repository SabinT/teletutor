package teletutor.core.services;

import java.io.Serializable;
import java.util.HashMap;

/**
 * Contains key/value pairs of the properties of objects, recorded on calling 
 * the setter methods, and transmitted across the network when changes are 
 * "flushed"
 * @author Rae
 */
public class UpdateInfo extends HashMap<String,Serializable> implements Serializable {
    
}
