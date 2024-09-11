package com.upgrad.bookmyconsultation.repository;

import com.upgrad.bookmyconsultation.entity.Address;
import com.upgrad.bookmyconsultation.entity.Appointment;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


//mark it as repository
//create an interface AddressRepository that extends CrudRepository
@Repository
public interface AddressRepository extends CrudRepository<Address, String> {

}
