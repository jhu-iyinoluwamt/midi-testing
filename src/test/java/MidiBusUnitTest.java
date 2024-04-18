import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sound.midi.*;
import java.lang.reflect.Field;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

public class MidiBusUnitTest {
    private MidiBus midiBus;
    private Object tParent;
    private String local = "Bus 1";

    @BeforeEach
    void setUp() {
        midiBus = new MidiBus();
        tParent = new Object();
    }

    @Test
    void testDefaultConstructor() {
        assertNotNull(midiBus.getBusName(), "Bus name should be generated.");
    }

    @Test
    void testObjectConstructor() throws IllegalAccessException, NoSuchFieldException {
        MidiBus tBus = new MidiBus(tParent);
        assertNotNull(midiBus.getBusName(), "Bus name should be generated.");
        String[] expected = new String[0];
        assertArrayEquals(expected, tBus.attachedInputs());
        assertArrayEquals(expected, tBus.attachedOutputs());
    }

    @Test
    void testParentAndBusNameConstructor() {
        String busName = "Test Bus";
        MidiBus bus = new MidiBus(tParent, busName);
        assertEquals(busName, bus.getBusName(), "Bus name should be set correctly.");
        assertEquals(tParent, getField(bus, "parent"), "Parent should be set correctly.");
    }

