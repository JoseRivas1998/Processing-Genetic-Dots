import processing.core.PApplet;
import processing.core.PVector;

import java.util.List;

public class Population {

    private Dot[] dots;
    private float fitnessSum;
    private int maxSteps;
    private int numGenes;
    boolean first;

    public Population(float x, float y, int populationSize, int numGenes, Rectangle target) {
        dots = new Dot[populationSize];
        maxSteps = numGenes;
        this.numGenes = numGenes;
        first = true;
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new Dot(x, y, numGenes, numGenes, target);
        }
    }

    public void update(PApplet applet, List<Rectangle> obstacles) {
        for (Dot dot : dots) {
            dot.update(applet, obstacles);
        }
    }

    public void display(boolean drawAll, PApplet applet) {
        if(first || drawAll) {
            for (Dot dot : dots) {
                applet.fill(0);
                dot.display(applet);
            }
        } else {
            applet.fill(0);
            dots[0].display(applet);
            dots[0].displayPath(applet);
        }
    }

    public boolean isAllInactive() {
        for (Dot dot : dots) {
            if(dot.isActive()) return false;
        }
        return true;
    }

    public void calcFitness() {
        for (Dot dot : dots) {
            dot.calculateFitness();
        }
    }

    public void calculateFitnessSum() {
        fitnessSum = 0f;
        for (Dot dot : dots) {
            fitnessSum += dot.getFitness();
        }
    }

    public void naturalSelection() {
        first = false;
        Dot[] newDots = new Dot[dots.length];
        calculateFitnessSum();

        int best = getBestDot();
        newDots[0] = dots[best].copy();

        calcMaxSteps();

        for (int i = 1; i < newDots.length; i++) {
            Dot parentA = selectParent();
            Dot parentB = selectParent();
            newDots[i] = parentA.crossover(parentB);
            newDots[i].setMaxSteps(maxSteps);
        }

        dots = newDots;
    }

    public void mutate(float mutationRate) {
        for (int i = 1; i < dots.length; i++) {
            dots[i].mutate(mutationRate);
        }
    }

    private void calcMaxSteps() {
        int lowestSteps = numGenes;
        for (Dot dot : dots) {
            if(dot.isWon() && dot.getStep() < lowestSteps) {
                lowestSteps = dot.getStep();
            }
        }
        maxSteps = lowestSteps;
    }

    public Dot selectParent() {
        float rand = (float) (Math.random() * fitnessSum);
        float runningSum = 0;
        for (Dot dot : dots) {
            runningSum += dot.getFitness();
            if(runningSum > rand) {
                return dot;
            }
        }
        return null;
    }

    public int getBestDot() {
        int bestDot = 0;
        float bestFitness = 0;
        for (int i = 0; i < dots.length; i++) {
            Dot dot = dots[i];
            if(dot.getFitness() > bestFitness) {
                bestDot = i;
                bestFitness = dot.getFitness();
            }
        }
        return bestDot;
    }

}
