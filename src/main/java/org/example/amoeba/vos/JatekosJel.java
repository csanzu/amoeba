package org.example.amoeba.vos;

public enum JatekosJel {
    X, O;
    public JatekosJel opposite() {
        return this == X ? O : X;
    }
}


