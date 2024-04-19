import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class NoteAndControlChangeTest {

    @Test
    void testConstructorAndToString() {
        Note note = new Note(1, 60, 100);
        assertEquals("[C, c:1, p:60, v:100]", note.toString());

        Note noteFull = new Note(1, 60, 100, 120, 1000L, "bus123");
        assertEquals("[C, c:1, p:60, v:100, t:120, ts:1000, b:bus123]", noteFull.toString());
    }

    @Test
    void testSettersAndGetters() {
        Note note = new Note(1, 60, 100);
        note.setChannel(2);
        assertEquals(2, note.channel());

        note.setPitch(61);
        assertEquals(61, note.pitch());
        assertEquals("C#", note.name());

        note.setVelocity(120);
        assertEquals(120, note.velocity());

        note.setTicks(150);
        assertEquals(150, note.ticks());
    }

    @Test
    void testEquals() {
        Note note1 = new Note(1, 60, 100, 120, 1000L, "bus123");
        Note note2 = new Note(1, 60, 100, 120, 1000L, "bus123");
        Note note3 = new Note(2, 61, 101, 121, 1001L, "bus124");

        assertEquals(note1, note2);
        assertNotEquals(note1, note3);
    }

//    @Test
//    void testOctaveAndRelativePitch() {
//        Note note = new Note(1, 24, 100); // Octave for middle C
//        assertEquals(2, note.octave());
//        assertEquals(0, note.relativePitch()); // C is the base note
//    }

    @Test
    void testConstructorWithTimestampAndBusName() {
        ControlChange cc = new ControlChange(1, 64, 127, 1000L, "MainBus");
        assertEquals(1, cc.channel());
        assertEquals(64, cc.number());
        assertEquals(127, cc.value());
        assertEquals(1000L, cc.timestamp);
        assertEquals("MainBus", cc.bus_name);
    }

    @Test
    void testConstructorWithoutTimestampAndBusName() {
        ControlChange cc = new ControlChange(1, 64, 127);
        assertEquals(1, cc.channel());
        assertEquals(64, cc.number());
        assertEquals(127, cc.value());
        assertEquals(-1, cc.timestamp);
        assertNull(cc.bus_name);
    }

    @Test
    void testSetters() {
        ControlChange cc = new ControlChange(1, 64, 127);
        cc.setChannel(2);
        assertEquals(2, cc.channel());

        cc.setNumber(65);
        assertEquals(65, cc.number());

        cc.setValue(120);
        assertEquals(120, cc.value());
    }

    @Test
    void testToControlChangeString() {
        ControlChange cc = new ControlChange(1, 64, 127);
        assertEquals("[c:1, n:64, v:127]", cc.toString());

        cc = new ControlChange(1, 64, 127, 1000L, "MainBus");
        assertEquals("[c:1, n:64, v:127, ts:1000, b:MainBus]", cc.toString());
    }

    @Test
    void testControlChnageEquals() {
        ControlChange cc1 = new ControlChange(1, 64, 127);
        ControlChange cc2 = new ControlChange(1, 64, 127);
        ControlChange cc3 = new ControlChange(1, 64, 127, 1000L, "MainBus");
        ControlChange cc4 = new ControlChange(2, 65, 128, 1000L, "MainBus");

        assertTrue(cc1.equals(cc2));
        assertFalse(cc1.equals(cc3));
        assertFalse(cc1.equals(cc4));
        assertFalse(cc3.equals(cc4));
    }

    @Test
    void testNotEqualsDifferentClass() {
        ControlChange cc = new ControlChange(1, 64, 127);
        Object obj = new Object();

        assertFalse(cc.equals(obj));
    }

    @Test
    void testEqualsNull() {
        ControlChange cc = new ControlChange(1, 64, 127);
        assertFalse(cc.equals(null));
    }
}
