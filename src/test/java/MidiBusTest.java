import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.Mockito;

import javax.sound.midi.MidiDevice;
import javax.sound.midi.MidiSystem;
import javax.sound.midi.MidiUnavailableException;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class MidiBusTest {

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
    public void constructorTest(){
        MidiBus myBus = new MidiBus(this, 0, "Studio 68c", "68cbus");
        Assertions.assertTrue(myBus.bus_name == "68cbus");
        Assertions.assertTrue(myBus.input_devices!= null);
    }

    @Test
    public void constructor1Test(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", 0, "68cbus");
        Assertions.assertTrue(myBus.bus_name == "68cbus");
        Assertions.assertTrue(myBus.input_devices!= null);
    }

    @Test
    public void addInputErrorTest(){
        MidiBus myBus = new MidiBus(this, "Studio 68c", 0, "68cbus");

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStreamCaptor));

        myBus.addInput(123);

        System.setErr(System.err);
        String errorMessage = outputStreamCaptor.toString().trim();
        Assertions.assertEquals(errorMessage,"The MidiBus Warning: The chosen input device numbered [123] was not added because it doesn't exist");

    }


}