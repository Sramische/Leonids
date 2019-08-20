package com.plattysoft.leonids.initializers;

import com.plattysoft.leonids.Particle;

import java.util.Random;

public class PositionInitializer implements ParticleInitializer {

    protected int left;
    protected int top;
    protected int right;
    protected int bottom;

    private boolean randomX;
    private boolean randomY;

    public PositionInitializer(){}

    public PositionInitializer(int left, int top, int right, int bottom){
       setBounds(left, top, right, bottom);
    }

    public void setRandomizationRules(boolean randomX, boolean randomY){
        this.randomX = randomX;
        this.randomY = randomY;
    }

    public void setBounds(int left, int top, int right, int bottom){
        this.left = left;
        this.top = top;
        this.right = right;
        this.bottom = bottom;
    }

    @Override
    public void initParticle(Particle p, Random r) {
        int x = randomX ? getFromRange(r, left, right) : left;
        int y = randomY ? getFromRange(r, top, bottom) : top;
        p.setPosition(x, y);
    }

    protected int getFromRange(Random r, int minValue, int maxValue) {
        if (minValue == maxValue) {
            return minValue;
        }
        if (minValue < maxValue) {
            return r.nextInt(maxValue - minValue) + minValue;
        }
        else {
            return r.nextInt(minValue - maxValue) + maxValue;
        }
    }
}
