import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ObjectMidiListenerTest implements ObjectMidiListener{

    public int globalPitch;
    public int globalNoteOn;

    @Test
    public void BusNameVerificationTest() throws Exception {
        MidiBus newBus = new MidiBus(this, "Studio 68c", "Studio 68c");
        newBus.sendMessage(0x90, 0, 60, 127);

//        Assertions.assertEquals(newBus.getBusName(), "Studio 68c");
        ObjectMidiListener listener = new ObjectMidiListener() {
            @Override
            public void noteOn(Note note) {
                globalNoteOn = 1;
            }

            @Override
            public void noteOff(Note note) {
                globalNoteOn = 0;

            }

            @Override
            public void controllerChange(ControlChange change) {

            }
        };


        Assertions.assertTrue(newBus.addMidiListener(listener));


        Assertions.assertEquals(globalNoteOn, 0);
        Assertions.assertTrue(newBus.getBusName().contains("MidiBus"));
    }

    @Override
    public void noteOn(Note note) {
        globalNoteOn = 1;
        System.out.println("here");
    }

    /**
     * Objects notifying this ObjectMidiListener of a new NoteOff events call this method.
     *
     * @param note the note object associated with this event
     */
    @Override
    public void noteOff(Note note) {
        globalNoteOn = 0;

    }

    /**
     * Objects notifying this ObjectMidiListener of a new ControllerChange events call this method.
     *
     * @param change the ControlChange object associated with this event
     */
    @Override
    public void controllerChange(ControlChange change) {

    }

}