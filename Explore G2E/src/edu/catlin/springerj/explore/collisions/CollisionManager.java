package edu.catlin.springerj.explore.collisions;

import edu.catlin.springerj.g2e.core.AbstractEntity;
import edu.catlin.springerj.g2e.core.AbstractManager;
import edu.catlin.springerj.g2e.core.Core;
import edu.catlin.springerj.g2e.exception.InvalidComponentException;
import edu.catlin.springerj.g2e.math.Vector2;

import java.util.ArrayList;
import java.util.List;

public class CollisionManager extends AbstractManager {

    public ArrayList<CircleCollisionComponent> list = new ArrayList();

    @Override
    public AbstractManager add(AbstractEntity... es) {
    	super.add(es);
    	AbstractEntity e = es[0];
    	try {
            list.add(e.get(CircleCollisionComponent.class));
        } catch (InvalidComponentException ex) {
        }
        return this;
    }

    public boolean collisionLine(Vector2 p1, Vector2 p2, String name) {
        for (CircleCollisionComponent ccc : Core.getRootManager().getManager(CollisionManager.class).list) {
            if (name.equals(ccc.name)) {
                if (ccc.intersects(p1, p2)) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<CircleCollisionComponent> collisionLine(Vector2 p1, Vector2 p2) {
        List<CircleCollisionComponent> returnval = new ArrayList<CircleCollisionComponent>();
        for (CircleCollisionComponent ccc : Core.getRootManager().getManager(CollisionManager.class).list) {
            if (ccc.intersects(p1, p2)) {
                returnval.add(ccc);
            }
        }

        return returnval;
    }

    public boolean collisionPoint(Vector2 point, String name) {
        for (CircleCollisionComponent ccc : Core.getRootManager().getManager(CollisionManager.class).list) {
            if (name.equals(ccc.name)) {
                if (ccc.contains(point)) {
                    return true;
                }
            }
        }
        return false;
    }

    public <E extends AbstractEntity> E entityPoint(Vector2 point, Class<E> c) {
        for (CircleCollisionComponent ccc : Core.getRootManager().getManager(CollisionManager.class).list) {
            if (c.isInstance(ccc.entity)) {
                if (ccc.contains(point)) {
                    return (E) ccc.entity;
                }
            }
        }
        return null;
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update() {
    }

}
