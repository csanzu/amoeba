package org.example.amoeba.vos;

public class Pozicio {
    private int sor;
    private int oszlop;

    public int getSor() {
        return sor;
    }

    public void setSor(int sor) {
        this.sor = sor;
    }

    public int getOszlop() {
        return oszlop;
    }

    public void setOszlop(int oszlop) {
        this.oszlop = oszlop;
    }

    public Pozicio(int sor, int oszlop) {
        this.sor = sor;
        this.oszlop = oszlop;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        Pozicio pozicio = (Pozicio) o;
        return sor == pozicio.sor && oszlop == pozicio.oszlop;
    }

    @Override
    public int hashCode() {
        return 31 * sor + oszlop;
    }


}