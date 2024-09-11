package com.upgrad.bookmyconsultation.service;

import com.upgrad.bookmyconsultation.entity.Address;
import com.upgrad.bookmyconsultation.entity.Appointment;
import com.upgrad.bookmyconsultation.entity.Doctor;
import com.upgrad.bookmyconsultation.enums.Speciality;
import com.upgrad.bookmyconsultation.exception.InvalidInputException;
import com.upgrad.bookmyconsultation.exception.ResourceUnAvailableException;
import com.upgrad.bookmyconsultation.model.TimeSlot;
import com.upgrad.bookmyconsultation.repository.AddressRepository;
import com.upgrad.bookmyconsultation.repository.AppointmentRepository;
import com.upgrad.bookmyconsultation.repository.DoctorRepository;
import com.upgrad.bookmyconsultation.util.ValidationUtils;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import springfox.documentation.annotations.Cacheable;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Log4j2
@Service
public class DoctorService {
	@Autowired
	private AppointmentRepository appointmentRepository;
	@Autowired
	private DoctorRepository doctorRepository;
	@Autowired
	private AddressRepository addressRepository;

	
	//create a method register with return type and parameter of typeDoctor
	//declare InvalidInputException for the method
		//validate the doctor details
		//if address is null throw InvalidInputException
		//set UUID for doctor using UUID.randomUUID.
		//if speciality is null 
			//set speciality to Speciality.GENERAL_PHYSICIAN
		//Create an Address object, initialise it with address details from the doctor object
		//Save the address object to the database. Store the response.
		//Set the address in the doctor object with the response
		//save the doctor object to the database
		//return the doctor object
	public Doctor register(Doctor doctor) throws InvalidInputException {
		// Validate the doctor details
		ValidationUtils.validate(doctor);

		// If address is null throw InvalidInputException
		if (doctor.getAddress() == null) {
			throw new InvalidInputException("Address cannot be null");
		}

		// Set UUID for doctor using UUID.randomUUID().
		doctor.setId(UUID.randomUUID().toString());

		// If speciality is null
		if (doctor.getSpeciality() == null) {
			// Set speciality to Speciality.GENERAL_PHYSICIAN
			doctor.setSpeciality(Speciality.GENERAL_PHYSICIAN);
		}

		// Create an Address object, initialize it with address details from the doctor object
		Address address = new Address();
		address.setId(UUID.randomUUID().toString());
		address.setAddressLine1(doctor.getAddress().getAddressLine1());
		address.setAddressLine2(doctor.getAddress().getAddressLine2());
		address.setCity(doctor.getAddress().getCity());
		address.setState(doctor.getAddress().getState());
		address.setPostcode(doctor.getAddress().getPostcode());

		// Save the address object to the database. Store the response.
		Address savedAddress = addressRepository.save(address);

		// Set the address in the doctor object with the response
		doctor.setAddress(savedAddress);

		// Save the doctor object to the database
		return doctorRepository.save(doctor);
	}
	
	//create a method name getDoctor that returns object of type Doctor and has a String paramter called id
		//find the doctor by id
		//if doctor is found return the doctor
		//else throw ResourceUnAvailableException

	public Doctor getDoctor(String id) {
		// Find the doctor by id
		return doctorRepository.findById(id)
				// If doctor is found return the doctor
				.orElseThrow(() -> new ResourceUnAvailableException("Doctor not found with id: " + id));
	}

	public List<Doctor> getAllDoctorsWithFilters(String speciality) {

		if (speciality != null && !speciality.isEmpty()) {
			return doctorRepository.findBySpecialityOrderByRatingDesc(Speciality.valueOf(speciality));
		}
		return getActiveDoctorsSortedByRating();
	}

	@Cacheable(value = "doctorListByRating")
	private List<Doctor> getActiveDoctorsSortedByRating() {
		log.info("Fetching doctor list from the database");
		return doctorRepository.findAllByOrderByRatingDesc()
				.stream()
				.limit(20)
				.collect(Collectors.toList());
	}

	public TimeSlot getTimeSlots(String doctorId, String date) {

		TimeSlot timeSlot = new TimeSlot(doctorId, date);
		timeSlot.setTimeSlot(timeSlot.getTimeSlot()
				.stream()
				.filter(slot -> {
					return appointmentRepository
							.findByDoctorIdAndTimeSlotAndAppointmentDate(timeSlot.getDoctorId(), slot, timeSlot.getAvailableDate()) == null;

				})
				.collect(Collectors.toList()));

		return timeSlot;

	}
}
