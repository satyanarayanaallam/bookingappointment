package com.upgrad.bookmyconsultation.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.UUID;


//Mark it with Data, Entity, Builder, NoArgsConstructor, AllArgsConstructor
//create a class named User
	//create firstName of type String
	//create lastName of type String
	//create dob of type String
	//create mobile of type String
	//create primary key 'emailId' of type String
	//create password of type String
	//create createdDate of type String
	//create salt of type String
	//all the mentioned members must be private


@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class User {
    private String firstName;
    private String lastName;
    private String dob;
    private String mobile;
    @Id
    private String emailId;
    private String password;
    private String createdDate;
    private String salt;
}