package edu.catlin.springerj.g2e.core;


public abstract class AbstractSystem extends ManagedObject {
	int id;
	
	public abstract void initialize(AbstractEntity e);
	public abstract void update();
	
	void background(boolean started) { }
	public void destroy() {destroyed = true;} boolean destroyed = false;
}
