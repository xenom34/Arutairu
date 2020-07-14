package fr.altairstudios.arutairu;

import android.util.SparseArray;

class LessonsStorage {
    private SparseArray<Lesson> storage = new SparseArray<>();
    static final int HIRAGANA = 1;
    static final int DAKUTEN = 2;
    static final int KATAKANA = 3;
    static final int DAKUTENKATA = 4;
    static final int NUMBERS = 5;
    static final int PEOPLE = 6;
    static final int JOBS = 7;
    static final int BODY = 8;
    static final int FAMILY = 9;
    static final int ANIMALS = 10;
    static final int PLANTS = 11;
    static final int CROPS = 12;
    static final int FOOD = 13;
    static final int DRINK = 14;
    static final int DAYS = 16;
    static final int WEATHER = 17;
    static final int DIRECTIONS = 18;
    static final int MATERIALS = 19;
    static final int WEIGHTS = 20;
    static final int SOCIETY = 21;
    static final int HOME = 22;
    static final int TOOLS = 23;
    static final int STATIONERY = 24;
    static final int CLOTHES = 25;
    static final int TRANSPORT = 26;
    static final int LANGUAGE = 27;
    static final int MEDIA = 28;
    static final int COLORS = 29;
    static final int SUBJECT = 30;
    static final int TIME = 31;
    static final int N5 = 32;
    static final int N4 = 33;
    static final int N3 = 34;
    static final int N2 = 35;
    static final int N1 = 36;
    static final int K1 = 37;
    static final int K2 = 38;
    static final int K3 = 39;
    static final int K4 = 40;
    static final int K5 = 41;
    static final int RADICALS = 42;
    static final int SENTENCES = 43;
    static final int GEOGRAPHY = 44;


    static final int TOTAL = 42;
    

