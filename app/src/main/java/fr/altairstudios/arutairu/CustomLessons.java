package fr.altairstudios.arutairu;

import java.io.Serializable;
import java.util.ArrayList;

public class CustomLessons implements Serializable {
    private final ArrayList<Lesson> listCustomLessons;

    public void addLesson(Lesson lesson){
        listCustomLessons.add(lesson);
    }

    public ArrayList<Lesson> getList() {
        return listCustomLessons;
    }

    public boolean isEmpty(){
        return listCustomLessons.isEmpty();
    }

    public CustomLessons() {
        this.listCustomLessons = new ArrayList<>();
    }
}
