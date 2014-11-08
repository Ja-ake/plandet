package edu.catlin.springerj.g2e.object.physics;

import edu.catlin.springerj.g2e.math.Vector2;
import java.util.ArrayList;
import java.util.Arrays;

public class Edge implements CollisionShape {

    public Vector2 p1;
    public Vector2 p2;

    public Edge(Vector2 p1, Vector2 p2) {
        this.p1 = p1;
        this.p2 = p2;
    }

    @Override
    public AABB getAABB() {
        return new AABB(new Vector2(Math.min(p1.x, p2.x), Math.min(p1.y, p2.y)), new Vector2(Math.max(p1.x, p2.x), Math.max(p1.y, p2.y)));
    }

    public Vector2 getDisplacement() {
        return p2.subtract(p1);
    }

    public Vector2 getNormal() {
        return getDisplacement().normal();
    }

    @Override
    public ArrayList<Vector2> getProjAxes(CollisionShape other) {
        return new ArrayList(Arrays.asList(getNormal()));
    }

    @Override
    public ProjectionRange getProjRange(Vector2 axis) {
        ProjectionRange r = new ProjectionRange();
        r.max = r.min = p1.dot(axis);
        r.max_low = r.max_high = r.min_low = r.min_high = p1.dot(axis.normal());
        double x = p2.dot(axis);
        if (x >= r.max) {
            r.max = x;
            double y = p2.dot(axis.normal());
            if (y > r.max_high) {
                r.max_high = y;
            } else if (y < r.max_low) {
                r.max_low = y;
            }
        }
        if (x <= r.min) {
            r.min = x;
            double y = p2.dot(axis.normal());
            if (y > r.min_high) {
                r.min_high = y;
            } else if (y < r.min_low) {
                r.min_low = y;
            }
        }
        return r;
    }

    @Override
    public void setPos(Vector2 pos) {
        p2 = p2.add(pos.subtract(p1));
        p1 = pos;
    }

    @Override
    public void setRot(double rot) {
        //Do nothing lol
        //Hope noone ever tries to change the rotation of a line or something
    }

}
