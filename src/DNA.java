import processing.core.PVector;

import java.util.NoSuchElementException;

public class DNA {

    private int numGenes;
    private PVector[] genes;
    private int step;

    public DNA(int numGenes) {
        this.numGenes = numGenes;
        genes = new PVector[numGenes];
        this.step = 0;
        randomize(numGenes);
    }

    private void randomize(int numGenes) {
        for (int i = 0; i < numGenes; i++) {
            genes[i] = PVector.random2D();
        }
    }

    public boolean hasNext() {
        return step < genes.length;
    }

    public PVector next() {
        if (!hasNext()) throw new NoSuchElementException();
        PVector next = genes[step].copy();
        step++;
        return next;
    }

    public DNA copy() {
        DNA clone = new DNA(this.numGenes);
        for (int i = 0; i < this.numGenes; i++) {
            clone.genes[i] = this.genes[i].copy();
        }
        return clone;
    }

    public DNA crossover(DNA parent) {
        DNA child = new DNA(this.numGenes);

        int halfIndex = this.numGenes / 2;

        for (int i = 0; i < this.numGenes; i++) {
            if (i < halfIndex) {
                child.genes[i] = genes[i].copy();
            } else {
                child.genes[i] = parent.genes[i].copy();
            }
        }

        return child;
    }

    public void mutate(float mutationRate) {
        for (int i = 0; i < genes.length; i++) {
            if (Math.random() < mutationRate) {
                genes[i] = PVector.random2D();
            }
        }
    }

    public int getStep() {
        return step;
    }
}
