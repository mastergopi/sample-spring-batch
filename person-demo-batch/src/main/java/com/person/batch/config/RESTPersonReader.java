package com.person.batch.config;

import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.NonTransientResourceException;
import org.springframework.batch.item.ParseException;
import org.springframework.batch.item.UnexpectedInputException;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.person.batch.constants.PersonBatchConstants;
import com.person.batch.dto.PersonDto;

public class RESTPersonReader implements ItemReader<PersonDto> {

	private static final Logger LOGGER = LoggerFactory.getLogger(RESTPersonReader.class);

	private final String apiUrl;
	private final RestTemplate restTemplate;

	private int nextPersonIndex;
	private List<PersonDto> personsList;

	public RESTPersonReader(String apiUrl, RestTemplate restTemplate) {
		this.apiUrl = apiUrl + PersonBatchConstants.GET_PERSONS;
		this.restTemplate = restTemplate;
	}

	@Override
	public PersonDto read() throws Exception, UnexpectedInputException, ParseException, NonTransientResourceException {
		if (null == this.personsList || this.personsList.isEmpty()) {
			this.personsList = retrievePersonFromApi();
		}
		PersonDto nextPerson = null;

		if (nextPersonIndex < personsList.size()) {
			nextPerson = personsList.get(nextPersonIndex);
			LOGGER.info("Reading Person detail : {}",nextPersonIndex);
			nextPersonIndex++;
		}
		return nextPerson;
	}

	private List<PersonDto> retrievePersonFromApi() {
		LOGGER.info("Retrieving Person information..");

		HttpEntity<String> request = new HttpEntity<String>(createHeaders());
		
		ResponseEntity<PersonDto[]> response  = restTemplate.exchange
		 (apiUrl, HttpMethod.GET, request, PersonDto[].class);
		
		PersonDto[] personDto = response.getBody();
		return Arrays.asList(personDto);
	}

	private HttpHeaders createHeaders() {		

		HttpHeaders headers = new HttpHeaders();
		headers.setBasicAuth("admin", "admin");
		
		return headers;
	}

}
