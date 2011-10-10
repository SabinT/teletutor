/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.impl;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import org.jgroups.Header;
import org.jgroups.util.Streamable;

/**
 *
 * @author Rae
 */
public class SubChannelHeader extends Header implements Streamable {
    private String subChannelName;
    
    public SubChannelHeader() {
        subChannelName = "NONAME";
    }
    
    public SubChannelHeader (String name) {
        subChannelName = name;
    }

    public String getSubChannelName() {
        return subChannelName;
    }
    
    @Override
    public int size() {
        // string size + 'length' short, works for 8-bit characters only
        return subChannelName.length() + 2;
    }

    @Override
    public void writeTo(DataOutputStream stream) throws IOException {
        stream.writeUTF(subChannelName);
    }

    @Override
    public void readFrom(DataInputStream stream) throws IOException, IllegalAccessException, InstantiationException {
        short size = stream.readShort();
        byte[] buf = new byte[size];
        
        stream.read(buf);
        subChannelName = new String(buf,"UTF-8");
    }
}
