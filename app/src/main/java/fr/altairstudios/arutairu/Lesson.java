package fr.altairstudios.arutairu;

public class Lesson {
    private int kj, jpn, rom, src,en;
    private boolean romaji, kanji;

    public Lesson(int kj, int jpn, int rom, int src, int en) {
        this.jpn = jpn;
        this.rom = rom;
        this.src = src;
        this.romaji = true;
        this.kanji = true;
        this.en = en;
        this.kj = kj;
    }

    public boolean haveRomaji() {
        return romaji;
    }

    public Lesson(int jpn, int rom, int src, int en) {
        this.jpn = jpn;
        this.src = src;
        this.rom = rom;
        this.en = en;
        this.kanji = false;
        this.romaji = true;
    }

    public Lesson(int jpn, int src, int en) {
        this.jpn = jpn;
        this.src = src;
        this.en = en;
        this.romaji = false;
        this.kanji = false;
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

    public int getKj() {
        return kj;
    }

    public int getEn() {
        return en;
    }

    public boolean haveKanji() {
        return kanji;
    }
}
