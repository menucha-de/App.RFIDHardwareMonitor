package havis.custom.harting.rfidhardwaremonitor;

import havis.middleware.tdt.TdtTranslationException;
import havis.middleware.tdt.TdtTranslator;
import havis.util.monitor.Capabilities;
import havis.util.monitor.CapabilityType;
import havis.util.monitor.Configuration;
import havis.util.monitor.ConfigurationType;
import havis.util.monitor.Event;
import havis.util.monitor.ReaderSource;
import havis.util.monitor.Source;
import havis.util.monitor.TagEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import mockit.Deencapsulation;
import mockit.Mocked;
import mockit.NonStrictExpectations;

import org.junit.Assert;
import org.junit.Test;

public class ReaderMonitorTest {

	@Test
	public void notifyNewTag(final @Mocked TdtTranslator translator) {

		try {
			new NonStrictExpectations() {
				{
					translator.translate((byte[]) any);
					result = "EpcAsTagUri";
				}
			};
		} catch (TdtTranslationException e) {
			Assert.fail("No exception expected");
		}

		ReaderMonitor monitor = new ReaderMonitor();

		final byte[] sampleEPC = new byte[] { 1, 2, 3, 4 };
		final Date timestamp = new Date();
		Event event = new TagEvent(timestamp, sampleEPC, 1, -40);

		monitor.notify(createSource(), event);

		HashMap<byte[], Tag> tags = Deencapsulation.getField(monitor, "tags");

		Assert.assertNotNull(tags);
		Assert.assertFalse(tags.isEmpty());
		Assert.assertTrue(tags.keySet().contains(sampleEPC));

		Tag tag = tags.get(sampleEPC);

		Assert.assertEquals("EpcAsTagUri", tag.getEpc());
		Assert.assertEquals(timestamp, tag.getTimestamp());
		Assert.assertEquals(1, tag.getAntennaID());
		Assert.assertEquals(-40, tag.getRssi());
	}

	@Test
	public void notifyUpdateTag() {
		ReaderMonitor monitor = new ReaderMonitor();

		HashMap<byte[], Tag> tags = new HashMap<byte[], Tag>();
		final byte[] inputEPC = new byte[] { 1, 2, 3, 4 };
		final Tag inputTag = new Tag("EpcAsTagUri", 1, -40, new Date());
		tags.put(inputEPC, inputTag);

		Deencapsulation.setField(monitor, "tags", tags);

		final Date newTimestamp = new Date();
		Event event = new TagEvent(newTimestamp, inputEPC, 2, -50);

		monitor.notify(createSource(), event);

		tags = Deencapsulation.getField(monitor, "tags");

		Assert.assertNotNull(tags);
		Assert.assertFalse(tags.isEmpty());
		Assert.assertTrue(tags.keySet().contains(inputEPC));

		Tag tag = tags.get(inputEPC);

		Assert.assertEquals("EpcAsTagUri", tag.getEpc());
		Assert.assertEquals(newTimestamp, tag.getTimestamp());
		Assert.assertEquals(2, tag.getAntennaID());
		Assert.assertEquals(-50, tag.getRssi());
	}

	@Test
	public void notifyRemoveOldTags() throws InterruptedException {
		ReaderMonitor monitor = new ReaderMonitor();
		HashMap<byte[], Tag> tags = new HashMap<byte[], Tag>();

		for (int i = 0; i < 30; i++) {
			byte[] inputEPC = new byte[] { 1, 2, 3, (byte) i };
			Tag inputTag = new Tag("EpcAsTagUri" + i, 1, -40, new Date());
			tags.put(inputEPC, inputTag);
		}

		Deencapsulation.setField(monitor, "tags", tags);

		Thread.sleep(1050);

		final Date newTimestamp = new Date();
		final byte[] inputEPC = new byte[] { 1, 2, 3, 100 };
		Event event = new TagEvent(newTimestamp, inputEPC, 2, -50);

		monitor.notify(createSource(), event);

		tags = Deencapsulation.getField(monitor, "tags");

		Assert.assertNotNull(tags);
		Assert.assertFalse(tags.isEmpty());
		Assert.assertTrue(tags.keySet().contains(inputEPC));
		Assert.assertTrue(tags.size() == 1);
	}

	@Test
	public void getTagList() throws TdtTranslationException {
		ReaderMonitor monitor = new ReaderMonitor();
		HashMap<byte[], Tag> tags = new HashMap<byte[], Tag>();

		for (int i = 0; i < 2; i++) {
			byte[] inputEPC = new byte[] { 1, 2, 3, (byte) i };
			Tag inputTag = new Tag("EpcAsTagUri" + i, 1, -40, new Date());
			tags.put(inputEPC, inputTag);
		}

		Deencapsulation.setField(monitor, "tags", tags);

		List<Tag> result = monitor.getTagList();

		Assert.assertTrue(result != null);
		Assert.assertTrue(result.size() == 2);

		for (int i = 0; i < result.size(); i++) {
			Tag t = result.get(i);
			Assert.assertEquals("EpcAsTagUri" + i, t.getEpc());
			Assert.assertEquals(1, t.getAntennaID());
			Assert.assertEquals(-40, t.getRssi());
		}
	}

	@Test
	public void getTagListRemoveOldTags() throws InterruptedException {
		ReaderMonitor monitor = new ReaderMonitor();
		HashMap<byte[], Tag> tags = new HashMap<byte[], Tag>();

		for (int i = 0; i < 2; i++) {
			byte[] inputEPC = new byte[] { 1, 2, 3, (byte) i };
			Tag inputTag = new Tag("EpcAsTagUri" + i, 1, -40, new Date());
			tags.put(inputEPC, inputTag);
		}

		Deencapsulation.setField(monitor, "tags", tags);

		Thread.sleep(1050);

		List<Tag> result = monitor.getTagList();

		Assert.assertTrue(result != null);
		Assert.assertTrue(result.size() == 0);
	}

	private Source createSource() {
		return new ReaderSource() {

			@Override
			public void setConfiguration(List<Configuration> configuration) {
			}

			@Override
			public List<Configuration> getConfiguration(ConfigurationType type, short antennaId) {
				return null;
			}

			@Override
			public List<Capabilities> getCapabilities(CapabilityType type) {
				return null;
			}
		};
	}
}
