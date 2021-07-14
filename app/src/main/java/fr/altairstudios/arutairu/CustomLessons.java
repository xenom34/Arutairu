package fr.altairstudios.arutairu;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomLessons implements Serializable {
    ArrayList<Lesson> listCustomLessons;

    public void addLesson(Lesson lesson){
        listCustomLessons.add(lesson);
    }

    public CustomLessons(ArrayList<Lesson> listCustomLessons) {
        this.listCustomLessons = listCustomLessons;
    }
}
