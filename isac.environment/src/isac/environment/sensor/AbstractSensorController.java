package isac.environment.sensor;

import java.util.Random;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

import isac.core.constructs.ActiveEntity;
import isac.core.sharedknowledge.R;

/**
 * 
 * Abstract class that defines the structure of a SensorController
 * 
 * @author acco
 *
 */
public abstract class AbstractSensorController extends ActiveEntity {

	private Random random;

	private double serviceDisruptionProbability;

	private boolean working;

	private Condition condition;

	private ReentrantLock lock;

	public AbstractSensorController() {
		this.random = new Random();
		this.serviceDisruptionProbability = 0.001;
		this.working = true;

		lock = new ReentrantLock();
		this.condition = lock.newCondition();

	}

	@Override
	public void work() {

		while (working) {

			this.delay();

			Object value = this.sense();

			byte[] processedValue = this.process(value);

			this.act(processedValue);

			if (random.nextDouble() <= this.serviceDisruptionProbability) {
				this.disable();
				this.working = false;
			}

		}

	}

	protected abstract void disable();

	protected abstract void act(byte[] bytes);

	protected abstract byte[] process(Object value);

	protected abstract Object sense();

	private void delay() {

		int randomDelay = random.nextInt(R.MAX_SENSOR_DELAY);

		lock.lock();

		try {
			condition.await(randomDelay, TimeUnit.MILLISECONDS);
		} catch (InterruptedException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}

		lock.unlock();

	}

	/**
	 * Wake the sensor.
	 */
	public void wake() {
		lock.lock();
		condition.signal();
		lock.unlock();
	}

}
