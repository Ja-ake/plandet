package edu.catlin.springerj.explore.jake.items;

import edu.catlin.springerj.explore.jake.newjake.CircleCollisionComponent;
import edu.catlin.springerj.g2e.core.AbstractEntity;
import edu.catlin.springerj.g2e.core.AbstractSystem;
import edu.catlin.springerj.g2e.core.Core;
import edu.catlin.springerj.g2e.lwjgl.draw.Graphics;
import edu.catlin.springerj.g2e.lwjgl.SpriteComponent;
import edu.catlin.springerj.g2e.math.Vector2;
import edu.catlin.springerj.g2e.movement.PositionComponent;
import edu.catlin.springerj.g2e.movement.RotationComponent;
import edu.catlin.springerj.g2e.movement.VelocityComponent;

public class GrappleSystem extends AbstractSystem {

    private PositionComponent pc;
    private VelocityComponent vc;
    private RotationComponent rc;
    private SpriteComponent sc;
    private GrappleComponent gc;
    private CircleCollisionComponent ccc;

    @Override
    public void initialize(AbstractEntity e) {
        pc = e.get(PositionComponent.class);
        vc = e.get(VelocityComponent.class);
        rc = e.get(RotationComponent.class);
        sc = e.get(SpriteComponent.class);
        gc = e.get(GrappleComponent.class);
        ccc = e.get(CircleCollisionComponent.class);
    }

    @Override
    public void update() {
        System.out.println(sc.alpha);
        Vector2 playerPos = gc.player.getComponent(PositionComponent.class).position;
        //Graphics
        rc.rot = pc.position.subtract(playerPos).direction() - Math.PI / 2;
        Graphics.drawLine(pc.position.x, pc.position.y, playerPos.x, playerPos.y, 0, 0, 0, sc.alpha);
        //If you're active
        if (sc.alpha == 1) {
            //If you should die
            if (pc.position.subtract(playerPos).lengthSquared() > 100000) {
                vc.velocity = new Vector2();
                sc.alpha = .5;
            } else {
                if (ccc.pc != null) {
                    CircleCollisionComponent planet = ccc.touching("Planet");
                    if (planet != null) {
                        vc.velocity = planet.vc.velocity;
                        Vector2 disp = planet.pc.position.subtract(playerPos).setLength(2000 * Core.getDefaultTimer().getDeltaTime());
                        if (planet.pc.position.subtract(playerPos).lengthSquared() > 10000) {
                            planet.vc.velocity = planet.vc.velocity.subtract(disp.multiply(planet.invMass));
                            gc.player.getComponent(VelocityComponent.class).velocity
                                    = gc.player.getComponent(VelocityComponent.class).velocity.add(disp.multiply(gc.player.getComponent(CircleCollisionComponent.class).invMass));
                        } else {
                            sc.alpha = .5;
                        }
                    }
                }
            }
        } else {
            //Inactive
            sc.alpha -= .2 * Core.getDefaultTimer().getDeltaTime();
        }
        //If you're ready to be destroyed
        if (sc.alpha < 0) {
            if (getManager() != null) {
                getManager().remove(gc.grapple);
            }
        }
    }

}
