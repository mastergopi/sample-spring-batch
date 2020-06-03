package com.person.batch.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.batch.core.Job;
import org.springframework.batch.core.Step;
import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
import org.springframework.batch.core.configuration.annotation.JobBuilderFactory;
import org.springframework.batch.core.configuration.annotation.StepBuilderFactory;
import org.springframework.batch.core.launch.support.RunIdIncrementer;
import org.springframework.batch.item.ItemProcessor;
import org.springframework.batch.item.ItemReader;
import org.springframework.batch.item.ItemWriter;
import org.springframework.batch.item.file.FlatFileItemWriter;
import org.springframework.batch.item.file.transform.BeanWrapperFieldExtractor;
import org.springframework.batch.item.file.transform.DelimitedLineAggregator;
import org.springframework.batch.item.file.transform.FieldExtractor;
import org.springframework.batch.item.file.transform.LineAggregator;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.core.io.FileSystemResource;
import org.springframework.web.client.RestTemplate;

import com.person.batch.constants.PersonBatchConstants;
import com.person.batch.dto.PersonDto;
import com.person.batch.util.StringHeaderWriter;

@Configuration
@EnableBatchProcessing
public class RESTPersonJobConfig {

	private static final Logger LOGGER = LoggerFactory.getLogger(RESTPersonJobConfig.class);	

	@Bean
	ItemReader<PersonDto> restPersonReader(Environment environment, RestTemplate restTemplate) {
		return new RESTPersonReader(environment.getRequiredProperty(PersonBatchConstants.API_URL), restTemplate);
	}

	@Bean
	ItemProcessor<PersonDto, PersonDto> restPersonProcessor() {
		return new RESTPersonProcessor();
	}

	@Bean
	ItemWriter<PersonDto> restPersonWriter() {
		// return new RESTPersonWriter();
		LOGGER.info("enter : RESTPersonJobConfig.restPersonWriter() ");

		FlatFileItemWriter<PersonDto> csvFileWriter = new FlatFileItemWriter<>();

		String exportFileHeader = "ID,FIRST NAME,LAST NAME,GENDER,AGE,FULL NAME";
		StringHeaderWriter headerWriter = new StringHeaderWriter(exportFileHeader);
		csvFileWriter.setHeaderCallback(headerWriter);

		String exportFilePath = PersonBatchConstants.CSV_FILE_PATH;
		csvFileWriter.setResource(new FileSystemResource(exportFilePath));
		csvFileWriter.setAppendAllowed(true);

		LineAggregator<PersonDto> lineAggregator = createPersonLineAggregator();
		csvFileWriter.setLineAggregator(lineAggregator);

		return csvFileWriter;
	}

	private LineAggregator<PersonDto> createPersonLineAggregator() {
		LOGGER.info("enter : RESTPersonJobConfig.createPersonLineAggregator");

		DelimitedLineAggregator<PersonDto> lineAggregator = new DelimitedLineAggregator<>();
		lineAggregator.setDelimiter(PersonBatchConstants.COMMA_SEPARATOR);

		FieldExtractor<PersonDto> fieldExtractor = createStudentFieldExtractor();
		lineAggregator.setFieldExtractor(fieldExtractor);

		LOGGER.info("exit : RESTPersonJobConfig.createPersonLineAggregator");

		return lineAggregator;
	}

	private FieldExtractor<PersonDto> createStudentFieldExtractor() {
		BeanWrapperFieldExtractor<PersonDto> extractor = new BeanWrapperFieldExtractor<>();
		extractor.setNames(new String[] { "id", "firstName", "lastName", "gender", "age", "fullName" });
		return extractor;
	}

	@Bean
	Step apiServiceToCSVFileStep(ItemReader<PersonDto> restPersonReader,
			ItemProcessor<PersonDto, PersonDto> restPersonProcessor, ItemWriter<PersonDto> restPersonWriter,
			StepBuilderFactory stepBuilderFactory) {
		return stepBuilderFactory.get("apiServiceToCSVFileStep").<PersonDto, PersonDto>chunk(1).reader(restPersonReader)
				.processor(restPersonProcessor).writer(restPersonWriter).build();
	}

	@Bean
	Job apiServiceToCSVFileJob(@Qualifier("apiServiceToCSVFileStep") Step apiToCSVPersonStep,
			JobBuilderFactory jobBuilderFactory) {
		return jobBuilderFactory.get("apiServiceToCSVFileJob").incrementer(new RunIdIncrementer())
				.flow(apiToCSVPersonStep).end().build();
	}
}
