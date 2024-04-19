import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.MidiMessage;
import javax.sound.midi.ShortMessage;

public class PianoInputTest implements SimpleMidiListener{

    int globalpitch;
    int globalVelocity;
    int globalNoteChannel;
    int noteOnCount = 0;
    int noteOffCount = 0;

    // A LIST OF THE LAST 20 RECENTLY PLAYED NOTES
    String[] pitchesRecentlyPlayedList = new String[20];
    int getGlobalControllerChangeCount = 0;

    int globalControllerChannel;
    int globalControllerValue;
    int globalControllerNumber;

    MidiBus mybus;


    // PIANO INPUT WHITEBOX AND DEMO TESTS //
//    @Test
//    public void lowestVelocityTest() throws InterruptedException{
//        System.out.println("PLAY KEYBOARD AT SOFTEST VELOCITY");
//
//        midiTest();
//
//        while (noteOnCount < 1){
//            Thread.sleep(100);
//        }
//
//        Assertions.assertTrue(globalVelocity < 50);
//    }
//    @Test
//    public void maxVelocityTest() throws InterruptedException{
//        System.out.println("PLAY KEYBOARD AT GREATEST VELOCITY!!");
//
//        midiTest();
//
//        while (noteOnCount < 1) {
//            Thread.sleep(100); // Add a short delay to avoid busy-waiting
//        }
//
//        Assertions.assertTrue(globalVelocity > 100);
//    }
//
//    @Test
//    public void verifyPitch() throws InterruptedException {
//        System.out.println("PLAY C - E - G Sequenctially");
//
//        midiTest();
//
//        while (noteOnCount < 3){
//            Thread.sleep(100);
//        }
//
//        Assertions.assertTrue(pitchesRecentlyPlayedList[0] == "C"
//                && pitchesRecentlyPlayedList[1] == "E" && pitchesRecentlyPlayedList[2] == "G");
//
//    }

    @Test
    public void noteConstructorTest(){
        Note note = new Note(0, 70, 127, 50);

        Assertions.assertTrue(note.channel == 0);
        Assertions.assertTrue(note.pitch == 70);
        Assertions.assertTrue(note.velocity == 127);
        Assertions.assertTrue(note.ticks == 50);
    }

    @Test
    public void noteConstructor1Test(){
        Note note = new Note(0, 70, 127, 50, 500, "Studio 68c");

        Assertions.assertTrue(note.channel == 0);
        Assertions.assertTrue(note.pitch == 70);
        Assertions.assertTrue(note.velocity == 127);
        Assertions.assertTrue(note.ticks == 50);
        Assertions.assertTrue(note.timestamp == 500);

    }

    @Test
    public void noteRelativePitch(){
        Note note = new Note(0, 70, 127, 50, 500, "newBus");
        note.relativePitch();

        Assertions.assertTrue(note.relativePitch() == 70);
    }

    @Test
    public void noteOctave(){
        Note note = new Note(0, 70, 127, 50, 500, "newBus");
        note.relativePitch();

        Assertions.assertTrue(note.octave() !=0);
    }

    @Test
    public void callPaplet() throws InvalidMidiDataException {
        Note note = new Note(0, 70, 127, 50, 500, "newBus");
        byte[] rawBytes = new byte[3];

        rawBytes[0] = (byte) 144;
        rawBytes[1] = (byte) 80;
        rawBytes[2] = (byte) 127;

        PApplet pObj = new PApplet();
        pObj.noteOn(0,70,127);
        pObj.noteOff(0, 70, 127);

        pObj.controllerChange(0, 70, 127);
        pObj.noteOn(note);
        pObj.noteOff(note);

        pObj.noteOn(0,70, 127, 5, "68c");
        pObj.noteOff(0,70, 0, 5, "68c");

        pObj.rawMidi(rawBytes);
        pObj.rawMidi(rawBytes, 5, "68c");

        pObj.controllerChange(1, 64, 127, 1000, "Studio 68c");
        pObj.controllerChange(1,64,127);

        ShortMessage msg = new ShortMessage(0x90, 60, 127);
        pObj.midiMessage(msg);

        pObj.midiMessage(msg,5,"68c");

        Assertions.assertTrue(note.channel == 0);
    }

    @Test
    public void registerParentWithPap(){
        MidiBus mybus = new MidiBus(this);
        PApplet pobj = new PApplet();

        mybus.registerParent(pobj);
    }


    // -------------------------------------------------------------------------------------------



    // Simple Midi Listenener Implementation
    @Override
    public void noteOn(int channel, int pitch, int velocity) {

        Note note = new Note(channel,pitch,velocity);
        String note_pitch = note.name();
        pitchesRecentlyPlayedList[noteOnCount] = note_pitch;

        System.out.println("Note On -> pitch: " + note_pitch + " velocity: " + velocity + " channel " + channel);
        globalVelocity = velocity;
        globalpitch = pitch;
        globalNoteChannel = channel;

        noteOnCount++;
    }

    @Override
    public void noteOff(int channel, int pitch, int velocity) {
        System.out.println("Note Off: " + pitch + " with velocity " + velocity + " on channel " + channel);
        globalVelocity = velocity;
        globalpitch = pitch;
        globalNoteChannel = channel;
        noteOffCount++;
    }

    @Override
    public void controllerChange(int channel, int number, int value) {
        System.out.println("Controller Change: " + number + " with value " + value + " on channel " + channel);
        globalControllerChannel = channel;
        globalControllerNumber = number;
        globalControllerValue = value;
    }

    public int getVelocity(){
        return globalVelocity;
    }

    public void midiTest() {
        mybus = new MidiBus(this, "Studio 68c", "Studio 68c");
        // Replace "Name of your MIDI keyboard" with the actual name as it appears in your MIDI software or settings
    }
}
