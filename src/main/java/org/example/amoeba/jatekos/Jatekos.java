package org.example.amoeba.jatekos;

import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;
import org.example.amoeba.vos.Pozicio;

public interface Jatekos {
    JatekosJel getJel();

    Pozicio lep(Tabla tabla);

    String getNev();
}
