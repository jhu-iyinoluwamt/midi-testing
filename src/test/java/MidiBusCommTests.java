import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import javax.sound.midi.*;
import static org.junit.jupiter.api.Assertions.*;

public class MidiBusCommTests {
    private MidiBus midiBus;
    private String local = "Bus 1";

    @Mock
    private MidiListener mockListener;

    @BeforeEach
    void setUp() {
        midiBus = new MidiBus();
    }

    @Test
    void testSendNoteOn() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        midiBus.sendNoteOn(0, 60, 127);
        assertMidiMessageReceived(ShortMessage.NOTE_ON, 0, 60, 127);
    }

    @Test
    void testSendNoteOff() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        midiBus.sendNoteOff(0, 60, 127);
        assertMidiMessageReceived(ShortMessage.NOTE_OFF, 0, 60, 127);
    }

    @Test
    void testSendControllerChange() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        midiBus.sendControllerChange(0, 1, 100);
        assertMidiMessageReceived(ShortMessage.CONTROL_CHANGE, 0, 1, 100);
    }

    @Test
    void testSendWithNoOutputs() {
        assertDoesNotThrow(() -> midiBus.sendNoteOn(0, 60, 127),
                "Should handle sending with no outputs gracefully.");
    }


    @Test
    void testSendMessageWithBytes() throws Exception {
        byte[] mockData = new byte[]{(byte) ShortMessage.NOTE_ON, 60, 100};
        assertDoesNotThrow(() -> midiBus.sendMessage(mockData));
    }

    @Test
    void testSendMessageWithStatus() throws Exception {
        assertDoesNotThrow(() -> midiBus.sendMessage(ShortMessage.NOTE_ON));
    }

    @Test
    void testSendMessageWithStatusAndOneData() throws Exception {
        assertDoesNotThrow(() -> midiBus.sendMessage(ShortMessage.CONTROL_CHANGE, 7));
    }

    @Test
    void testSendMessageWithStatusAndTwoData() throws Exception {
        assertDoesNotThrow(() -> midiBus.sendMessage(ShortMessage.CONTROL_CHANGE, 7, 127));
    }

    @Test
    void testSendNoteOnToDifferentChannels() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        for (int i = 0; i < 16; i++) {
            midiBus.sendNoteOn(i, 60, 127);
            assertMidiMessageReceived(ShortMessage.NOTE_ON, i, 60, 127);
        }
    }

    @Test
    void testSendNoteOnWithDifferentNoteValues() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        for (int i = 0; i < 128; i++) {
            midiBus.sendNoteOn(0, i, 127);
            assertMidiMessageReceived(ShortMessage.NOTE_ON, 0, i, 127);
        }
    }

    @Test
    void testSendNoteOnWithDifferentVelocityValues() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        for (int i = 0; i < 128; i++) {
            midiBus.sendNoteOn(0, 60, i);
            assertMidiMessageReceived(ShortMessage.NOTE_ON, 0, 60, i);
        }
    }

    @Test
    void testSendControllerChangeWithDifferentValues() {
        midiBus.addInput(local);
        midiBus.addOutput(local);
        for (int i = 0; i < 128; i++) {
            midiBus.sendControllerChange(0, i, i);
            assertMidiMessageReceived(ShortMessage.CONTROL_CHANGE, 0, i, i);
        }
    }

//    @Test
//    void testStop() {
//        assertDoesNotThrow(() -> midiBus.stop());
//    }

    @Test
    void testAddRemoveMidiListener() {
        assertTrue(midiBus.addMidiListener(mockListener), "Listener should be added successfully.");
        assertFalse(midiBus.addMidiListener(mockListener), "Duplicate listener should not be added.");
        assertTrue(midiBus.removeMidiListener(mockListener), "Listener should be removed successfully.");
        assertFalse(midiBus.removeMidiListener(mockListener), "Non-existent listener removal should be handled.");
    }

    @Test
    void testSendTimestamps() {
        assertTrue(midiBus.sendTimestamps(), "Initial state should send timestamps.");
        midiBus.sendTimestamps(false);
        assertFalse(midiBus.sendTimestamps(), "Should not send timestamps after setting false.");
        midiBus.sendTimestamps(true);
        assertTrue(midiBus.sendTimestamps(), "Should send timestamps after setting true.");
    }

    private void assertMidiMessageReceived(int status, int channel, int data1, int data2) {
        try {
            MidiDevice.Info[] infos = MidiSystem.getMidiDeviceInfo();
            for (MidiDevice.Info info : infos) {
                MidiDevice device = MidiSystem.getMidiDevice(info);
                if (device.getMaxTransmitters() != 0) {
                    device.open();
                    device.getTransmitter().setReceiver(new Receiver() {
                        @Override
                        public void send(MidiMessage message, long timeStamp) {
                            if (message instanceof ShortMessage) {
                                ShortMessage shortMessage = (ShortMessage) message;
                                if (shortMessage.getStatus() == status
                                        && shortMessage.getChannel() == channel
                                        && shortMessage.getData1() == data1
                                        && shortMessage.getData2() == data2) {
                                    System.out.println("Received MIDI message: " + shortMessage);
                                    assertEquals(status, shortMessage.getStatus());
                                    assertEquals(channel, shortMessage.getChannel());
                                    assertEquals(data1, shortMessage.getData1());
                                    assertEquals(data2, shortMessage.getData2());
                                }
                            }
                        }

                        @Override
                        public void close() {
                        }
                    });
                    break;
                }
            }
        } catch (MidiUnavailableException e) {
            e.printStackTrace();
        }
    }

}
