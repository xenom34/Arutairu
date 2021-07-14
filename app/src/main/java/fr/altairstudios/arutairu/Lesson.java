package fr.altairstudios.arutairu;

import java.io.Serializable;

public class Lesson implements Serializable {
    private int kjRes;
    private final int jpnRes;
    private int romRes;
    private final int srcRes;
    private final int enRes;

    private String[] jpnArray;
    private String[] srcArray;
    private String[] romArray;

    private final boolean romaji;
    private final boolean kanji;
    private final boolean customLesson;

    private String title;

    public Lesson(int kjRes, int jpnRes, int romRes, int srcRes, int enRes) {
        this.jpnRes = jpnRes;
        this.romRes = romRes;
        this.srcRes = srcRes;
        this.romaji = true;
        this.kanji = true;
        this.customLesson = false;
        this.enRes = enRes;
        this.kjRes = kjRes;
    }

    public Lesson(String[] jpnArray, String[] srcArray, String[] romArray, String title) {
        this.jpnArray = jpnArray;
        this.srcArray = srcArray;
        this.romArray = romArray;
        this.jpnRes = Integer.MIN_VALUE;
        this.srcRes = Integer.MIN_VALUE;
        this.enRes = Integer.MIN_VALUE;
        this.title = title;
        this.romaji = true;
        this.kanji = false;
        this.customLesson = true;
    }

    public String getTitle() {
        return title;
    }

    public boolean haveRomaji() {
        return romaji;
    }

    public Lesson(int jpnRes, int romRes, int srcRes, int enRes) {
        this.jpnRes = jpnRes;
        this.srcRes = srcRes;
        this.romRes = romRes;
        this.enRes = enRes;
        this.kanji = false;
        this.customLesson = false;
        this.romaji = true;
    }

    public Lesson(int jpnRes, int srcRes, int enRes) {
        this.jpnRes = jpnRes;
        this.srcRes = srcRes;
        this.enRes = enRes;
        this.romaji = false;
        this.kanji = false;
        this.customLesson = false;
    }

    public String[] getJpnArray() {
        return jpnArray;
    }

    public String[] getSrcArray() {
        return srcArray;
    }

    public String[] getRomArray() {
        return romArray;
    }

    public int getJpnRes() {
        return jpnRes;
    }

    public int getRomRes() {
        return romRes;
    }

    public int getSrcRes() {
        return srcRes;
    }

    public int getKjRes() {
        return kjRes;
    }

    public int getEnRes() {
        return enRes;
    }

    public boolean haveKanji() {
        return kanji;
    }
}
