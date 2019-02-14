import processing.core.PApplet;
import processing.core.PVector;

import java.util.ArrayList;
import java.util.List;

import static processing.core.PApplet.dist;

public class Dot {

    private DNA dna;

    private Rectangle bounds;
    private PVector initialPosition;
    private int numGenes;
    private int maxSteps;

    private PVector acc;
    private PVector vel;

    private Rectangle target;

    private boolean dead;
    private boolean won;

    private float fitness;

    private List<PVector> path;

    public Dot(float x, float y, int numGenes, int maxSteps, Rectangle target) {
        this.numGenes = numGenes;
        this.maxSteps = maxSteps;
        this.dna = new DNA(numGenes);
        bounds = new Rectangle(x, y, 5, 5);
        initialPosition = new PVector(bounds.getX(), bounds.getY());
        acc = new PVector();
        vel = new PVector();
        this.target = target;
        dead = false;
        won = false;
        this.fitness = 0;
        path = new ArrayList<>();
    }

    private void move() {
        if (dna.hasNext()) {
            acc.set(dna.next());
            vel.add(acc);
            vel.limit(5);
            bounds.setX(bounds.getX() + vel.x);
            bounds.setY(bounds.getY() + vel.y);
            path.add(new PVector(bounds.getX(),bounds.getY()));
        } else {
            dead = true;
        }
    }

    public void update(PApplet applet, List<Rectangle> obstacles) {
        if (isActive()) {
            move();
            if (getStep() >= maxSteps) {
                dead = true;
            } else if (bounds.getX() < 0 ||
                    bounds.getX() + bounds.getWidth() > applet.width ||
                    bounds.getY() < 0 ||
                    bounds.getY() + bounds.getHeight() > applet.height) {
                dead = true;
            } else if(bounds.overlaps(target)) {
                won = true;
            } else {
                for (Rectangle obstacle : obstacles) {
                    if(bounds.overlaps(obstacle)) {
                        dead = true;
                        break;
                    }
                }
            }
        }
    }

    public void display(PApplet applet) {
        bounds.display(applet);
    }

    public void displayPath(PApplet applet) {
        applet.noFill();
        applet.beginShape();
        for (PVector pVector : path) {
            applet.vertex(pVector.x, pVector.y);
        }
        applet.endShape();
    }

    public Dot crossover(Dot parent) {
        Dot child = new Dot(initialPosition.x, initialPosition.y, this.numGenes, this.maxSteps, this.target);
        child.dna = dna.crossover(parent.dna);
        return child;
    }

    public void mutate(float mutationRate) {
        dna.mutate(mutationRate);
    }

    public int getStep() {
        return dna.getStep();
    }

    public int getMaxSteps() {
        return maxSteps;
    }

    public void setMaxSteps(int maxSteps) {
        this.maxSteps = maxSteps;
    }

    public void calculateFitness() {
        if(won) {
            fitness = 1f/16f + (10000f) / (dna.getStep() * dna.getStep());
        } else {
            float distanceToGoal = dist(bounds.getX(), bounds.getY(), target.getX(), target.getY());
            fitness = 1.0f/(distanceToGoal * distanceToGoal);
        }
    }

    public boolean isActive() {
        return !dead && !won;
    }

    public float getFitness() {
        return fitness;
    }

    public Dot copy() {
        Dot clone = new Dot(initialPosition.x, initialPosition.y, numGenes, maxSteps, target);
        clone.dna = dna.copy();
        return clone;
    }

    public boolean isDead() {
        return dead;
    }

    public boolean isWon() {
        return won;
    }
}
