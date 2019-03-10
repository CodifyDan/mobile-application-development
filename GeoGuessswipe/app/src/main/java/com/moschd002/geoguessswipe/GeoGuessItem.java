package com.moschd002.geoguessswipe;

public class GeoGuessItem {

    private String mGeoGuessName;
    private int mGeoGuessImageName;
    private boolean mGeoGuessInEurope;

    public GeoGuessItem(String mGeoGuessName, int mGeoGuessImageName, boolean mGeoGuessInEurope) {
        this.mGeoGuessName = mGeoGuessName;
        this.mGeoGuessImageName = mGeoGuessImageName;
        this.mGeoGuessInEurope = mGeoGuessInEurope;
    }

    public String getmGeoGuessName() {
        return mGeoGuessName;
    }

    public void setmGeoGuessName(String mGeoGuessName) {
        this.mGeoGuessName = mGeoGuessName;
    }

    public int getmGeoGuessImageName() {
        return mGeoGuessImageName;
    }

    public void setmGeoGuessImageName(int mGeoGuessImageName) {
        this.mGeoGuessImageName = mGeoGuessImageName;
    }

    public boolean ismGeoGuessInEurope() {
        return mGeoGuessInEurope;
    }

    public void setmGeoGuessInEurope(boolean mGeoGuessInEurope) {
        this.mGeoGuessInEurope = mGeoGuessInEurope;
    }

    public static final String[] GEO_GUESS_COUNTRY_NAMES = {
            "Denmark",
            "Canada",
            "Bangladesh",
            "Kazakhstan",
            "Colombia",
            "Poland",
            "Malta",
            "Thailand"
    };

    public static final boolean[] GEO_GUESS_INEUROPE = {
            true,
            false,
            false,
            true,
            false,
            true,
            true,
            false
    };

    public static final int[] GEO_GUESS_IMAGES = {
            R.drawable.img1_yes_denmark,
            R.drawable.img2_no_canada,
            R.drawable.img3_no_bangladesh,
            R.drawable.img4_yes_kazachstan,
            R.drawable.img5_no_colombia,
            R.drawable.img6_yes_poland,
            R.drawable.img7_yes_malta,
            R.drawable.img8_no_thailand,
    };
}
