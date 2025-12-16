package org.example.amoeba.jatekos;

import org.example.amoeba.tabla.Pozicio;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.tabla.Tabla;

public interface Jatekos {
    JatekosJel jel();
    Pozicio lep(Tabla tabla);
    String getNev();
}
