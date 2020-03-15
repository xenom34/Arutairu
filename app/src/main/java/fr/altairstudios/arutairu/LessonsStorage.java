package fr.altairstudios.arutairu;

import android.util.SparseArray;

import java.nio.file.DirectoryIteratorException;
import java.util.HashMap;

class LessonsStorage {
    private SparseArray<Lesson> storage = new SparseArray<>();
    static final int NUMBERS = 1;
    static final int PEOPLE = 2;
    static final int JOBS = 3;
    static final int BODY = 4;
    private static final int FAMILY = 5;
    private static final int ANIMALS = 6;
    private static final int PLANTS = 7;
    private static final int CROPS = 8;
    private static final int FOOD = 9;
    private static final int DRINK = 10;
    private static final int SEASONING = 11;
    private static final int DAYS = 12;
    private static final int WEATHER = 13;
    private static final int DIRECTIONS = 14;
    private static final int MATERIALS = 15;
    private static final int WEIGHTS = 16;
    private static final int SOCIETY = 17;
    private static final int HOME = 18;
    private static final int TOOLS = 19;
    private static final int STATIONERY = 20;
    private static final int CLOTHES = 21;
    private static final int TRANSPORT = 22;
    private static final int LANGUAGE = 23;
    private static final int MEDIA = 24;
    private static final int COLORS = 25;
    private static final int OTHERS = 26;
    private static final int ABSTRACT = 27;
    

    LessonsStorage() {
        storage.put(NUMBERS, new Lesson(R.array.numbersJP, R.array.numbersRM, R.array.numbersFR));
        storage.put(PEOPLE, new Lesson(R.array.peopleJS, R.array.peopleRM, R.array.peopleFR));
        storage.put(JOBS, new Lesson(R.array.jobsJP, R.array.jobsRM, R.array.jobsFR));
        storage.put(BODY, new Lesson(R.array.bodyJP, R.array.bodyRM, R.array.bodyFR));
        storage.put(FAMILY, new Lesson(R.array.familyJP, R.array.familyRM, R.array.familyFR));
        storage.put(ANIMALS, new Lesson(R.array.animalsJP, R.array.animalsRM, R.array.animalsFR));
        storage.put(PLANTS, new Lesson(R.array.plantsJP, R.array.plantsRM, R.array.plantsFR));
        storage.put(CROPS, new Lesson(R.array.cropsJP, R.array.cropsRM, R.array.cropsFR));
        storage.put(FOOD, new Lesson(R.array.foodJP, R.array.foodRM, R.array.foodFR));
        storage.put(DRINK, new Lesson(R.array.drinkJP, R.array.drinkRM, R.array.drinkFR));
        storage.put(SEASONING, new Lesson(R.array.seasoningJP, R.array.seasoningRM, R.array.seasoneryFR));
        storage.put(DAYS, new Lesson(R.array.daysJP, R.array.weekRM, R.array.weekFR));
        storage.put(WEATHER, new Lesson(R.array.weatherJP, R.array.weatherRM, R.array.weatherFR));
        storage.put(DIRECTIONS, new Lesson(R.array.directionsJP, R.array.directionsRM, R.array.directionsFR));
        storage.put(MATERIALS, new Lesson(R.array.materialsJP, R.array.materialsRM, R.array.materialsFR));
        storage.put(WEIGHTS, new Lesson(R.array.weightsJP, R.array.weightsRM, R.array.weightFR));
        storage.put(SOCIETY, new Lesson(R.array.societyJP, R.array.societyRM, R.array.societyFR));
        storage.put(HOME, new Lesson(R.array.homeJP, R.array.homeRM, R.array.homeFR));
        storage.put(TOOLS, new Lesson(R.array.toolsJP, R.array.toolsRM, R.array.toolsFR));
        storage.put(STATIONERY, new Lesson(R.array.stationeryJP, R.array.stationeryRM, R.array.stationery));
        storage.put(CLOTHES, new Lesson(R.array.clothesJP, R.array.clothesJP, R.array.clothesFR));
        storage.put(TRANSPORT, new Lesson(R.array.transportJP, R.array.transportRM, R.array.transportFR));
        storage.put(LANGUAGE, new Lesson(R.array.languageJP, R.array.languageRM, R.array.languageFR));
        storage.put(MEDIA, new Lesson(R.array.mediaJP, R.array.mediaRM, R.array.mediasFR));
        storage.put(COLORS, new Lesson(R.array.colorsJP, R.array.colorsRM, R.array.colorsFR));
        storage.put(OTHERS, new Lesson(R.array.othersJP, R.array.colorsRM, R.array.colorsFR));
        storage.put(ABSTRACT, new Lesson(R.array.abstractJP, R.array.asbstractRM, R.array.asbstractFR));
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


}
