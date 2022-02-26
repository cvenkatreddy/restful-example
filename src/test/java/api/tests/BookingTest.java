package api.tests;

import java.time.LocalDate;

import org.testng.Assert;
import org.testng.annotations.Test;

import api.booking.CreateBooking;
import api.booking.DeleteBooking;
import api.booking.GetBooking;
import api.booking.UpdateBooking;
import env.ApplicationProperties;
import env.Environment;
import pojo.Booking;
import pojo.BookingDates;
import pojo.BookingDetail;


public class BookingTest extends BaseTest {
	ApplicationProperties appProps = Environment.INSTANCE.getApplicationProperties();
	Booking requestBody;
	
	@Test(description = "Name updation for Dee Jay", enabled=true)
	public void updateNameInBooking() throws Exception {
		requestBody = new Booking();
		requestBody.setFirstname("Dee");
		requestBody.setLastname("Jay");
		CreateBooking createBookingRequest = new CreateBooking(appProps.getBaseURL());
		createBookingRequest.setRequestBody(requestBody);
		createBookingRequest.setExpectedStatusCode(200);
		createBookingRequest.perform();

		BookingDetail createBookingResponse = createBookingRequest.getAPIResponseAsPOJO(BookingDetail.class);
		Assert.assertEquals(requestBody, createBookingResponse.getBooking());
		
		UpdateBooking updateBookingRequest = new UpdateBooking(appProps.getBaseURL());
		Booking newRequestBody = new Booking();
		newRequestBody.setFirstname("Dee");
		newRequestBody.setLastname("Jayson");
		updateBookingRequest.setBookingId(createBookingResponse.getBookingid());
		updateBookingRequest.setAuthToken(token.getToken());
		updateBookingRequest.setRequestBody(newRequestBody);
		updateBookingRequest.setExpectedStatusCode(200);
		updateBookingRequest.perform();

		Booking updateBookingResponse = updateBookingRequest.getAPIResponseAsPOJO(Booking.class);
		Assert.assertEquals(newRequestBody, updateBookingResponse);
		

		GetBooking getBookingRequest = new GetBooking(appProps.getBaseURL());
		getBookingRequest.setBookingId(createBookingResponse.getBookingid());
		getBookingRequest.setExpectedStatusCode(200);
		getBookingRequest.perform();

		Booking getBookingResponse = getBookingRequest.getAPIResponseAsPOJO(Booking.class);
		
		Assert.assertEquals(updateBookingResponse, getBookingResponse);
		Assert.assertEquals(updateBookingResponse.getFirstname(), getBookingResponse.getFirstname());
		Assert.assertEquals(updateBookingResponse.getLastname(), getBookingResponse.getLastname());
			
	}
	
	@Test(description = "Update Booking dates for Bloomy Reachson", enabled=true)
	public void updateDatesInBooking() throws Exception {
		requestBody = new Booking();		
		requestBody.setFirstname("Bloomy");
		requestBody.setLastname("Reachson");
		requestBody.setTotalprice(10);
		CreateBooking createBookingRequest = new CreateBooking(appProps.getBaseURL());
		createBookingRequest.setRequestBody(requestBody);
		createBookingRequest.setExpectedStatusCode(200);
		createBookingRequest.perform();

		BookingDetail createBookingResponse = createBookingRequest.getAPIResponseAsPOJO(BookingDetail.class);		
		Assert.assertEquals(requestBody, createBookingResponse.getBooking());
		
		UpdateBooking updateBookingRequest = new UpdateBooking(appProps.getBaseURL());
		Booking newRequestBody = new Booking();
		BookingDates bookingDates = new BookingDates(LocalDate.now().toString(), LocalDate.now().plusDays(14).toString());
		newRequestBody.setFirstname("Bloomy"); // I am setting name(first, lastname) here as faker used in default constructor
		newRequestBody.setLastname("Reachson");
		newRequestBody.setBookingdates(bookingDates);
		newRequestBody.setTotalprice(1000);
		
		updateBookingRequest.setBookingId(createBookingResponse.getBookingid());
		updateBookingRequest.setAuthToken(token.getToken());
		updateBookingRequest.setRequestBody(newRequestBody);
		updateBookingRequest.setExpectedStatusCode(200);
		updateBookingRequest.perform();

		Booking updateBookingResponse = updateBookingRequest.getAPIResponseAsPOJO(Booking.class);
		Assert.assertEquals(newRequestBody, updateBookingResponse);

		GetBooking getBookingRequest = new GetBooking(appProps.getBaseURL());
		getBookingRequest.setBookingId(createBookingResponse.getBookingid());
		getBookingRequest.setExpectedStatusCode(200);
		getBookingRequest.perform();

		Booking getBookingResponse = getBookingRequest.getAPIResponseAsPOJO(Booking.class);
		
		Assert.assertEquals(updateBookingResponse, getBookingResponse);
		Assert.assertEquals(updateBookingResponse.getBookingdates().getCheckin(), getBookingResponse.getBookingdates().getCheckin());
		Assert.assertEquals(updateBookingResponse.getBookingdates().getCheckout(), getBookingResponse.getBookingdates().getCheckout());
		Assert.assertEquals(updateBookingResponse.getTotalprice(), getBookingResponse.getTotalprice());
	
	}
	

	@Test(description = "Delete a booking of John Macintosh", enabled=true)
	public void deleteBooking() throws Exception {
		Booking requestBody = new Booking();
		requestBody.setFirstname("John");
		requestBody.setLastname("Macintosh");
		CreateBooking createBookingRequest = new CreateBooking(appProps.getBaseURL());
		createBookingRequest.setExpectedStatusCode(200);
		createBookingRequest.setRequestBody(requestBody);
		createBookingRequest.perform();

		BookingDetail createBookingResponse = createBookingRequest.getAPIResponseAsPOJO(BookingDetail.class);
		Assert.assertEquals(requestBody, createBookingResponse.getBooking());
		

		DeleteBooking deleteBooking = new DeleteBooking(appProps.getBaseURL());
		deleteBooking.setAuthToken(token.getToken());
		deleteBooking.setBookingId(createBookingResponse.getBookingid());
		deleteBooking.setExpectedStatusCode(201); // We can't use 201 status code for delete, either 204-No Content or 200 is the standard code for delete
		deleteBooking.perform();

		GetBooking getBookingRequest = new GetBooking(appProps.getBaseURL());
		getBookingRequest.setBookingId(createBookingResponse.getBookingid());
		getBookingRequest.setExpectedStatusCode(404);
		getBookingRequest.perform();
	}
}