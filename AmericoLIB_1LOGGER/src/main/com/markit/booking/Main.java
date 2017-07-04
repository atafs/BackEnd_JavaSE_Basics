package main.com.markit.booking;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import log.LogMessage;
import main.com.markit.booking.dao.Room;
import main.com.markit.booking.impl.BookingManagerImpl;
import main.com.markit.booking.interfaces.BookingManager;

public class Main {
	
	//logger
	private static LogMessage logger = new LogMessage();
	
	//main
	public static void main(String[] args) {
		
		//instantiate rooms, guests and dates
		Map<Integer, Room> rooms = new HashMap<>();
		rooms.put(101, new Room(101));
		rooms.put(102, new Room(102));
		rooms.put(103, new Room(103));
		rooms.put(104, new Room(104));
		
		logger.getLog().info("INFO: Rooms available in Hotel: \n" + rooms);
		
		ConcurrentMap<LocalDate, ConcurrentMap<Integer, String>> bookings = new ConcurrentHashMap<>();
		
		//is room available when it has not been booked
		BookingManager bm = new BookingManagerImpl(rooms, bookings);
		LocalDate today = LocalDate.now();
		logger.getLog().debug("Is Room Available => " + bm.isRoomAvailable(103, today) + "\n");
		logger.getLog().debug("Is Room Available => " + bm.isRoomAvailable(105, today) + "\n");
		logger.getLog().debug("Is Room Available => " + bm.isRoomAvailable(null, today) + "\n");
		logger.getLog().debug("Is Room Available => " + bm.isRoomAvailable(105, null) + "\n");

		
		//is room available after a guest booking
		bm.addBooking("Batalha", 101, today);
		bm.addBooking("Batalha", 104, today);
		bm.addBooking("Batalha", 101, today);
		bm.addBooking(null, 101, today);
		bm.addBooking("Batalha", null, today);
		bm.addBooking("Batalha", 101, null);

		//get available rooms
		bm.getAvailableRooms(today);

	}
}
