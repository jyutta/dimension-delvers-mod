package com.dimensiondelvers.dimensiondelvers.item.runegem;


public enum RuneGemTier {

    RAW("raw"),
    SHAPED("shaped"),
    CUT("cut"),
    POLISHED("polished"),
    FRAMED("framed"),
    UNIQUE("unique");

    private final String name;

    private RuneGemTier(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
