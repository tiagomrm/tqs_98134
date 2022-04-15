package io.cucumber.skeleton;

public class Belly {

    private int size;

    public void eat(int cukes) {
        this.size = cukes;
    }

    public int size() {
        return size;
    }

    public void waitFor(int hours) {
        size = Math.max((size - 42 * hours), 0);
    }

    public boolean isGrowling() {
        return size <= 0;
    }
}
