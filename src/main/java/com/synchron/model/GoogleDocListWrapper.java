package com.synchron.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * Created by AnGo on 12.06.2017.
 */

@XmlRootElement(name = "GOOGLE_DOCS")
public class GoogleDocListWrapper {
    private List<GoogleDoc> googleDocList;

    @XmlElement(name = "google_doc")
    public List<GoogleDoc> getGoogleDocList() {
        return googleDocList;
    }

    public void setGoogleDocList(List<GoogleDoc> googleDocList) {
        this.googleDocList = googleDocList;
    }
}
