
package br.com.sandclan.retrocollection.models;

import java.util.List;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;

@Root(name = "Game", strict = false)
public class Game {

    @Element(name = "id",required = false)
    private long id;

    @Element(name = "GameTitle",required = false)
    private String gameTitle;

    @Element(name = "PlatformId",required = false)
    private long platformId;

    @Element(name = "Platform",required = false)
    private String platformName;

    @Element(name = "ReleaseDate",required = false)
    private String releaseDate;

    @Element(name = "Overview",required = false)
    private String overview;

    @ElementList(inline = true,name="Images",required = false)
    private List<Image> images;




    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGameTitle() {
        return gameTitle;
    }

    public void setGameTitle(String gameTitle) {
        this.gameTitle = gameTitle;
    }

    public long getPlatformId() {
        return platformId;
    }

    public void setPlatformId(long platformId) {
        this.platformId = platformId;
    }

    public String getPlatformName() {
        return platformName;
    }

    public void setPlatformName(String platformName) {
        this.platformName = platformName;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(String releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }


    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }
}
