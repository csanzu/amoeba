package org.example.amoeba.vos;

public enum JatekosJel {
    X,O;
    public JatekosJel opposite() {
        return this == X ? O : X;
    }

//enumnál amúgy nem kell toString, az alap is a name()-et adja vissza ami a konstans neve
/**
    @Override
    public String toString() {
        return name();
    }
 **/
}


