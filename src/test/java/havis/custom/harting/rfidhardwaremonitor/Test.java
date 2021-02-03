package havis.custom.harting.rfidhardwaremonitor;

import java.util.ArrayList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.LinkedBlockingQueue;

public class Test {

	public static void main(String[] args) throws InterruptedException {
		Queue<Tag> tags = new LinkedBlockingQueue<Tag>();

		for (int i = 0; i < 100; i++) {
			tags.add(new Tag());
			Thread.sleep(1);
		}

		long start = System.nanoTime();
		List<Tag> remove = new ArrayList<>();
		for (Tag t : tags) {
			if (System.currentTimeMillis() - t.getT() > 10)
				remove.add(t);
		}
		tags.removeAll(remove);
		System.out.println(System.nanoTime() - start);

		tags.clear();

		long sum = 0;
		for (int j = 0; j < 10; j++) {
			for (int i = 0; i < 10; i++) {
				tags.add(new Tag());
				Thread.sleep(1);
			}

			long start2 = System.nanoTime();
			remove.clear();
			for (Tag t : tags) {
				if (System.currentTimeMillis() - t.getT() > 10)
					remove.add(t);
			}
			tags.removeAll(remove);
			sum += System.nanoTime() - start2;

			tags.clear();
		}
		System.out.println(sum);

	}

	private static class Tag {
		private long timeStamp;

		public Tag() {
			timeStamp = System.currentTimeMillis();
		}

		public long getT() {
			return timeStamp;
		}
	}
}
