package fr.altairstudios.arutairu;

public class Lesson {
    private int jpn, rom, src;

    public Lesson(int jpn, int rom, int src) {
        this.jpn = jpn;
        this.rom = rom;
        this.src = src;
    }

    public int getJpn() {
        return jpn;
    }

    public int getRom() {
        return rom;
    }

    public int getSrc() {
        return src;
    }
}
