package test;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import core.CBRConnection;
import core.Coord;
import core.DTNHost;
import core.Message;
import core.MessageListener;
import core.ModuleCommunicationBus;
import core.MovementListener;
import core.NetworkInterface;
import core.Settings;
import core.SimClock;
import movement.MovementModel;
import movement.Path;
import routing.MessageRouter;
import routing.PassiveRouter;
import routing.util.Metadata;
import routing.util.MetadataInfo;

class MetadataInfoTest {

	// --> copied from ConnectionTest
	private MessageChecker mc;
	private TestUtils utils;
	protected static final int TRANSMIT_SPEED = 10;
	public static final double START_TIME = 10.0;
	private TestDTNHost h[];
	private CBRConnection c[];
	private Message m[];
	private int speed[] = {50, 50, 100, 200, 100};
	private int size[] = {50, 75, 100, 200, 1000};
	private int nrof = 5;
	private int index;
	private SimClock clock = SimClock.getInstance();
	private int conCount;
	// <-- copied from ConnectionTest


	public MetadataInfoTest() {
		// TODO Auto-generated constructor stub
	}

	// --> copied from ConnectionTest
	@BeforeEach
	void setUp() throws Exception {
		SimClock.reset();
		clock.setTime(START_TIME);
		TestSettings testSettings = new TestSettings();
		testSettings.setNameSpace(TestUtils.IFACE_NS);
		testSettings.putSetting(NetworkInterface.TRANSMIT_RANGE_S, "1.0");
		testSettings.putSetting(NetworkInterface.TRANSMIT_SPEED_S, "1");

		h = new TestDTNHost[nrof];
		c = new CBRConnection[nrof];
		m = new Message[nrof];

		for (int i=0; i< nrof; i++) {
			NetworkInterface ni = new TestInterface(testSettings);
			List<NetworkInterface> li = new ArrayList<NetworkInterface>();
			li.add(ni);

			ModuleCommunicationBus comBus = new ModuleCommunicationBus();
			h[i] = new TestDTNHost(li,comBus, testSettings);
			m[i] = new Message(h[0], h[i],""+i, size[i]);
		}

		con(h[0], h[1]);
		con(h[0], h[2]);
		con(h[1], h[3]);
		con(h[2], h[4]);
		con(h[3], h[4]);

		c[0].startTransfer(h[0], m[0]);
		c[1].startTransfer(h[0], m[1]);
		c[2].startTransfer(h[1], m[2]);
		conCount = 3;
	}

	private void con(DTNHost from, DTNHost to) {
		c[index] = new CBRConnection(from, from.getInterfaces().get(0), to, to.getInterfaces().get(0), speed[index]);
		index++;
	}
	// <-- copied from ConnectionTest

	
	@AfterEach
	void tearDown() throws Exception {
	}

	
	@Test
	void testCreation() {
		MetadataInfo md = new MetadataInfo(h[0]);
		assertTrue(h[0].getAddress() == md.get_id());
	}
	
	/*
	 * Verify that:
	 *  - when the connection comes up, the start time is properly set
	 *  -
	 */
	@Test
	void testConnUp() {
		MetadataInfo md = new MetadataInfo(h[0]);
		assertEquals(md.contact.get_last_start(), 0.0);
		// c[0] is a connection between h[0] and h[1], connection up should set the starting time to 10.0;
		md.connUp(c[0], SimClock.getTime());
		assertEquals(md.contact.get_last_start(), 10.0);
	}

	/*
	 * Verify:
	 * - The size of the contact list was 0 before the the first time the connection comes down
	 * - The size is one afterwards
	 * - The contact size has is properly calculated
	 */
	@Test
	void testConnDown() {}
	
	/*
	 * Verify:
	 * 	- The buffer size is properly set
	 *  
	 */
	@Test
	void testBufferSize() {}
	
	/*
	 * Verify:
	 *  - When two devices met, they exchange their buffer size
	 *  - When A meets B, and B meets C, C should learn the buffer size of A
	 */
	@Test
	void testBufferSizeTransitive() {}
	
	/*
	 * Verify:
	 * - When A meets B and B meets C, c should learn about the contacts from A
	 * - Test with:
	 * 	* one contact between A and B, and then B and C
	 * 	* (2) two contacts between A and B, and then B and C
	 * 	* one contact A <-> B, followed by B <-> C, A <-> B, B <-> C; verify that the result is the same as (2)
	 * 
	 */	
	@Test
	void testContactTransitive() {}
	
	/*
	 * Test the size of the metadata, as it grows
	 */
	@Test
	void testExchangeMetadataBundle() {}
	
	/*
	 * Test the exchange of metadata before data
	 */
	@Test
	void testMetadataExchange() {}
	
	/*
	 * Test that the calculated size from data and metadata is properly set
	 */
	@Test
	void testMetadataAndDataExchange() {}
	
	
}
