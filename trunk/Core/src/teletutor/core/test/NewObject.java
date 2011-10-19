/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package teletutor.core.test;

import teletutor.core.services.TeleChannel;
import teletutor.core.services.TeleObject;
import teletutor.core.services.UpdateInfo;

/**
 *
 * @author Sabin Timalsena
 */
public class NewObject extends TeleObject {
    public String str;
    
    public NewObject(String name, TeleChannel chan) throws Exception {
        super(name, chan);
    }

    @Override
    public void received(Object obj) {
        if (obj instanceof NewMessage) {
            setStr(((NewMessage) obj).getMsg());
        }
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
        registerFieldChange("str", str);
    }

    @Override
    public void objectUpdated(UpdateInfo changes) {
        return;
    }
    
}
