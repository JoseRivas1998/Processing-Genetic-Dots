import processing.core.*;
import processing.event.KeyEvent;

import java.util.ArrayList;
import java.util.List;

public class GeneticDots extends PApplet {

    private static final int SIZE = 800;
    private static final int POPULATION_SIZE = 1000;
    private static final int NUM_GENES = 500;
    private static final float MUTATION_RATE = 0.1f;

    private PVector corner1;
    private PVector corner2;
    private boolean clicked;
    private Rectangle toAdd;

    private List<Rectangle> obstacles;

    private Population population;

    private Rectangle target;

    private boolean drawAll;

    @Override
    public void settings() {
        size(SIZE, SIZE);
    }

    @Override
    public void setup() {
        clicked = false;
        corner1 = new PVector();
        corner2 = new PVector();
        toAdd = new Rectangle();
        obstacles = new ArrayList<>();
        target = new Rectangle(width / 2f, 50, 5, 5);
        drawAll = false;
        population = new Population(width / 2f, height - 50, POPULATION_SIZE, NUM_GENES, target);
    }

    @Override
    public void draw() {
        update();
        render();
    }

    private void update() {
        applyGeneticAlgorithm();
        updateCorner2();
        population.update(this, obstacles);
    }

    private void applyGeneticAlgorithm() {
        if(population.isAllInactive()) {
            population.calcFitness();
            population.naturalSelection();
            population.mutate(MUTATION_RATE);
        }
    }

    private void render() {
        background(255);
        drawObstacles();
        drawObstacleToBe();
        fill(255, 0, 0);
        target.display(this);
        population.display(drawAll, this);
    }

    private void updateCorner2() {
        if (clicked) {
            corner2.x = mouseX;
            corner2.y = mouseY;
        }
    }

    private void drawObstacleToBe() {
        if (clicked) {
            toAdd.set(corner1, corner2);
            fill(155);
            toAdd.display(this);
        }
    }

    private void drawObstacles() {
        fill(0, 0, 255);
        for (Rectangle obstacle : obstacles) {
            obstacle.display(this);
        }
    }

    @Override
    public void mouseClicked() {
        if (!clicked) {
            corner1.x = mouseX;
            corner1.y = mouseY;
        } else {
            corner2.x = mouseX;
            corner2.y = mouseY;
            toAdd.set(corner1, corner2);
            obstacles.add(new Rectangle(toAdd));
        }
        clicked = !clicked;
    }

    @Override
    public void keyPressed(KeyEvent event) {
        if(event.getKeyCode() == DOWN) {
            drawAll = !drawAll;
        } else if(event.getKeyCode() == ENTER) {
            obstacles.clear();
        }
    }

    public static void main(String[] args) {
        PApplet.main("GeneticDots");
    }

}