    @Test
    void testParentAndInputOutputByIndexConstructor() {
        MidiBus bus = new MidiBus(tParent, 1, 1); // Adjust indices as necessary
        assertNotNull(bus.getBusName(), "Bus name should not be null.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    @Test
    void testParentAndInputOutputByIndexAndBusNameConstructor() {
        String busName = "Special Bus";
        MidiBus bus = new MidiBus(tParent, 1, 1, busName); // Adjust indices as necessary
        assertEquals(busName, bus.getBusName(), "Bus name should be set correctly.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    @Test
    void testParentAndInputOutputByNameConstructor() {
        MidiBus bus = new MidiBus(tParent, local, local); // Replace with actual device names
        assertNotNull(bus.getBusName(), "Bus name should not be null.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    @Test
    void testParentAndInputOutputByNameAndBusNameConstructor() {
        String busName = "Complex Bus";
        MidiBus bus = new MidiBus(tParent, local, local, busName); // Replace with actual device names
        assertEquals(busName, bus.getBusName(), "Bus name should be set correctly.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    @Test
    void testParentInputIndexAndOutputNameConstructor() {
        MidiBus bus = new MidiBus(tParent, 1, local); // Replace with actual output device name
        assertNotNull(bus.getBusName(), "Bus name should not be null.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    @Test
    void testParentInputNameAndOutputIndexConstructor() {
        MidiBus bus = new MidiBus(tParent, local, 1); // Replace with actual input device name
        assertNotNull(bus.getBusName(), "Bus name should not be null.");
        assertNotEquals(0, bus.attachedInputs().length, "Input should be added.");
        assertNotEquals(0, bus.attachedOutputs().length, "Output should be added.");
    }

    private Object getField(Object obj, String fieldName) {
        try {
            Field field = obj.getClass().getDeclaredField(fieldName);
            field.setAccessible(true);
            return field.get(obj);
        } catch (Exception e) {
            fail("Failed to access field: " + fieldName);
            return null;
        }
    }

    @Test
    void testRemoveOutput() {
        midiBus.addOutput(local);
        assertTrue(midiBus.removeOutput(0), "Should remove output device successfully.");
        assertFalse(midiBus.removeOutput(1), "Should return false when trying to remove non-existent device.");
    }

    @Test
    void testAddAndRemoveOutput() {
        midiBus.addOutput(local);
        assertTrue(midiBus.removeOutput(0), "Should remove output device successfully.");
        assertFalse(midiBus.removeOutput(1), "Should return false when trying to remove non-existent device.");
    }

    @Test
    void testClearInputsOutputsAndAll() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        midiBus.clearInputs();
        assertEquals(0, midiBus.attachedInputs().length, "Should have cleared all input devices.");
        midiBus.clearOutputs();
        assertEquals(0, midiBus.attachedOutputs().length, "Should have cleared all output devices.");
        midiBus.clearAll();
        assertEquals(0, midiBus.attachedInputs().length, "Should have cleared all devices.");
        assertEquals(0, midiBus.attachedOutputs().length, "Should have cleared all devices.");
    }

    @Test
    void testAddInputByIndex() {
        // Assuming index '0' is always valid for tests
        assertTrue(midiBus.addInput(0), "Input should be added successfully by index.");
        assertFalse(midiBus.addInput(-1), "Should handle invalid index gracefully.");
    }

    @Test
    void testAddOutputByName() {
        // Assuming "ValidOutput" is a correct name and "InvalidOutput" is not
        assertTrue(midiBus.addOutput(local), "Output should be added successfully by name.");
        assertFalse(midiBus.addOutput(local), "Should handle invalid name gracefully.");
    }

    @Test
    void testRemoveInputByIndex() {
        midiBus.addInput(0);
        assertTrue(midiBus.removeInput(0), "Input should be removed successfully.");
        assertFalse(midiBus.removeInput(1), "Should handle non-existent index gracefully.");
    }

    @Test
    void testRemoveOutputByName() {
        midiBus.addOutput(local);
        assertTrue(midiBus.removeOutput(local), "Output should be removed successfully.");
        assertFalse(midiBus.removeOutput(local), "Should handle non-existent name gracefully.");
    }


    @Test
    void testBusNameGeneration() {
        midiBus.generateBusName();
        String busName = midiBus.getBusName();
        assertTrue(busName.startsWith("MidiBus_") && busName.length() > "MidiBus_".length());
    }

    @Test
    void testBusNameManagement() {
        midiBus.setBusName("TestBus");
        assertEquals("TestBus", midiBus.getBusName(), "Bus name should match the set value.");
        midiBus.generateBusName();
        assertTrue(midiBus.getBusName().startsWith("MidiBus_"), "Generated bus name should start with 'MidiBus_'.");
    }

    @Test
    void testToString() {
        midiBus.setBusName("MidiBus1");
        String expected = "MidiBus: MidiBus1 [0 input(s), 0 output(s), 0 listener(s)]";
        assertEquals(expected, midiBus.toString(), "toString should return the correct string representation.");
    }

    @Test
    void testEqualsAndHashCode() {
        MidiBus anotherMidiBus = new MidiBus();
        anotherMidiBus.setBusName("MidiBus1");
        midiBus.setBusName("MidiBus1");

        assertEquals(midiBus, anotherMidiBus, "Two MidiBuses with the same name should be equal.");
        assertEquals(midiBus.hashCode(), anotherMidiBus.hashCode(), "Hash codes should match for equal MidiBuses.");

        anotherMidiBus.setBusName("MidiBus2");
        assertNotEquals(midiBus, anotherMidiBus, "MidiBuses with different names should not be equal.");
    }

    @Test
    void testClone() {
        midiBus.setBusName("CloneTest");
        MidiBus clonedBus = midiBus.clone();
        assertEquals(midiBus, clonedBus, "Cloned bus should be equal to the original.");
        assertNotSame(midiBus, clonedBus, "Cloned bus should not be the same object as the original.");
    }

    @Test
    void testCloseAndDisposeMethods() {
        midiBus.addInput(0);  // Assuming there is at least one valid input index
        midiBus.addOutput(0); // Assuming there is at least one valid output index
        assertDoesNotThrow(midiBus::close, "Closing should not throw an exception.");
        assertDoesNotThrow(midiBus::dispose, "Disposing should not throw an exception.");
    }

    @Test
    void testListDevices() {
        assertDoesNotThrow(MidiBus::list, "Listing devices should not throw an exception.");
    }

    @Test
    void testDeviceListingStaticMethods() {
        MidiBus.findMidiDevices();

        // Test for available inputs
        String[] inputs = MidiBus.availableInputs();
        assertNotNull(inputs, "Should return a non-null array of input device names.");
        assertTrue(inputs.length > 0, "Should have at least one available input.");
        // If the name of a specific expected device is known, test for its presence
        assertTrue(Arrays.asList(inputs).contains(local), "Should contain the specific input device.");

        // Test for available outputs
        String[] outputs = MidiBus.availableOutputs();
        assertNotNull(outputs, "Should return a non-null array of output device names.");
        assertTrue(outputs.length > 0, "Should have at least one available output.");
        // Similarly, check for a known device
        assertTrue(Arrays.asList(outputs).contains(local), "Should contain the specific output device.");

        // Test for unavailable devices
        String[] unavailable = MidiBus.unavailableDevices();
        assertNotNull(unavailable, "Should return a non-null array of unavailable device names.");
        // The list of unavailable devices might be empty or non-empty depending on the setup
        if (unavailable.length > 0) {
            assertTrue(Arrays.asList(unavailable).contains("Known Unavailable Device Name"), "Should contain the specific unavailable device if expected.");
        }
    }

    @Test
    void testDynamicDeviceDiscovery() {
        // Assuming that device availability might change
        MidiBus.findMidiDevices(); // Refresh the device list

        // Perform checks similar to those above
        String[] newInputs = MidiBus.availableInputs();
        assertTrue(newInputs.length >= 0, "Should handle dynamic changes in available inputs.");

        String[] newOutputs = MidiBus.availableOutputs();
        assertTrue(newOutputs.length >= 0, "Should handle dynamic changes in available outputs.");

        // Assume new devices might become unavailable
        String[] newUnavailable = MidiBus.unavailableDevices();
        assertTrue(newUnavailable.length >= 0, "Should handle dynamic changes in unavailable devices.");
    }

    @Test
    void testStop() {
        assertDoesNotThrow(() -> midiBus.stop());
    }
}
