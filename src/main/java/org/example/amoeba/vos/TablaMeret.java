package org.example.amoeba.vos;

public record TablaMeret(int szelesseg, int magassag) {
    public TablaMeret{
        if (szelesseg < 4 || szelesseg > 25 || magassag < 4 || magassag > 25) {
            throw new IllegalArgumentException("szelesseg es magassag 4 es 25 kozott lehet");
        }
    }
    public int getSzelesseg() {
        return szelesseg;
    }
    public int getMagassag() {
        return magassag;
    }
}
