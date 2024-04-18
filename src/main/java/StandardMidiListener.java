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
 * A StandardMidiListener can be notified of incomming MIDI messages in MidiMessage form, usually by a MidiBus object which it is connected to. Typically it would analyse and react to incomming MIDI messages in some useful way.
 *
 * @version 009
 * @author Severin Smith
 * @see MidiListener
 * @see RawMidiListener
 * @see SimpleMidiListener
 * @see ObjectMidiListener
 * @see MidiBus
 * @see MidiMessage
*/
public interface StandardMidiListener extends MidiListener {
	/**
	 * Objects notifying this StandardMidiListener of a new MIDI message call this method and pass the MidiMessage
	 * 
	 * @param message the MidiMessage received
	 * @param timeStamp the timestamp of the midiMessage
	*/
	public void midiMessage(MidiMessage message, long timeStamp);
}