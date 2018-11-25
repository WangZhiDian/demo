package com.demo.common.util.batch;

import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.Lists;
import com.google.common.collect.Queues;

public abstract class AsyncBatchExecutor<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger("excutor");
	private static final AtomicInteger THREAD_COUNTER = new AtomicInteger();

	public String getName() {
		return this.getClass().getSimpleName();
	}

	class Worker extends Thread {
		private final int batchSize;
		private volatile boolean running = true;
		final List<T> messages;

		private Worker(int batchSize) {
			this.batchSize = batchSize;
			this.messages = Lists.newArrayListWithCapacity(batchSize);
			setDaemon(true);
		}

		@Override
		public void run() {
			while (running) {
				try {
					// drain until we have a full batch or the duration is up
					Queues.drain(queue, messages, batchSize, queueDrainTime,
							TimeUnit.MILLISECONDS);

					if (queue.size() > batchSize) {
						LOGGER.warn("queue size ({}) > batch size for {}",
								queue.size(), this.getClass().getSimpleName());
					}

					if (!messages.isEmpty()) {
						// try again after one second until messages are
						// processed
						while (!handle(messages)) {
							Thread.sleep(retryInterval);
							if (queue.size() > batchSize) {
								LOGGER.warn(
										"queue size ({}) > batch size for {}",
										queue.size(), this.getClass()
												.getSimpleName());
							}
						}
						messages.clear();
					}
				} catch (InterruptedException ignored) {
					Thread.currentThread().interrupt();
				}
			}
		}

		private void shutdown() {
			flushQueue();
			this.running = false;
		}

		private void flushQueue() {
			if (!this.running)
				return;

			// let the queue drain
			while (!queue.isEmpty() || !messages.isEmpty()) {
				try {
					Thread.sleep(10);
				} catch (InterruptedException e) {
					Thread.currentThread().interrupt();
				}
			}
		}
	}

	private final BlockingQueue<T> queue;
	private final int queueSize;
	private final long queueDrainTime;
	private final long retryInterval;
	private volatile boolean append = true;

	final Worker workerThread;

	/**
	 * @param batchSize
	 *            当到达多少条后就进行批处�?
	 * @param queueSize
	 *            表示队列的最大长�?
	 * @param queueDrainTime
	 *            表示当到达多长时间后就进行批处理
	 * @param retryInterval
	 *            表示再次尝试时间
	 */
	public AsyncBatchExecutor(final int batchSize, final int queueSize,
			long queueDrainTime, long retryInterval) {
		this.queueSize = queueSize;
		this.queueDrainTime = queueDrainTime;
		this.queue = buildQueue();
		this.workerThread = new Worker(batchSize);
		this.retryInterval = retryInterval;
	}

	// public static <T> AsyncBatchExecutor<T> createAndStart(final int
	// batchSize, final int queueSize,
	// AsyncBatchHandler<T> delegate, long queueDrainTime, long retryInterval) {
	// AsyncBatchExecutor<T> asyncBatchExecutor = new
	// AsyncBatchExecutor<>(batchSize, queueSize, delegate,
	// queueDrainTime, retryInterval);
	// asyncBatchExecutor.start();
	// return asyncBatchExecutor;
	// }

	private ArrayBlockingQueue<T> buildQueue() {
		if (queueSize > 0) {
			return new ArrayBlockingQueue<T>(queueSize);
		}
		return new ArrayBlockingQueue<T>(10000);
	}

	public int getMaxQueueSize() {
		return queueSize;
	}

	public int getQueueSize() {
		return queue.size() + workerThread.messages.size();
	}

	public float getQueueFillFraction() {
		// unbounded
		if (queueSize == 0)
			return 0;

		return (float) getQueueSize() / (float) queueSize;
	}

	public void start() {
		System.out.println("Async start");
		workerThread.setName("asyncBatchWorkerThread-"
				+ THREAD_COUNTER.incrementAndGet());
		workerThread.setPriority(Thread.MIN_PRIORITY);
		workerThread.start();
		append = true;
	}

	public void stop() {
		append = false;
		workerThread.shutdown();
		done();
	}

	public void flush() {
		workerThread.flushQueue();
	}

	public void add(T event) {
		if (!append)
			return;
		try {
			queue.put(event);
		} catch (InterruptedException e) {
			LOGGER.error("put failed", e);
		}
	}

	public abstract boolean handle(List<T> batch);

	public abstract void done();
}