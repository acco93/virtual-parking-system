package isac.core.constructs;

public abstract class ActiveEntity extends Thread {

	public ActiveEntity() {

	}

	@Override
	public void run() {
		this.work();
	}

	protected abstract void work();

	
	
}
