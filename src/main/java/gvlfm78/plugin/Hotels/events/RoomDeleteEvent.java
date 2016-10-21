package kernitus.plugin.Hotels.events;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import kernitus.plugin.Hotels.Room;

public class RoomDeleteEvent extends Event{

	private Room room;
	private static final HandlerList handlers = new HandlerList();

	public RoomDeleteEvent(Room room){
		this.room = room;
	}

	@Override
	public HandlerList getHandlers() {
		return handlers;
	}

	public static HandlerList getHandlerList() {
		return handlers;
	}

	public Room getRoom(){
		return room;
	}

}
