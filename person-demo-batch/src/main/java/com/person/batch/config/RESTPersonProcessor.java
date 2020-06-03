package com.person.batch.config;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.item.ItemProcessor;

import com.person.batch.dto.PersonDto;

public class RESTPersonProcessor implements ItemProcessor<PersonDto, PersonDto>{

    private static final Logger LOGGER = LoggerFactory.getLogger(RESTPersonProcessor.class);

	@Override
	public PersonDto process(PersonDto item) throws Exception {
		LOGGER.info("Processing student information: {}", item);
		return item;
	}

}
