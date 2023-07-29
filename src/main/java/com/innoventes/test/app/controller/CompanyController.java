package com.innoventes.test.app.controller;

import java.beans.PropertyDescriptor;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.innoventes.test.app.dto.CompanyDTO;
import com.innoventes.test.app.entity.Company;
import com.innoventes.test.app.exception.ValidationException;
import com.innoventes.test.app.mapper.CompanyMapper;
import com.innoventes.test.app.service.CompanyService;

@RestController
@RequestMapping("/api/v1")
public class CompanyController {

	@Autowired
	private CompanyMapper companyMapper;

	@Autowired
	private CompanyService companyService;

	@Autowired
	private MessageSource messageSource;

	@GetMapping("/companies")
	public ResponseEntity<List<CompanyDTO>> getAllCompanies() {
		List<Company> companyList = companyService.getAllCompanies();

		List<CompanyDTO> companyDTOList = new ArrayList<>();

		for (Company entity : companyList) {
			companyDTOList.add(companyMapper.getCompanyDTO(entity));
		}

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.status(HttpStatus.OK).location(location).body(companyDTOList);
	}

	@PostMapping("/companies")
	public ResponseEntity<CompanyDTO> addCompany(@Valid @RequestBody CompanyDTO companyDTO)
			throws ValidationException {
		Company company = companyMapper.getCompany(companyDTO);
		Company newCompany = companyService.addCompany(company);
		CompanyDTO newCompanyDTO = companyMapper.getCompanyDTO(newCompany);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(newCompany.getId())
				.toUri();
		return ResponseEntity.created(location).body(newCompanyDTO);
	}

	@PutMapping(value = "/companies/{id}")
	public ResponseEntity<CompanyDTO> updateCompany(@PathVariable(value = "id") Long id,
			@Valid @RequestBody CompanyDTO companyDTO) throws ValidationException {
		Company company = companyMapper.getCompany(companyDTO);
		Company updatedCompany = companyService.updateCompany(id, company);
		CompanyDTO updatedCompanyDTO = companyMapper.getCompanyDTO(updatedCompany);

		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.status(HttpStatus.OK).location(location).body(updatedCompanyDTO);
	}

	@DeleteMapping(value = "/companies/{id}")
	public ResponseEntity<CompanyDTO> deleteCompany(@PathVariable(value = "id") Long id) {
		companyService.deleteCompany(id);
		return ResponseEntity.noContent().build();
	}

	public String getMessage(String exceptionCode) {
		return messageSource.getMessage(exceptionCode, null, LocaleContextHolder.getLocale());
	}

	/**
	 * Get company details by passing the company ID
	 * @param id Company ID
	 * @return ResponseEntity with status ok and result in body
	 * @author arup.khanra
	 * */
	@GetMapping(value = "/companies/{id}")
	public ResponseEntity<CompanyDTO> getCompanyById(@PathVariable(value = "id") Long id){
		Company result = companyService.getCompanyById(id);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.status(HttpStatus.OK).location(location).body(companyMapper.getCompanyDTO(result));
	}

	/**
	 * Get company details by passing the company code
	 * @param companyCode Company code
	 * @return ResponseEntity with status ok and result in body
	 * @author arup.khanra
	 * */
	@GetMapping(value = "/companies/{companyCode}")
	public ResponseEntity<CompanyDTO> getCompanyByCompanyCode(@PathVariable(value = "companyCode") String companyCode){
		Company result = companyService.getCompanyByCompanyCode(companyCode);
		URI location = ServletUriComponentsBuilder.fromCurrentRequest().build().toUri();
		return ResponseEntity.status(HttpStatus.OK).location(location).body(companyMapper.getCompanyDTO(result));
	}

	@PatchMapping("/companies/{companyId}")
	public ResponseEntity partialUpdateCompany(
			@PathVariable(value = "companyId") Long companyId,
			@Valid @RequestBody CompanyDTO companyDTO) {
		// Fetch the existing Company entity from the database using companyId
		Company existingCompany = companyService.getCompanyById(companyId);

		if (existingCompany == null) {
			return ResponseEntity.notFound().build();
		}

		// Update the Company entity with the non-null values from the CompanyDTO
		BeanUtils.copyProperties(companyDTO, existingCompany, getNullPropertyNames(companyDTO));

		// Perform additional validation (Task 6) on the updated fields
		// (e.g., validate the updated webSiteURL and strength fields if they exist)

		// Save the updated Company entity back to the database
		// Implement your logic to save the Company entity back to the database.

		return ResponseEntity.ok("Partial Update Successfully..!");
	}

	// Helper method to get the names of null properties from the CompanyDTO
	private String[] getNullPropertyNames(CompanyDTO companyDTO) {
		BeanWrapper beanWrapper = new BeanWrapperImpl(companyDTO);
		List<String> nullPropertyNames = new ArrayList<>();
		for (PropertyDescriptor propertyDescriptor : beanWrapper.getPropertyDescriptors()) {
			String propertyName = propertyDescriptor.getName();
			if (beanWrapper.getPropertyValue(propertyName) == null) {
				nullPropertyNames.add(propertyName);
			}
		}
		return nullPropertyNames.toArray(new String[0]);
	}
}
