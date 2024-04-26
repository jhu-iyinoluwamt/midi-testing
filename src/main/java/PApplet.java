/**
 * Copyright (c) 2009 Severin Smith
 *
 * This file is part of a library called The MidiBus (themidibus) - https://www.smallbutdigital.com/themidibus.php.
 *
 * The MidiBus is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The MidiBus is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the MidiBus. If not, see <http://www.gnu.org/licenses/>.
*/

import javax.sound.midi.MidiMessage;

/**
 * PApplet is your processing application or sketch. In it you can implement the following methods which will be called whenerever a MidiBus object attached to the PApplet, recieves a new incomming MIDI message of the appropriate type.
 * <p>
 * <i><b style="color:red;">Note:</b> This page is a dummy page for documentation of the MidiBus' extention of the regular PApplet's functionality, for the full documentation of PApplet please visits the <a target="_blank" href="http://dev.processing.org/reference/core/javadoc/processing/core/PApplet.html">Processing javadocs</a></i>
 *
 * @version 009
 * @author Severin Smith
 * @see MidiBus
 * @see MidiListener
 * @see RawMidiListener
 * @see StandardMidiListener
 * @see SimpleMidiListener
*/
public class PApplet {

	public int globalChannel;
	public int globalPitch;
	public int globalVelocity;
	public long globalTimestamps;

	public int globalNumber;

	public int globalValue;

	public String globalBusName;

	public int globalStatus;
	public int globalData1;
	public int globalData2;
	
	/**
	 * Is passed the channel, controller number and contoller value associated with every new ContollerChange MIDI message recieved by a MidiBus attached to this applet.
	 *
	 * @param channel the channel on which the ContollerChange arrived
	 * @param number the controller number associated with the ContollerChange
	 * @param value the controller value associated with the ContollerChange
	 * @see #controllerChange(int channel, int pitch, int velocity, long timestamp, String bus_name)
	*/
	public void controllerChange(int channel, int number, int value) {
		globalChannel = channel;
		globalNumber = number;
		globalValue = value;
	}
	
	/**
	 * Is passed the channel, pitch and velocity associated with every new NoteOff MIDI message recieved by a MidiBus attached to this applet and the name of the MidiBus which recieved the message.
	 *
	 * @param channel the channel on which the ContollerChange arrived
	 * @param number the controller number associated with the ContollerChange
	 * @param value the controller value associated with the ContollerChange
	 * @param timestamp the timestamp on the midi message
	 * @param bus_name the name of MidiBus which recieved the ContollerChange
	 * @see #controllerChange(int channel, int pitch, int velocity)
	*/
	public void controllerChange(int channel, int number, int value, long timestamp, String bus_name) {
		System.out.println("here");
		globalChannel = channel;
		globalValue = value;
		globalTimestamps = timestamp;
		globalBusName = bus_name;
	}

	/**
	 * Is passed a ControlChange object representing controlChange event
	 * 
	 * @param change the ControlChange object
	 * @see #controllerChange(int channel, int pitch, int velocity)
	 * @see themidibus.ObjectMidiListener#controllerChange(ControlChange)
	*/
	public void controllerChange(ControlChange change) {
		globalChannel = change.channel;
		globalValue = change.value;
		globalNumber = change.number;
		globalBusName = change.bus_name;
	}
	
	/**
	 * Is passed the raw MidiMessage associated with every new MIDI message recieved by a MidiBus attached to this applet.
	 *
	 * @param message the MidiMessage recieved
	 * @see #midiMessage(MidiMessage message, long timestamp, String bus_name)
	*/
	public void midiMessage(MidiMessage message) {
		globalStatus = message.getStatus();
		byte[] msg = new byte[3];
		msg = message.getMessage();

		globalStatus = msg[0];
		globalData1 = msg[1];
		globalData2 = msg[2];

	}
	
	/**
	 * Is passed the raw MidiMessage associated with every new MIDI message recieved by a MidiBus attached to this applet and the name of the MidiBus which recieved the message.
	 *
	 * @param message the MidiMessage recieved
	 * @param timestamp the timestamp on the midi message
	 * @param bus_name the name of MidiBus which recieved the MIDI message 
	 * @see #midiMessage(MidiMessage message)
	*/
	public void midiMessage(MidiMessage message, long timestamp, String bus_name) {
		globalStatus = message.getStatus();
		byte[] msg = new byte[3];
		msg = message.getMessage();

		globalStatus = msg[0];
		globalData1 = msg[1];
		globalData2 = msg[2];

		globalTimestamps = timestamp;
		globalBusName = bus_name;
	}
	
