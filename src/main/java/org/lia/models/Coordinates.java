package org.lia.models;

import java.io.Serializable;
import java.util.Objects;

public class Coordinates implements Serializable {
    private static final long serialVersionUID = 85464768755190753L;

    private long x;
    private double y; //Максимальное значение поля: 113

    public Coordinates(long x, double y) {
        this.setX(x);
        this.setY(y);
    }

    public long getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public void setX(long x) {
        this.x = x;
    }

    public void setY(double y) {
        this.y = y;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Coordinates that)) return false;
        return x == that.x && Double.compare(y, that.y) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(x, y);
    }

    @Override
    public String toString() {
        return "Coordinates{" +
                "x=" + x +
                ", y=" + y +
                '}';
    }
}
