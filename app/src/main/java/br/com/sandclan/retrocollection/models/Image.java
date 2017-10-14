
package br.com.sandclan.retrocollection.models;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.io.Serializable;
import java.util.Map;


@Root(name = "Images", strict = false)
public class Image implements Serializable{


    @Element(name = "clearlogo", required = false)
    private String clearLogo;


    @ElementList(inline = true, name = "screenshot", required = false)
    private String screenshot;


    @ElementMap(entry = "boxart", key = "side",value = "thumb", attribute = true, inline = true, required = false)
    private Map<String, String> boxart;


    public String getClearLogo() {
        return clearLogo;
    }

    public void setClearLogo(String clearLogo) {
        this.clearLogo = clearLogo;
    }

    public String getScreenshot() {
        return screenshot;
    }

    public void setScreenshot(String screenshot) {
        this.screenshot = screenshot;
    }


    public Map<String, String> getBoxart() {
        return boxart;
    }

    public void setBoxart(Map<String, String> boxart) {
        this.boxart = boxart;
    }
}
