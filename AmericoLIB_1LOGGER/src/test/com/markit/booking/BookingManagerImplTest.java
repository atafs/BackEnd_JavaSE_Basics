package test.com.markit.booking;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import main.com.markit.booking.dao.Room;
import main.com.markit.booking.impl.BookingManagerImpl;
import main.com.markit.booking.interfaces.BookingManager;

public class BookingManagerImplTest {
	
	//attributes
	private BookingManager bookingManager;
	private LocalDate localDate;
	
	@Before
	public void setUp(){
		Map<Integer, Room> rooms = new HashMap<>();
		rooms.put(101, new Room(101));
		rooms.put(102, new Room(102));
		rooms.put(103, new Room(103));
		rooms.put(104, new Room(104));
		
		ConcurrentMap<LocalDate, ConcurrentMap<Integer, String>> bookings = new ConcurrentHashMap<>();
		
		bookingManager = new BookingManagerImpl(rooms, bookings);
		localDate = LocalDate.now();
	}

	
	@Test
	public void isRoomAvailableYes() {
		boolean available = bookingManager.isRoomAvailable(101, localDate);
		Assert.assertEquals("The room is available.", true, available);
	}
	
	@Test
	public void isRoomAvailableNo() {
		bookingManager.addBooking("Batalha", 101, localDate);
		boolean available = bookingManager.isRoomAvailable(101, localDate);
		Assert.assertEquals("The room is not available.", false, available);
	}
	
	@Test
	public void isRoomAvailableYesDifferentDates() {
		bookingManager.addBooking("Americo", 101, localDate);
		boolean available = bookingManager.isRoomAvailable(101, LocalDate.of(2014, 02, 12));
		Assert.assertEquals("The room is available.", true, available);
	}

	@Test
	public void testAddBooking() {
		bookingManager.addBooking("Americo", 101, localDate);
	}
	
	@Test
	public void addBookingSameRoomDifferentDates() {
		bookingManager.addBooking("Guida", 101, LocalDate.of(2014, 02, 12));
		bookingManager.addBooking("Baby", 101, LocalDate.of(2014, 01, 12));
	}
	
	@Test
	public void addBookingSameGuestDifferentRooms() {
		bookingManager.addBooking("Americo", 101, LocalDate.of(2014, 02, 12));
		bookingManager.addBooking("Pai", 102, LocalDate.of(2014, 02, 12));
	}
	
	@Test
	public void testAddBookingSameGuestDifferentDates() {
		bookingManager.addBooking("Smith", 101, LocalDate.of(2014, 02, 12));
		bookingManager.addBooking("Smith", 101, LocalDate.of(2014, 02, 12));
	}
	
	

}
