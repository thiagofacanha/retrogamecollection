
package br.com.sandclan.retrocollection.models;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "Data", strict = false)
public class GamePlatform {


    @Element(name = "baseImgUrl",required = false)
    private String baseImgUrl;

    @ElementList(inline = true,name="Game",required = false)
    private List<Game> games;


    public List<Game> getGames() {
        return games;
    }

    public void setGames(List<Game> games) {
        this.games = games;
    }

    public String getBaseImgUrl() {
        return baseImgUrl;
    }

    public void setBaseImgUrl(String baseImgUrl) {
        this.baseImgUrl = baseImgUrl;
    }
}
