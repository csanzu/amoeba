package org.example.amoeba.DB;

import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;

public record BetoltottJatek(
        Tabla tabla,
        JatekosJel aktualisJel,
        String emberNev
) {}