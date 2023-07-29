package com.innoventes.test.app.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;
import javax.validation.constraints.*;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "company")
@Entity
public class Company extends BaseEntity {

	@Id
	@SequenceGenerator(sequenceName = "company_seq", allocationSize = 1, name = "company_seq")
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "company_seq")
	private Long id;

	@Column(name = "company_name")
	@NotBlank(message = "Company name is mandatory")
	@Size(min = 5, message = "Company name must be at least 5 characters long")
	private String companyName;

	@Column(name = "email")
	@Email(message = "Please provide valid email!")
	@NotBlank(message = "Email is mandatory")
	private String email;
	
	@Column(name = "strength")
	@PositiveOrZero(message = "Strength should be a positive number or zero")
	@Null(message = "Strength must be null if not provided")
	private Integer strength;
	
	@Column(name = "website_url")
	private String webSiteURL;
}
