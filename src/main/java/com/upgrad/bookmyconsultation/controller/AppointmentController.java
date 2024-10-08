package com.upgrad.bookmyconsultation.controller;

import com.upgrad.bookmyconsultation.entity.Appointment;
import com.upgrad.bookmyconsultation.exception.InvalidInputException;
import com.upgrad.bookmyconsultation.exception.ResourceUnAvailableException;
import com.upgrad.bookmyconsultation.exception.SlotUnavailableException;
import com.upgrad.bookmyconsultation.service.AppointmentService;
import lombok.val;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/appointments")
public class AppointmentController {

	@Autowired
	private AppointmentService appointmentService;


	//create a method post method named bookAppointment with return type ReponseEntity
		//method has paramter of type Appointment, use RequestBody Annotation for mapping
	
		//save the appointment details to the database and save the response from the method used
		//return http response using ResponseEntity

		@PostMapping("/book")
		public ResponseEntity<String> bookAppointment(@RequestBody Appointment appointment) {
			try{
				// Save the appointment to the database
				String savedAppointment = appointmentService.bookAppointment(appointment);

				// Return the saved appointment with an HTTP 201 Created status
				return new ResponseEntity<String>(savedAppointment, HttpStatus.CREATED);
			}catch (Exception ex){
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		}
	
	
	
	//create a get method named getAppointment with return type as ResponseEntity
		//method has appointmentId of type String. Use PathVariable annotation to identity appointment using the parameter defined
		
		//get the appointment details using the appointmentId
		//save the response
		//return the response as an http response
		@GetMapping("/{appointmentId}")
		public ResponseEntity<Appointment> getAppointment(@PathVariable String appointmentId) {
			try {
				// Get the appointment details using the appointmentId
				Appointment appointment = appointmentService.getAppointment(appointmentId);

				// Return the appointment with an HTTP 200 OK status
				return new ResponseEntity<>(appointment, HttpStatus.OK);
			} catch (ResourceUnAvailableException e) {
				// Return an error message with an HTTP 404 Not Found status
				return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
			}
		}
	

}