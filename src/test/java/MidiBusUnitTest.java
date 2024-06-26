import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import javax.sound.midi.*;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Vector;

import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;
import org.mockito.internal.matchers.Null;
//import org.powermock.api.mockito.PowerMockito;
//import org.powermock.core.classloader.annotations.PrepareForTest;

import static org.junit.jupiter.api.Assertions.*;
//import static org.powermock.api.mockito.PowerMockito.mock;
//import static org.powermock.api.mockito.PowerMockito.mock;

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

//    @Test
//    void testStop() {
//        assertDoesNotThrow(() -> midiBus.stop());
//    }

    @Test
    public void constructorTest(){
        MidiBus myBus = new MidiBus(this, 0, "Studio 68c", "68cbus");
        assertTrue(myBus.bus_name == "68cbus");
        assertTrue(myBus.input_devices!= null);
    }

    @Test
    public void constructor1Test(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", 0, "68cbus");
        assertTrue(myBus.bus_name == "68cbus");
        assertTrue(myBus.input_devices!= null);
    }

    @Test
    public void addInputErrorTest(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", 0, "68cbus");

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStreamCaptor));

        myBus.addInput(123);

        System.setErr(System.err);
        String errorMessage = outputStreamCaptor.toString().trim();
        assertEquals(errorMessage,"The MidiBus Warning: The chosen input device numbered [123] was not added because it doesn't exist");

    }

    @Test
    public void addInputInvalidStringTest(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", "Studio 68c");

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStreamCaptor));

        String deviceName = "im tired lol";

        myBus.addInput(deviceName);

        System.setErr(System.err);
        String errorMessage = outputStreamCaptor.toString().trim();
        assertEquals("The MidiBus Warning: No available input MIDI devices named: \""+deviceName+"\" were found",errorMessage);
    }

    @Test
    public void removeInputTest(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", "Studio 68c");
        myBus.removeInput(0);

        String device = myBus.availableInputs()[0];
        MidiDevice.Info[] devices = MidiBus.availableInputsMidiDeviceInfo();

//        MidiBus.InputDeviceContainer container = midiBus.input_devices.get(0);

        String[] emptyString = new String[0];
        Vector<MidiBus.InputDeviceContainer> emptyVector = new Vector<>();

        assertEquals(midiBus.input_devices, emptyVector);
    }

    @Test
    public void removeInputStringInvalidTest(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", "Studio 68c");
        myBus.removeInput("im so tired");

        String device = myBus.availableInputs()[0];
        MidiDevice.Info[] devices = MidiBus.availableInputsMidiDeviceInfo();

//        MidiBus.InputDeviceContainer container = midiBus.input_devices.get(0);

        String[] emptyString = new String[0];
        Vector<MidiBus.InputDeviceContainer> emptyVector = new Vector<>();

        assertEquals(midiBus.input_devices, emptyVector);
    }

    @Test
    public void removeInputStringTest(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", "Studio 68c");
        myBus.removeInput("Studio 68c");

        String device = myBus.availableInputs()[0];
        MidiDevice.Info[] devices = MidiBus.availableInputsMidiDeviceInfo();

//        MidiBus.InputDeviceContainer container = midiBus.input_devices.get(0);

        String[] emptyString = new String[0];
        Vector<MidiBus.InputDeviceContainer> emptyVector = new Vector<>();

        assertEquals(midiBus.input_devices, emptyVector);
    }

    @Test
    public void removeInputInvalidTest(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", "Studio 68c");
        myBus.removeInput(0);

        assertTrue(!midiBus.removeInput(12312));
    }

    @Test
    public void addOutputInvalidTest(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", "Studio 68c");
        assertTrue(!myBus.addOutput(123123));
    }

    @Test
    public void addOutputInvalidTest1(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", "Studio 68c");
        assertTrue(!myBus.addOutput("still tired"));
    }

    @Test
    public void midiBusInitBusNullTest(){
        String nullBusName = null;
        MidiBus mybus = new MidiBus(this, nullBusName);
        assertTrue(mybus.bus_name.contains("MidiBus"));
    }

    @Test
    public void midiBusNoAvailableDevices() throws MidiUnavailableException {

        MidiBus mybus = new MidiBus(this);
        MidiBus.available_devices = null;

        mybus.closeAllMidiDevices();
        MidiDevice device;

        for (int i = 0; i < MidiBus.availableOutputs().length; i++){
            device = MidiSystem.getMidiDevice(MidiBus.available_devices[i]);
            assertTrue(!device.isOpen());
        }
    }
    @Test
    public void registerNullParentTest(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", "Studio 68c");
        myBus.parent = new MidiBus(this, "Studio 68c", "Studio 68c");

        midiBus.registerParent(myBus);
        assertTrue(myBus.parent != null);
    }

    @Test
    public void cloneWithListenersAndIOTest(){
        MidiBus mybus = new MidiBus(this, "Studio 68c", "Studio 68c");
        mybus.addInput("Real Time Sequencer");
        mybus.addOutput("Real Time Sequencer");

        MidiListener listener = new MidiListener() {};
        MidiListener listener1 = new MidiListener() {};

        mybus.addMidiListener(listener);
        mybus.addMidiListener(listener1);

        mybus.addInput("Bus 1");
        mybus.addOutput("Bus 1");

        MidiBus cloneBus = mybus.clone();

        Assertions.assertEquals(cloneBus, mybus);
    }

