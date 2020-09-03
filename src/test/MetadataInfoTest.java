package test;

import static org.junit.Assert.*;

//import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

//import org.junit.jupiter.api.AfterEach;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import core.CBRConnection;
import core.Coord;
import core.DTNHost;
import core.Message;
import core.MessageListener;
import core.ModuleCommunicationBus;
import core.NetworkInterface;
import core.SimClock;
import routing.ActiveRouter;
import routing.ContactHistory;
import routing.MessageRouter;
import routing.PassiveRouter;
import routing.Router10;
import routing.StorageMetrics;
import routing.MetadataInfo;

 public class MetadataInfoTest{

	// --> copied from ConnectionTest
	private MessageChecker mc;
	private TestUtils utils;
	protected static final int TRANSMIT_SPEED = 10;
	public static final double START_TIME = 10.0;
	private DTNHost h[];
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
	@Before
	public void setUp() throws Exception {
		SimClock.reset();
		clock.setTime(START_TIME);
		TestSettings testSettings = new TestSettings();
		testSettings.setNameSpace(TestUtils.IFACE_NS);
		testSettings.putSetting(NetworkInterface.TRANSMIT_RANGE_S, "1.0");
		testSettings.putSetting(NetworkInterface.TRANSMIT_SPEED_S, "1");
		
		List<MessageListener> ml = new ArrayList<MessageListener>();
        ml.add(mc);
        this.utils = new TestUtils(null,ml,testSettings);

		h = new DTNHost[nrof];
		c = new CBRConnection[nrof];
		m = new Message[nrof];

		for (int i=0; i< nrof; i++) {
			NetworkInterface ni = new TestInterface(testSettings);
			List<NetworkInterface> li = new ArrayList<NetworkInterface>();
			li.add(ni);

			ModuleCommunicationBus comBus = new ModuleCommunicationBus();
			h[i] = new DTNHost(ml,null,"h",li,comBus, new StationaryMovement(new Coord(0,0)), new Router10(testSettings));
			m[i] = new Message(h[0], h[i],""+i, size[i]);
		}

		con(h[0], h[1]);
		con(h[0], h[2]);
		con(h[1], h[3]);
		con(h[2], h[4]);
		con(h[3], h[4]);

//		c[0].startTransfer(h[0], m[0]);
//		c[1].startTransfer(h[0], m[1]);
//		c[2].startTransfer(h[1], m[2]);
		conCount = 3;
	}

	private void con(DTNHost from, DTNHost to) {
		c[index] = new CBRConnection(from, from.getInterfaces().get(0), to, to.getInterfaces().get(0), speed[index]);
		index++;
	}
	// <-- copied from ConnectionTest

	
	@After
	public void tearDown() throws Exception {
	}

	
	@Test
	public void testCreation() {
		MetadataInfo md = new MetadataInfo(h[1]);  //should it be h[1]? Yes
		assertTrue(h[1].toString() == md.get_id());
	}
	
	/*
	 * Verify that:
	 *  - when the connection comes up, the start time is properly set
	 *  -
	 */
	
    @Test
	public void testConnUp() {
		MetadataInfo md = new MetadataInfo(h[1]);
		assertEquals(md.getContact().get_last_start(), 0.0, 0.001);
		Router10 thisRouter =(Router10)h[0].getRouter();
		Router10 otherRouter =(Router10)h[1].getRouter();
		assertEquals(thisRouter.getContactMetrics().size(), 0);
		assertEquals(thisRouter.getStorageMetadata().size(), 0);
		// c[0] is a connection between h[0] and h[1], connection up should set the starting time to 10.0;
		md.connUp(c[0], h[0], SimClock.getTime());
		md.connUp(c[0], h[1], SimClock.getTime());
		//assertEquals(md.getContact().get_last_start(), 10.0); // 
		assertEquals(md.getContact().get_last_start(), 10.0, 0.001);
		assertEquals(thisRouter.getContactMetrics().size(), 1);
		assertEquals(otherRouter.getContactMetrics().size(), 1);
		assertEquals(thisRouter.getStorageMetadata().size(), 1);
		assertEquals(otherRouter.getStorageMetadata().size(), 1);
	}

	/*
	 * Verify:
	 * - The size of the contact list was 0 before the the first time the connection comes down
	 * - The size is one afterwards
	 * - The contact size has is properly calculated
	 */
	@Test
	public void testConnDown() {
	    MetadataInfo md = new MetadataInfo(h[1]);   
	    Router10 thisRouter = (Router10)h[0].getRouter();
	    assertEquals(thisRouter.getContactMetadata().size(), 0);
	    md.connUp(c[0], h[0], SimClock.getTime());
	    md.connDown(c[0], h[0], SimClock.getTime());
	    assertEquals(thisRouter.getContactMetadata().size(), 1);
	   
	}
	
	/*
	 * Verify:
	 * 	- The buffer size is properly set
	 *  
	 */
	@Test
	public void testBufferSize() {
	    MetadataInfo md = new MetadataInfo(h[1]);  
	    Router10 thisRouter = (Router10)h[0].getRouter();
	    Map<String, StorageMetrics> storageMetadata = thisRouter.getStorageMetadata();
	    md.connUp(c[0], h[0], SimClock.getTime());
	    StorageMetrics sm = storageMetadata.get(storageMetadata.keySet().toArray()[0]);
	    assertEquals(sm.getBufferSize(), h[1].getRouter().getBufferSize());
	}
	
	/*
	 * Verify:
	 *  - When two devices met, they exchange their buffer size
	 *  - When A meets B, and B meets C, C should learn the buffer size of A
	 */
	@Test
	public void testBufferSizeTransitive() {
	    MetadataInfo md = new MetadataInfo(h[2]); 
	    Router10 thisRouter = (Router10)h[0].getRouter();
	    Router10 otherRouter = (Router10)h[2].getRouter();
	    md.connUp(c[0], h[0], SimClock.getTime());
	    md.connUp(c[0], h[1], SimClock.getTime());
	    Map<String, StorageMetrics> storageMetadata1 = thisRouter.getStorageMetadata();
	    Map<String, StorageMetrics> storageMetadata2 = otherRouter.getStorageMetadata();
	    assertEquals(storageMetadata1.size(), 1);
	    assertEquals(storageMetadata2.size(), 0);
	    md.connUp(c[1], h[0], SimClock.getTime());
	    md.connUp(c[1], h[2], SimClock.getTime());
	    StorageMetrics sm1 = storageMetadata1.get(storageMetadata1.keySet().toArray()[0]);
	    StorageMetrics sm2 = storageMetadata2.get(storageMetadata2.keySet().toArray()[1]);
	    assertEquals(storageMetadata1.size(), 2);
	    assertEquals(storageMetadata2.size(), 2);
	    assertEquals(sm1.getBufferSize(), sm2.getBufferSize());
	}
	
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
	public void testContactTransitive() {
	    
	    MetadataInfo md = new MetadataInfo(h[2]); 
        Router10 thisRouter = (Router10)h[0].getRouter();
        Router10 otherRouter = (Router10)h[2].getRouter();
        Map<String, ContactHistory> contactMetadata1; 
        Map<String, ContactHistory> contactMetadata2; 
        md.connUp(c[0], h[0], SimClock.getTime());
        md.connDown(c[0], h[0], SimClock.getTime());
        contactMetadata1 = thisRouter.getContactMetadata();
        contactMetadata2 = otherRouter.getContactMetadata();
        assertEquals(contactMetadata1.size(), 1);
        assertEquals(contactMetadata2.size(), 0);
        md.connUp(c[1], h[0], SimClock.getTime());
        md.connUp(c[1], h[2], SimClock.getTime());
        md.connDown(c[1], h[0], SimClock.getTime());
        md.connDown(c[1], h[2], SimClock.getTime());
        contactMetadata1 = thisRouter.getContactMetadata();
        contactMetadata2 = otherRouter.getContactMetadata();
        assertEquals(contactMetadata1.size(), 2);
        assertEquals(contactMetadata2.size(), 2);
       
	}
	
	/*
	 * Test the size of the metadata, as it grows
	 */
	@Test
	public void testExchangeMetadataBundle() {
	   
	}
	
	/*
	 * Test the exchange of metadata before data
	 */
	@Test
	public void testMetadataExchange() {
	    
	}
	
	/*
	 * Test that the calculated size from data and metadata is properly set
	 */
	@Test
	public void testMetadataAndDataExchange() {
	    
	}
	
	
}
