package com.moschd002.studentportal;

import java.io.Serializable;

public class PortalItem implements Serializable {
    private String mPortalName;
    private String mPortalLink;

    public PortalItem(String mPortalName, String mPortalLink) {
        this.mPortalName = mPortalName;
        this.mPortalLink = mPortalLink;
    }

    public String getmPortalName() {
        return mPortalName;
    }

    public void setmPortalName(String mPortalName) {
        this.mPortalName = mPortalName;
    }

    public String getmPortalLink() {
        return mPortalLink;
    }

    public void setmPortalLink(String mPortalLink) {
        this.mPortalLink = mPortalLink;
    }
}
