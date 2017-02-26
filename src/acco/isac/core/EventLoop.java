package acco.isac.core;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

public abstract class EventLoop<T> extends Thread {

	private BlockingQueue<T> queue;

	public EventLoop() {
		this.queue = new LinkedBlockingQueue<>();
	}

	protected void append(T event) {
		try {
			this.queue.put(event);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	private T get() {
		T event = null;

		try {
			event = this.queue.take();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return event;
	}

	@Override
	public void run() {

		while (true) {

			T event = this.get();
			process(event);
		}

	}

	protected abstract void process(T event);

}
