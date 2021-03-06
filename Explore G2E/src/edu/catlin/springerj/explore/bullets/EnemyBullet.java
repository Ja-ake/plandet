package edu.catlin.springerj.explore.bullets;

import edu.catlin.springerj.explore.collisions.CircleCollisionComponent;
import edu.catlin.springerj.explore.collisions.CollisionManager;
import edu.catlin.springerj.explore.enemy.HealthComponent;
import edu.catlin.springerj.explore.player.PlayerEntity;
import edu.catlin.springerj.g2e.core.AbstractEntity;
import edu.catlin.springerj.g2e.core.Core;
import edu.catlin.springerj.g2e.lwjgl.SpriteComponent;
import edu.catlin.springerj.g2e.lwjgl.draw.Graphics;
import edu.catlin.springerj.g2e.math.Vector2;
import edu.catlin.springerj.g2e.movement.PositionComponent;
import edu.catlin.springerj.g2e.movement.VelocityComponent;
import edu.catlin.springerj.g2e.movement.VelocityMovementSystem;
import edu.catlin.springerj.g2e.thread.Task;
import edu.catlin.springerj.g2e.thread.TaskThread;

public class EnemyBullet extends AbstractEntity {

    public EnemyBullet(Vector2 position, Vector2 velocity) {
        //Components
        add(new PositionComponent(position));
        add(new VelocityComponent(velocity));
        add(new BulletComponent());
        //Systems
        add(new VelocityMovementSystem());
        add(new BulletSystem());
    }

    @Override
    public void initialize() {
    }

    @Override
    public void update() {
        PlayerEntity p = Core.getRootManager().getManager(CollisionManager.class).entityPoint(getComponent(PositionComponent.class).position, PlayerEntity.class);
        if (p != null) {
            p.get(HealthComponent.class).damage(5);
            final SpriteComponent sc = p.getComponent(SpriteComponent.class);
            sc.setSprite("character_idle_left_red", 8);
            p.get(CircleCollisionComponent.class).applyImpulse(get(VelocityComponent.class).velocity.setLength(1000));
            Core.getRootManager().remove(this);
            Core.task(new Task(Task.PRIORITY_NORMAL) {
                private double time = 0;

                @Override
                public void run() {
                    time += Core.getDefaultTimer().getDeltaTime();
                    if (time > 0.5d) {
                        sc.setSprite("character_idle_left", 8);
                        Core.getDefaultTaskThread().remove(this.getID());
                    }
                }
            }, TaskThread.TYPE_CONTINUOUS);
        }
    }
}
