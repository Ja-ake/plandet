package edu.catlin.springerj.explore.collisions;

import edu.catlin.springerj.g2e.core.AbstractEntity;
import edu.catlin.springerj.g2e.core.AbstractSystem;
import edu.catlin.springerj.g2e.core.Core;
import edu.catlin.springerj.g2e.math.Vector2;

public class CircleCollisionSystem extends AbstractSystem {

    private CircleCollisionComponent ccc;

    public void collide(CircleCollisionComponent other) {
        if (ccc.intersects(other)) {
            //System.out.println("collision: " + ccc.pc.position + other.pc.position);
            double dist = ccc.pc.position.subtract(other.pc.position).length();
            //Get the collison axis
            Vector2 axis = ccc.pc.position.subtract(other.pc.position).multiply(1 / dist);
            //Get the collision depth
            double depth = ccc.size + other.size - dist;
            //Calculate the relative velocity
            Vector2 vel = ccc.vc.velocity.subtract(other.vc.velocity);
            //The velocity should be the opposite direction to the axis
            if (vel.dot(axis) < 0) {
                //Calculate the impulse
                double impulse = -1 * vel.dot(axis) / (ccc.invMass + other.invMass);
                //Apply impulse
                ccc.applyImpulse(axis.multiply(impulse));
                other.applyImpulse(axis.multiply(-impulse));
                //Move them outside each other
                ccc.pc.position = ccc.pc.position.add(axis.multiply(depth * ccc.invMass / (ccc.invMass + other.invMass)));
                other.pc.position = other.pc.position.add(axis.multiply(-depth * other.invMass / (ccc.invMass + other.invMass)));
                //Calculate the friction
//                double normalSpeed = ccc.vc.velocity.subtract(other.vc.velocity).dot(axis.normal());
//                double frictionImpulse = 0;
//                double u = 0;//.2;
//                if (normalSpeed > 0) {
//                    if (normalSpeed > -impulse * u) {
//                        frictionImpulse = -impulse * u;
//                    } else {
//                        frictionImpulse = normalSpeed;
//                    }
//                } else if (normalSpeed < 0) {
//                    if (normalSpeed < impulse * u) {
//                        frictionImpulse = impulse * u;
//                    } else {
//                        frictionImpulse = -normalSpeed;
//                    }
//                }
//                //Apply the friction impulse
//                ccc.applyImpulse(axis.normal().multiply(frictionImpulse));
//                other.applyImpulse(axis.normal().multiply(-frictionImpulse));
            }
        }
    }

    @Override
    public void initialize(AbstractEntity e) {
        ccc = e.get(CircleCollisionComponent.class);
    }

    @Override
    public void update() {
        if (ccc.solid) {
            for (CircleCollisionComponent other : Core.getRootManager().getManager(CollisionManager.class).list) {
                if (other != ccc) {
                    if (other.solid) {
                        collide(other);
                    }
                }
            }
        }
    }

}
