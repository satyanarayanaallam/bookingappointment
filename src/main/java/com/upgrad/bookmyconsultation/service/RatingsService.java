package com.upgrad.bookmyconsultation.service;

import com.upgrad.bookmyconsultation.entity.Doctor;
import com.upgrad.bookmyconsultation.entity.Rating;
import com.upgrad.bookmyconsultation.repository.DoctorRepository;
import com.upgrad.bookmyconsultation.repository.RatingsRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;


@Service
public class RatingsService {

	@Autowired
	private ApplicationEventPublisher publisher;

	@Autowired
	private RatingsRepository ratingsRepository;

	@Autowired
	private DoctorRepository doctorRepository;

	
	//create a method name submitRatings with void return type and parameter of type Rating
		//set a UUID for the rating
		//save the rating to the database
		//get the doctor id from the rating object
		//find that specific doctor with the using doctor id
		//modify the average rating for that specific doctor by including the new rating
		//save the doctor object to the database
	public void submitRating(Rating rating) {
		// Set a UUID for the rating
		rating.setId(UUID.randomUUID().toString());

		// Save the rating to the database
		ratingsRepository.save(rating);

		// Get the doctor ID from the rating object
		String doctorId = rating.getDoctorId();

		// Find that specific doctor using the doctor ID
		Doctor doctor = doctorRepository.findById(doctorId).orElseThrow(() -> new RuntimeException("Doctor not found"));

		// Get all ratings for the doctor
		List<Rating> doctorRatings = ratingsRepository.findByDoctorId(doctorId);

		// Calculate the new average rating by including the new rating
		double newAverageRating = doctorRatings.stream()
				.collect(Collectors.averagingDouble(Rating::getRating));

		// Set the new average rating for the doctor
		doctor.setRating(newAverageRating);

		// Save the doctor object to the database
		doctorRepository.save(doctor);
	}
}