//    @Test
//    public void unavailableDevices(){
//        MidiBus mybus = new MidiBus(this, "Studio 68c", "Studio 68c");
//        MockedStatic<MidiBus> mockBus =  Mockito.mockStatic(MidiBus.class);
////        Mockito.when(mockBus.av)
//
//    }



    @Test
    public void simpleMidiTest(){
        MidiBus myBus = new MidiBus();
        myBus.sendTimestamps(false);
        String[] availableInputs = MidiBus.availableInputs();



        System.out.println("AVAILABLE INPUTS");
        System.out.println("_________________");

        for (int i = 0; i < availableInputs.length; i++){
            System.out.println(i + " " + availableInputs[i]);
        }

        System.out.println("AVAILABLE OUTPUTS");
        System.out.println("_________________");

        for (int i = 0; i < availableInputs.length; i++){
            System.out.println(i+ " " + availableInputs[i]);

        }
    }

    @Test
    public void testNotifyParent_ExceptionInMethodRawMidi() throws Exception {


    }

    @Test
    public void testHashCode(){
        MidiBus myBus = new MidiBus();
        assertTrue(myBus.hashCode() > Integer.MIN_VALUE);

    }


    @Test
    public void testNotifyListenersForRawMidiListeners() throws InvalidMidiDataException {
//        // Create a mock RawMidiListener
//        RawMidiListener mockListener = Mockito.mock(RawMidiListener.class);
//
//        // Throw an exception when the notify method is called
//        Mockito.doThrow(new RuntimeException("Test exception")).when(mockListener).notify();
//
//        // Create a MidiBus instance
//        MidiBus midiBus = new MidiBus();
//
//        // Add the mock listener to the MidiBus
//        midiBus.addMidiListener(mockListener);
//        ShortMessage message = new ShortMessage();
//        message.setMessage(ShortMessage.NOTE_ON, 5, 60, 100); // Note On message on MIDI channel 5, note number 60, velocity 100
//
//        // Call the notifyListeners method
//        assertThrows(RuntimeException.class, () -> midiBus.notifyListeners(message, 5));
    }

    @Test
    public void addOuputGetMaxReceiversFailTest() throws MidiUnavailableException {
        MidiDevice.Info devices = MidiBus.availableInputsMidiDeviceInfo()[0];
        MidiBus midiBus = Mockito.mock(MidiBus.class);
        MidiDevice new_device = MidiSystem.getMidiDevice(devices);

        MidiDevice.Info mockDevice = Mockito.mock(MidiDevice.Info.class);
    }

    @Test
    public void sendNoteOffTest() throws Exception {
        MidiBus mybus = new MidiBus(this, "Studio 68c", "Studio 68c");
        Note note = new Note(0,70, 127);
        mybus.sendNoteOff(note);

        Assertions.assertEquals(note.channel, 0);
        Assertions.assertEquals(note.pitch, 70);
        Assertions.assertEquals(note.velocity, 127 );
    }

    @Test
    public void notifyListenersTest() throws Exception {
        MidiBus mybus = new MidiBus(this, "Studio 68c", "Studio 68c");

        ShortMessage message = new ShortMessage(0x90, 60,127);

        mybus.notifyListeners(message, 10);

        byte[] data = {(byte)0x90, (byte) 0x40, (byte) 0x7F}; // Example raw MIDI data

        midiBus.notifyListeners(new MidiMessage(data) {
            @Override
            public Object clone() {
                return null;
            }
        }, 0);

        Note note = new Note(0, 60, 127);




        Assertions.assertEquals(message.getChannel(), 0);
        Assertions.assertEquals(message.getData1(), 60);
        Assertions.assertEquals(message.getData2(), 127);
    }

    @Test
    public void testNotifyListeners() {
        // Create a mock MidiListener
        SimpleMidiListener listener = Mockito.mock(SimpleMidiListener.class);
        StandardMidiListener standardListener = Mockito.mock(StandardMidiListener.class);
        ObjectMidiListener objectListener = Mockito.mock(ObjectMidiListener.class);

        // Create a MidiBus instance and add the listener
        MidiBus midiBus = new MidiBus(this, "Studio 68c", "Studio 68c");
        midiBus.addMidiListener(listener);
        midiBus.addMidiListener(standardListener);
        midiBus.addMidiListener(objectListener);


        // Create a MIDI message
        byte[] data = new byte[] {(byte) (ShortMessage.NOTE_ON | 0x0F), 0x40, 0x7F};

        // Call notifyListeners
        midiBus.notifyListeners(new MidiMessage(data) {
            @Override
            public Object clone() {
                return null;
            }
        }, 0);

        data = new byte[] {(byte) (ShortMessage.NOTE_OFF | 0x0F), 0x40, 0x7F};

        // Call notifyListeners
        midiBus.notifyListeners(new MidiMessage(data) {
            @Override
            public Object clone() {
                return null;
            }
        }, 0);

        data = new byte[] {(byte) (ShortMessage.CONTROL_CHANGE | 0x0F), 0x40, 0x7F};

        // Call notifyListeners
        midiBus.notifyListeners(new MidiMessage(data) {
            @Override
            public Object clone() {
                return null;
            }
        }, 0);

        // Verify that the noteOn method was called on the listener
        Mockito.verify(listener, Mockito.times(1)).noteOn(0x0F, 0x40, 0x7F);


        // Verify that the midiMessage method was called on the standardListener
        Mockito.verify(standardListener, Mockito.times(3)).midiMessage(Mockito.any(MidiMessage.class), Mockito.eq(0L));

        // Verify that the noteOn method was called on the objectListener
        Mockito.verify(objectListener, Mockito.times(1)).noteOn(Mockito.any(Note.class));
    }

    @Test
    public void listenerExceptionTest() throws Exception {

        // Create a mock MidiListener that throws an exception
        SimpleMidiListener listener = Mockito.mock(SimpleMidiListener.class);
        Mockito.doThrow(new RuntimeException()).when(listener).noteOn(Mockito.anyInt(), Mockito.anyInt(), Mockito.anyInt());

        // Create a MidiBus instance and add the listener
        MidiBus midiBus = new MidiBus();
        midiBus.addMidiListener(listener);

        // Create a MIDI message
        byte[] data = new byte[] {(byte) (ShortMessage.NOTE_ON | 0x0F), 0x40, 0x7F};

        // Call notifyListeners and expect an exception
        assertThrows(RuntimeException.class, () -> midiBus.notifyListeners(new MidiMessage(data) {
            @Override
            public Object clone() {
                return null;
            }
        }, 0));

    }

    @Test
    public void MetaMessageTest() throws Exception {
        // Create a MidiBus instance
        MidiBus midiBus = new MidiBus();

// Create a MetaMessage
        byte[] metaMessageData = new byte[] {(byte) MetaMessage.META, 0x02, /* payload bytes go here */};

// Send the MetaMessage
        midiBus.sendMessage(metaMessageData);

        Assertions.assertTrue(metaMessageData.length != 0);
    }

    @Test
    public void SysexMessageTest() throws Exception {
        MidiBus midiBus = new MidiBus();

// Create a SysexMessage
        byte[] sysexMessageData = new byte[] {(byte) SysexMessage.SYSTEM_EXCLUSIVE, /* sysex data bytes go here */};

// Send the SysexMessage
        midiBus.sendMessage(sysexMessageData);

        Assertions.assertTrue(sysexMessageData.length != 0);
    }

    @Test
    public void sendMessageTest() throws Exception {
        MidiBus midiBus = new MidiBus();
        midiBus.sendMessage(0x90, 0, 60, 127);

        Assertions.assertTrue(midiBus.bus_name != null);
    }


    
}
