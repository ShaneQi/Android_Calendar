import static org.junit.Assert.*;

import java.util.GregorianCalendar;

import org.junit.*;

public class EventTest {

	Event E1;
	Event E2;
	Event E3;
	Event E4;
	Event E5;
	Event E6;
	Category cat;
	
	@Before
	public void initial(){
		cat = new Category("test");
	}
	
	@Test
	public void testInConflict() {
		GregorianCalendar T1 = new GregorianCalendar(2015,12,6,8,0);
		GregorianCalendar T2 = new GregorianCalendar(2015,12,6,9,0);
		GregorianCalendar T3 = new GregorianCalendar(2015,12,6,10,0);
		GregorianCalendar T4 = new GregorianCalendar(2015,12,6,11,0);
		E1 = new Event("E1", T1, T2, cat);
		E2 = new Event("E2", T1, T2, cat);
		E3 = new Event("E3", T2, T4, cat);
		E4 = new Event("E4", T3, T4, cat);
		E5 = new Event("E5", T1, T3, cat);
		assertEquals(true, E1.inConflict(E2));
		assertEquals(false, E1.inConflict(E3));
		assertEquals(false, E1.inConflict(E4));
		assertEquals(true, E3.inConflict(E5));
	}

}
