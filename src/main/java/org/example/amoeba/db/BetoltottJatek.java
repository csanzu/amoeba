package org.example.amoeba.db;

import org.example.amoeba.tabla.Tabla;
import org.example.amoeba.vos.JatekosJel;

public record BetoltottJatek(
        Tabla tabla,
        JatekosJel aktualisJel,
        String emberNev
) {}