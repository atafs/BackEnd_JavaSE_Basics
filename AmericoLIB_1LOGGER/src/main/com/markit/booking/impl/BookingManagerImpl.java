package main.com.markit.booking.impl;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;
import java.util.stream.Collectors;

import log.LogMessage;
import main.com.markit.booking.dao.Room;
import main.com.markit.booking.exception.RoomDoesNotExistException;
import main.com.markit.booking.interfaces.BookingManager;

public class BookingManagerImpl implements BookingManager {

	//attributes
	private static LogMessage logger = new LogMessage();
	private Map<Integer, Room> rooms;
	private ConcurrentMap<LocalDate, ConcurrentMap<Integer, String>> bookings;
	
	//constructor
	public BookingManagerImpl(Map<Integer, Room> rooms, ConcurrentMap<LocalDate, ConcurrentMap<Integer, String>> bookings) {
		this.rooms = rooms;
		this.bookings = bookings;
	}
	
	//getters and setters
	public Map<Integer, Room> getRooms() {
		return rooms;
	}

	public void setRooms(Map<Integer, Room> rooms) {
		this.rooms = rooms;
	}

	public ConcurrentMap<LocalDate, ConcurrentMap<Integer, String>> getBookings() {
		return bookings;
	}

	public void setBookings(ConcurrentMap<LocalDate, ConcurrentMap<Integer, String>> bookings) {
		this.bookings = bookings;
	}
	
	//private methods
	private boolean validadeIfRoomExists(Integer room) {
		if(!rooms.containsKey(room)) {
			try {
				throw new RoomDoesNotExistException("Invalid room: it does not exist in this hotel [" + room + "]");
			} catch (RoomDoesNotExistException e) {
				logger.getLog().warn("WARN: this room does not exists in this hotel => [" + room + "]" );
				e.printStackTrace();
				return false;
			}
		} else {
			logger.getLog().info("INFO: this room exists in this hotel => [" + room + "]" );
			return true;
		}
	}
		
	@Override
	public boolean isRoomAvailable(Integer room, LocalDate date) {
		if( room == null || date == null){
			try {
				throw new IllegalArgumentException("Invalid arguments: room [" + room + "]" +  " and date [" + date + "]" +  " must be passed as args");
			} catch (Exception e) {
				logger.getLog().error("ERROR: invalid arguments: room [" + room + "]" +  " and date [" + date + "]" +  " must be passed as args");
				e.printStackTrace();
				return false;
			}
		}
				
		if(validadeIfRoomExists(room) && !this.bookings.containsKey(date)) {
			return true;
		}	
		return false;
	}

	@Override
	public void addBooking(String guest, Integer room, LocalDate date) {
		if( guest == null || room == null || date == null){
			try {
				throw new IllegalArgumentException("Invalid arguments: guest [" + guest + "]" +  " room [" + room + "]" + " and date [" + date + "]" +  " must be passed as args");
			} catch (Exception e) {
				logger.getLog().error("Invalid arguments: guest [" + guest + "]" +  " room [" + room + "]" + " and date [" + date + "]" +  " must be passed as args");
				e.printStackTrace();
				return;
			}
		} 
		
		ConcurrentMap<Integer, String> roomsAndGuests = new ConcurrentHashMap<>();
		ConcurrentMap<Integer, String> bookingByDate = this.bookings.get(date);
		if(bookingByDate != null) {
			roomsAndGuests = this.bookings.getOrDefault(date, bookingByDate);
			logger.getLog().info("INFO: for the date [" + date + "] these are the rooms and guests occupied => " + roomsAndGuests);
		}
		
		roomsAndGuests.putIfAbsent(room, guest);
		if(validadeIfRoomExists(room) && !this.bookings.containsValue(roomsAndGuests)) {
			this.bookings.putIfAbsent(date, roomsAndGuests);
			logger.getLog().info("INFO: added room to guest => " + this.bookings.get(date));
			return;
		}
		logger.getLog().debug("DEBUG: room already taken by another guest => " + this.bookings.get(date));

	}

	@Override
	public Iterable<Integer> getAvailableRooms(LocalDate date) {
		if(date == null){
			try {
				throw new IllegalArgumentException("Invalid arguments: date [" + date + "]" +  " must be passed as args");
			} catch (Exception e) {
				logger.getLog().error("Invalid arguments: date [" + date + "]" +  " must be passed as args");
				e.printStackTrace();
				return null;
			}
		} 
		
		Collection<Integer> availableRooms = new ArrayList<>();
		for (Integer room : this.rooms.keySet()) {
			availableRooms.add(room);
		}
		
		if(this.bookings == null) {
			logger.getLog().info("INFO: available rooms are " + availableRooms);
			return availableRooms;
		}
		
		Collection<Integer> roomsBookedByDate = this.bookings.get(date).keySet().stream().collect(Collectors.toList());
		availableRooms.removeAll(roomsBookedByDate);
		logger.getLog().info("INFO: available rooms are " + availableRooms);

		return availableRooms;
	}

}
