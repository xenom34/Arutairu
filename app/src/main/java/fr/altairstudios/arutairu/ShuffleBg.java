package fr.altairstudios.arutairu;

import java.util.Vector;

public class ShuffleBg {
    private Vector<Integer> storage = new Vector<>();

    public ShuffleBg() {
        storage.add(R.drawable.one);
        storage.add(R.drawable.two);
        storage.add(R.drawable.three);
        storage.add(R.drawable.four);
        storage.add(R.drawable.five);
    }

    public int choose(){
        return storage.elementAt((int)(Math.random()*storage.size()));
    }
}
