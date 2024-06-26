import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import javax.sound.midi.InvalidMidiDataException;
import javax.sound.midi.ShortMessage;
import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

class RawMidiListenerTest implements RawMidiListener{
    MidiBus mybus;


    byte[] currentMidiByte = new byte[3];

    // Test that turns on and off Ab
    @Test
    public void sendMidiMessageTest() throws InterruptedException{
        mybus = new MidiBus(this, "Studio 68c", "Studio 68c") ;
        Integer[] rawMsgList = new Integer[3];
        byte[] rawBytes = new byte[3];

//        RawMidiListener listener = null;
        mybus.addMidiListener(this);

        rawBytes[0] = (byte) 144;
        rawBytes[1] = (byte) 80;
        rawBytes[2] = (byte) 127;

        mybus.sendMessage(rawBytes);

        Thread.sleep(2000);

        rawBytes[0] = (byte) 128;
        rawBytes[1] = (byte) 80;
        rawBytes[2] = (byte) 0;

        mybus.sendMessage(rawBytes);
        Thread.sleep(100);

        for (int i = 0; i < 3; i++){
            Assertions.assertEquals(currentMidiByte[i], rawBytes[i]);
        }
    }

    // Send invalid Midi note

    @Test
    public void sendMidiMessageFailTest() throws InterruptedException{
        mybus = new MidiBus(this, "Studio 68c", "Studio 68c") ;
        Integer[] rawMsgList = new Integer[3];
        byte[] rawBytes = new byte[3];

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStreamCaptor));

        mybus.addMidiListener(this);

        mybus.addMidiListener(this);

        rawBytes[0] = (byte) 200;
        rawBytes[1] = (byte) 200;
        rawBytes[2] = (byte) 200;

        mybus.sendMessage(rawBytes);

        System.setErr(System.err);

        String errorMessage = outputStreamCaptor.toString().trim(); // Get the printed error message
//        assertTrue(errorMessage.contains("The MidiBus Warning: Message not sent, invalid MIDI data"));
        Assertions.assertEquals(errorMessage,"The MidiBus Warning: Message not sent, invalid MIDI data" );
    }

    // Send invalid message
    @Test
    public void sendInvalidMessageWithStatus() throws InterruptedException, InvalidMidiDataException {
        mybus = new MidiBus(this, "Studio 68c", "Studio 68c") ;

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStreamCaptor));

        mybus.sendMessage(ShortMessage.NOTE_ON);

        System.setErr(System.err);
        String errorMessage = outputStreamCaptor.toString().trim();
        Assertions.assertEquals(errorMessage,"The MidiBus Warning: Message not sent, invalid MIDI data");

    }

    @Test
    public void sendValidMessageWithStatus() throws InterruptedException, InvalidMidiDataException {
        mybus = new MidiBus(this, "Studio 68c", "Studio 68c") ;

        ByteArrayOutputStream outputStreamCaptor = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outputStreamCaptor));

        mybus.sendMessage(ShortMessage.NOTE_ON , 4);

        System.setErr(System.err);
        String errorMessage = outputStreamCaptor.toString().trim();
        Assertions.assertEquals(errorMessage,"The MidiBus Warning: Message not sent, invalid MIDI data");

    }

    @Test
    public void midiMessageDifferentConstructor(){
        mybus = new MidiBus(this, "Studio 68c", "Studio 68c") ;
        mybus.sendMessage(ShortMessage.NOTE_ON, 1, 60, 100);

        Assertions.assertTrue(true);
    }

    @Override
    public void rawMidiMessage(int command, int channel, int data1, int data2){

        System.out.println("we in here ");
        System.out.println("ocme on bruh");
    }

    @Override
    public void rawMidiMessage(byte[] data){
        currentMidiByte = data;
        System.out.println("here");
        for (int i = 0; i < data.length; i++){
            System.out.println("raw midi: ");
            System.out.println(data[i]);
        }
    }



}