package fr.altairstudios.arutairu;

import java.util.Vector;

public class SelectedItemList {
    private Vector<Integer> selected;

    public SelectedItemList() {
        selected = new Vector<>();
    }

    void addSelected(int word){
        selected.add(word);
    }

    boolean isSelected(int word){
        for (Integer i: selected) {
            if (i.equals(word))
                return true;
        }
        return false;
    }

    void removeSelected(int word){
        selected.removeElement(word);
    }
}
