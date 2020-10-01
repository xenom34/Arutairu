package fr.altairstudios.arutairu;

import java.io.Serializable;
import java.util.Vector;

public class SelectedItemList implements Serializable {
    private Vector<Integer> selected;
    private Vector<Integer> correspondingIndex;
    private Vector<String> mJP;
    private Vector<String> mRomaji;
    private Vector<String> mFrench;
    private Vector<String> mTts;
    private int correspondingLesson;

    public void addCorrespondingIndex(int word){
        correspondingIndex.add(word);
    }

    public int getCorrespondingIndex(int word){
        return correspondingIndex.elementAt(word);
    }

    public void removeCorrespondingIndex(int index){
        correspondingIndex.remove(index);
    }

    public int getCorrespondingLesson() {
        return correspondingLesson;
    }

    public Vector<Integer> getSelected() {
        return selected;
    }

    public void setCorrespondingLesson(int correspondingLesson) {
        this.correspondingLesson = correspondingLesson;
    }

    public Vector<String> getmJP() {
        return mJP;
    }

    public Vector<String> getmRomaji() {
        return mRomaji;
    }

    public Vector<String> getmFrench() {
        return mFrench;
    }

    public Vector<String> getmTts(){return mTts;}

    void addRomaji(String word){
        mRomaji.add(word);
    }

    void addJp(String word){
        mJP.add(word);
    }

    void addFrench(String word){
        mFrench.add(word);
    }

    void addTts(String word){
        mTts.add(word);
    }


    public SelectedItemList() {
        selected = new Vector<>();
        mRomaji = new Vector<>();
        mFrench = new Vector<>();
        mJP = new Vector<>();
        correspondingIndex = new Vector<>();
        mTts = new Vector<>();
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
