
package br.com.sandclan.retrocollection.models;


import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.ElementMap;
import org.simpleframework.xml.Root;

import java.util.List;


@Root(name = "Images", strict = false)
public class Image {


    @Element(name = "clearlogo", required = false)
    private String clearLogo;


    @ElementList(inline = true,name = "screenshot", required = false)
    private String screenshot;




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

}
