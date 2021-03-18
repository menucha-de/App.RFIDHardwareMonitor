package havis.app.rfidhardwaremonitor;

import havis.middleware.misc.TdtInitiator;
import havis.middleware.misc.TdtInitiator.SCHEME;
import havis.middleware.tdt.TdtTagInfo;
import havis.middleware.tdt.TdtTranslationException;
import havis.middleware.tdt.TdtTranslator;
import havis.util.monitor.Event;
import havis.util.monitor.Monitor;
import havis.util.monitor.ReaderEvent;
import havis.util.monitor.ReaderSource;
import havis.util.monitor.Source;
import havis.util.monitor.TagEvent;

import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ReaderMonitor implements Monitor {
	private final static Logger LOG = Logger.getLogger(ReaderMonitor.class.getName());

	private final static int MAX_QUEUE_SIZE = 50;
	private final static int MAX_STORE_TIME = 2000;

	private Map<EPCByteArray, Tag> tags = Collections
			.synchronizedMap(new HashMap<EPCByteArray, Tag>(MAX_QUEUE_SIZE + 1));

	private TdtTranslator tdtTranslator = new TdtTranslator();

	public ReaderMonitor() {
		for (SCHEME scheme : SCHEME.values()) {
			try {
				LOG.log(Level.INFO, "Loading TDT scheme " + scheme.name());
				tdtTranslator.getTdtDefinitions().add(TdtInitiator.get(scheme));
			} catch (IOException e) {
				LOG.log(Level.SEVERE, "Failed to load TDT scheme " + scheme, e);
			}
		}
	}

	/**
	 * Creates a list from the known tags that were seen less or 1000
	 * milliseconds ago. If the tag was seen earlier than that the tag will be
	 * removed.
	 * 
	 * @return A list of tags
	 */
	public List<Tag> getTagList() {
		List<Tag> tagList = new LinkedList<Tag>();
		for (Iterator<Map.Entry<EPCByteArray, Tag>> iterator = this.tags.entrySet().iterator(); iterator
				.hasNext();) {
			Map.Entry<EPCByteArray, Tag> entry = iterator.next();

			try {
				if (!isTimeExceeded(entry.getValue()))
					tagList.add(entry.getValue());
				else
					iterator.remove();
			} catch (IllegalArgumentException e) {
				LOG.log(Level.WARNING, "Error while checking if tag is exceeded", e);
			}
		}
		return tagList;
	}

	@Override
	public void notify(Source source, Event event) {
		if (source instanceof ReaderSource)
			notify((ReaderSource) source, event);
	}

	public void notify(ReaderSource source, Event event) {
		if (event instanceof ReaderEvent)
			notify(source, (ReaderEvent) event);
	}

	public void notify(ReaderSource source, ReaderEvent event) {
		if (event instanceof TagEvent)
			notify(source, (TagEvent) event);
	}

	/**
	 * This method is invoked when ever a tag was seen by the internal rf
	 * hardware. If the tag was seen before it's values will be updated
	 * otherwise the tag event will be wrapped in the tag class. The epc of the
	 * tag will be translated to a valid tag uri by the TDT translator. The new
	 * tag object will be added to a map which holds the untranslated epc as the
	 * key and the tag object as the value. At the end of each invokation the
	 * map will be cleaned from all tag that were seen more than 1000
	 * milliseconds ago.
	 * 
	 * @param source
	 *            The source of the event
	 * @param event
	 *            The tag data
	 */
	public void notify(ReaderSource source, TagEvent event) {
		if (event != null) {
			EPCByteArray epc = new EPCByteArray(event.getId());
			Tag tag = tags.get(epc);
			if (tag != null) {
				tag.setAntennaID(event.getAntenna());
				tag.setRssi(event.getRssi());
				tag.setTimestamp(event.getTimestamp());
			} else {
				try {
					TdtTagInfo tagInfo = tdtTranslator.translate(event.getId());
					tag = new Tag(tagInfo.getUriTag(), event.getAntenna(), event.getRssi(),
							event.getTimestamp());
				} catch (TdtTranslationException e) {
					LOG.log(Level.SEVERE, "Unable to translate epc", e);
				}

				if (tag != null)
					tags.put(epc, tag);

				if (tags.size() >= MAX_QUEUE_SIZE) {
					for (Iterator<Tag> iterator = tags.values().iterator(); iterator.hasNext();)
						try {
							if (isTimeExceeded(iterator.next()))
								iterator.remove();
						} catch (IllegalArgumentException e) {
							LOG.log(Level.WARNING, "Error while checking if tag is exceeded", e);
						}

				}
			}
		}
	}

	/**
	 * Checks whether a tag was seen more than 1000 milliseconds ago
	 * 
	 * @param tag
	 *            The tag which is to check
	 * @return true if the time is exeeded, false if not
	 * @throws IllegalArgumentException
	 *             if the tag is null or has no timestamp
	 */
	private boolean isTimeExceeded(Tag tag) {
		if (tag != null && tag.getTimestamp() != null)
			return System.currentTimeMillis() - tag.getTimestamp().getTime() > MAX_STORE_TIME;
		throw new IllegalArgumentException("Tag can not be null and the timestamp must be set");
	}
}