	/**
	 * Is passed the channel, pitch and velocity associated with every new NoteOff MIDI message recieved by a MidiBus attached to this applet.
	 *
	 * @param channel the channel on which the NoteOff arrived
	 * @param pitch the pitch associated with the NoteOff
	 * @param velocity the velocity associated with the NoteOff
	 * @see #noteOff(int channel, int pitch, int velocity, long timestamp, String bus_name)
	*/
	public void noteOff(int channel, int pitch, int velocity) {
		globalChannel = channel;
		globalPitch = pitch;
		globalVelocity = velocity;
	}
	
	/**
	 * Is passed the channel, pitch and velocity associated with every new NoteOff MIDI message recieved by a MidiBus attached to this applet and the name of the MidiBus which recieved the message.
	 *
	 * @param channel the channel on which the NoteOff arrived
	 * @param pitch the pitch associated with the NoteOff
	 * @param velocity the velocity associated with the NoteOff
	 * @param timestamp the timestamp on the midi message
	 * @param bus_name the name of MidiBus which recieved the NoteOff
	 * @see #noteOff(int channel, int pitch, int velocity)
	*/
	public void noteOff(int channel, int pitch, int velocity, long timestamp, String bus_name) {
		globalChannel = channel;
		globalPitch = pitch;
		globalVelocity = velocity;
		globalTimestamps = timestamp;
		globalBusName = bus_name;
	}

	/**
	 * Is passed a Note object representing noteOff event
	 * 
	 * @param note the Note object
	 * @see #noteOff(int channel, int pitch, int velocity)
	 * @see themidibus.ObjectMidiListener#noteOn(Note)
	*/
	public void noteOff(Note note) {
		globalBusName = note.bus_name;
		globalVelocity = note.velocity;
		globalPitch = note.pitch;
		globalChannel = note.channel;
		globalTimestamps = note.timestamp;

		System.out.println("here");
	}
	
	/**
	 * Is passed the channel, pitch and velocity associated with every new NoteOn MIDI message recieved by a MidiBus attached to this applet.
	 *
	 * @param channel the channel on which the NoteOn arrived
	 * @param pitch the pitch associated with the NoteOn
	 * @param velocity the velocity associated with the NoteOn
	 * @see #noteOn(int channel, int pitch, int velocity, long timestamp, String bus_name)
	*/
	public void noteOn(int channel, int pitch, int velocity) {
		globalVelocity = velocity;
		globalPitch = pitch;
		globalChannel = channel;
	}
	
	/**
	 * Is passed the channel, pitch and velocity associated with every new NoteOn MIDI message recieved by a MidiBus attached to this applet and the name of the MidiBus which recieved the message.
	 *
	 * @param channel the channel on which the NoteOn arrived
	 * @param pitch the pitch associated with the NoteOn
	 * @param velocity the velocity associated with the NoteOn
	 * @param timestamp the timestamp on the midi message
	 * @param bus_name the name of MidiBus which recieved the NoteOn 
	 * @see #noteOn(int channel, int pitch, int velocity)
	*/
	public void noteOn(int channel, int pitch, int velocity, long timestamp, String bus_name) {
		globalVelocity = velocity;
		globalPitch = pitch;
		globalChannel = channel;
		globalTimestamps = timestamp;
		globalBusName = bus_name;
	}

	/**
	 * Is passed a Note object representing noteOn event
	 * 
	 * @param note the Note object
	 * @see #noteOn(int channel, int pitch, int velocity)
	 * @see themidibus.ObjectMidiListener#noteOff(Note)
	*/
	public void noteOn(Note note) {
		globalBusName = note.bus_name;
		globalVelocity = note.velocity;
		globalPitch = note.pitch;
		globalChannel = note.channel;
		globalTimestamps = note.timestamp;
	}
	
	/**
	 * Is passed the raw data associated with every new MIDI message recieved by a MidiBus attached to this applet.
	 *
	 * @param data the raw data associated with the MIDI message
	 * @see #rawMidi(byte[] data, long timestamp, String bus_name)
	*/
	public void rawMidi(byte[] data) {

		globalStatus = data[0];
		globalData1 = data[1];
		globalData2 = data[2];
	}
	
	/**
	 * Is passed the raw data associated with every new MIDI message recieved by a MidiBus attached to this applet and the name of the MidiBus which recieved the message.
	 *
	 * @param data the raw data associated with the MIDI message
	 * @param timestamp the timestamp on the midi message
	 * @param bus_name the name of MidiBus which recieved the MIDI message 
	 * @see #rawMidi(byte[] data)
	*/
	public void rawMidi(byte[] data, long timestamp, String bus_name) {
		globalStatus = data[0];
		globalData1 = data[1];
		globalData2 = data[2];
		globalTimestamps = timestamp;
		globalBusName = bus_name;
	}
}