    LessonsStorage() {
        storage.put(HIRAGANA, new Lesson(R.array.hiraganaJP, R.array.hiraganaRM, R.array.hiraganaFR, R.array.hiraganaFR));
        storage.put(KATAKANA, new Lesson(R.array.katakanaJP, R.array.katakanaRM, R.array.katakanaFR, R.array.katakanaFR));
        storage.put(NUMBERS, new Lesson(R.array.numbersKJ, R.array.numbersJP, R.array.numbersRM, R.array.numbersFR, R.array.numbersFR));
        storage.put(PEOPLE, new Lesson(R.array.peopleKJ, R.array.peopleJS, R.array.peopleRM, R.array.peopleFR, R.array.peopleEN));
        storage.put(JOBS, new Lesson(R.array.jobsKJ, R.array.jobsJP, R.array.jobsRM, R.array.jobsFR, R.array.jobsEN));
        storage.put(BODY, new Lesson(R.array.bodyKJ, R.array.bodyJP, R.array.bodyRM, R.array.bodyFR, R.array.bodyEN));
        storage.put(FAMILY, new Lesson(R.array.familyKJ, R.array.familyJP, R.array.familyRM, R.array.familyFR, R.array.familyEN));
        storage.put(ANIMALS, new Lesson(R.array.animalsKJ, R.array.animalsJP, R.array.animalsRM, R.array.animalsFR, R.array.animalsEN));
        storage.put(PLANTS, new Lesson(R.array.animalsKJ, R.array.plantsJP, R.array.plantsRM, R.array.plantsFR, R.array.plantsEN));
        storage.put(CROPS, new Lesson(R.array.cropsKJ, R.array.cropsJP, R.array.cropsRM, R.array.cropsFR, R.array.cropsEN));
        storage.put(FOOD, new Lesson(R.array.foodKJ, R.array.foodJP, R.array.foodRM, R.array.foodFR, R.array.foodEN));
        storage.put(DRINK, new Lesson(R.array.foodKJ, R.array.drinkJP, R.array.drinkRM, R.array.drinkFR, R.array.drinkEN));
        storage.put(DAYS, new Lesson(R.array.weeksKJ, R.array.weekJP, R.array.weekRM, R.array.weekFR, R.array.weekEN));
        storage.put(WEATHER, new Lesson(R.array.weatherKJ, R.array.weatherJP, R.array.weatherRM, R.array.weatherFR, R.array.weatherEN));
        storage.put(DIRECTIONS, new Lesson(R.array.directionsKJ, R.array.directionsJP, R.array.directionsRM, R.array.directionsFR, R.array.directionsEN));
        storage.put(MATERIALS, new Lesson(R.array.materialsKJ, R.array.materialsJP, R.array.materialsRM, R.array.materialsFR, R.array.materialsEN));
        storage.put(WEIGHTS, new Lesson(R.array.weightsKJ, R.array.weightsJP, R.array.weightsRM, R.array.weightFR, R.array.weightsEN));
        storage.put(SOCIETY, new Lesson(R.array.societyKJ, R.array.societyJP, R.array.societyRM, R.array.societyFR, R.array.societyEN));
        storage.put(HOME, new Lesson(R.array.homeKJ, R.array.homeJP, R.array.homeRM, R.array.homeFR, R.array.homeEN));
        storage.put(TOOLS, new Lesson(R.array.toolsKJ, R.array.toolsJP, R.array.toolsRM, R.array.toolsFR, R.array.toolsEN));
        storage.put(STATIONERY, new Lesson(R.array.stationeryKJ, R.array.stationeryJP, R.array.stationeryRM, R.array.stationeryFR, R.array.stationeryEN));
        storage.put(CLOTHES, new Lesson(R.array.clothesKJ, R.array.clothesJP, R.array.clothesJP, R.array.clothesFR, R.array.clothesEN));
        storage.put(TRANSPORT, new Lesson(R.array.transportKJ, R.array.transportJP, R.array.transportRM, R.array.transportFR, R.array.transportEN));
        storage.put(LANGUAGE, new Lesson(R.array.languageKJ, R.array.languageJP, R.array.languageRM, R.array.languageFR, R.array.languageEN));
        storage.put(MEDIA, new Lesson(R.array.mediaKJ, R.array.mediaJP, R.array.mediaRM, R.array.mediasFR, R.array.mediaEN));
        storage.put(COLORS, new Lesson(R.array.colorsKJ, R.array.colorsJP, R.array.colorsRM, R.array.colorsFR, R.array.colorsEN));
        storage.put(SUBJECT, new Lesson(R.array.subjectKJ, R.array.subjectJP, R.array.subjectRM, R.array.subjectFR, R.array.subjectEN));
        storage.put(N5, new Lesson(R.array.JLPTN5_KJ, R.array.JLPTN5_JP, R.array.JLPTN5_RM, R.array.JLPTN5_FR, R.array.JLPTN5_EN));
        storage.put(TIME, new Lesson(R.array.timeKJ, R.array.timeJP, R.array.timeRM, R.array.timeFR, R.array.timeEN));
        storage.put(N4, new Lesson(R.array.JLPTN4_KJ, R.array.JLPTN4_JP, R.array.JLPTN4_RM, R.array.JLPTN4_FR, R.array.JLPTN4_EN));
        storage.put(N3, new Lesson(R.array.JLPTN3_KJ, R.array.JLPTN3_JP, R.array.JLPTN3_RM, R.array.JLPTN3_FR, R.array.JLPTN3_EN));
        storage.put(N2, new Lesson(R.array.JLPTN2_KJ, R.array.JLPTN2_JP, R.array.JLPTN2_RM, R.array.JLPTN2_FR, R.array.JLPTN2_EN));
        storage.put(N1, new Lesson(R.array.JLPTN1_KJ, R.array.JLPTN1_JP, R.array.JLPTN1_RM, R.array.JLPTN1_FR, R.array.JLPTN1_EN));
        storage.put(K1, new Lesson(R.array.KANJIN1_JP, R.array.KANJIN1_FR, R.array.KANJIN1_EN));
        storage.put(K2, new Lesson(R.array.KANJIN2_JP, R.array.KANJIN2_FR, R.array.KANJIN2_EN));
        storage.put(K3, new Lesson(R.array.KANJIN3_JP, R.array.KANJIN3_FR, R.array.KANJIN3_EN));
        storage.put(K4, new Lesson(R.array.KANJIN4_JP, R.array.KANJIN4_FR, R.array.KANJIN4_EN));
        storage.put(K5, new Lesson(R.array.KANJIN5_JP, R.array.KANJIN5_FR, R.array.KANJIN5_EN));
        storage.put(RADICALS, new Lesson(R.array.radicals_JP, R.array.radicals_FR, R.array.radicals_EN));
        storage.put(SENTENCES, new Lesson(R.array.sentencesKJ, R.array.sentencesJP, R.array.sentencesRM, R.array.sentencesFR, R.array.sentencesEN));
        storage.put(GEOGRAPHY, new Lesson(R.array.geographyKJ, R.array.geographyJP, R.array.geographyRM, R.array.geographyFR, R.array.geographyEN));


    }
    int getJpRes(int i){
        return storage.get(i).getJpn();
    }

    int getRmRes(int i){
        return storage.get(i).getRom();
    }

    int getSrcRes(int i){
        return storage.get(i).getSrc();
    }

    int getKjRes(int i){
        return storage.get(i).getKj();
    }

    int getEnRes(int i){
        return storage.get(i).getEn();
    }

    boolean haveRomaji(int i){
        return storage.get(i).haveRomaji();
    }

    boolean haveKanji(int i){
        return storage.get(i).haveKanji();
    }


}
