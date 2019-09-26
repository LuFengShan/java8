package com.java8.csv;

import com.opencsv.CSVWriter;
import com.opencsv.bean.ColumnPositionMappingStrategy;
import com.opencsv.bean.StatefulBeanToCsv;
import com.opencsv.bean.StatefulBeanToCsvBuilder;
import com.opencsv.exceptions.CsvDataTypeMismatchException;
import com.opencsv.exceptions.CsvRequiredFieldEmptyException;
import org.junit.jupiter.api.Test;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OpenCsvDemo {
	private static final String[] CSV_HEADER = {"id", "name", "address", "age"};
	private static List<Customer> customers = new ArrayList<>(400000);

	static {
		for (int i = 0; i < 2000000; i++) {
			customers.add(new Customer(String.valueOf(i + 1), "Katherin Carter", "Washington DC", 26));
		}
	}

	@Test
	public void testWriterCsvByStringArray() {
		FileWriter fileWriter = null;
		CSVWriter csvWriter = null;
		try {
			fileWriter = new FileWriter("d:/customer-array.csv");

			// write String Array
			csvWriter = new CSVWriter(fileWriter,
					CSVWriter.DEFAULT_SEPARATOR,
					CSVWriter.NO_QUOTE_CHARACTER,
					CSVWriter.DEFAULT_ESCAPE_CHARACTER,
					CSVWriter.DEFAULT_LINE_END);

			csvWriter.writeNext(CSV_HEADER);

			for (Customer customer : customers) {
				String[] data = {
						customer.getId(),
						customer.getName(),
						customer.getAddress(),
						String.valueOf(customer.getAge())};

				csvWriter.writeNext(data);
			}

			System.out.println("Write CSV using CSVWriter successfully!");
		} catch (IOException e) {
			System.out.println("Writing CSV error!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
				//csvWriter.close();
			} catch (IOException e) {
				System.out.println("Flushing/closing error!");
				e.printStackTrace();
			}
		}

	}

	@Test
	public void testWriterCsvByObject() {
		FileWriter fileWriter = null;
		StatefulBeanToCsv<Customer> beanToCsv;
		try {
			fileWriter = new FileWriter("d:/customerList.csv");

			// write List of Objects
			ColumnPositionMappingStrategy<Customer> mappingStrategy =
					new ColumnPositionMappingStrategy<>();

			mappingStrategy.setType(Customer.class);
			mappingStrategy.setColumnMapping(CSV_HEADER);

			beanToCsv = new StatefulBeanToCsvBuilder<Customer>(fileWriter)
					.withMappingStrategy(mappingStrategy)
					.withQuotechar(CSVWriter.NO_QUOTE_CHARACTER)
					.build();

			beanToCsv.write(customers);

			System.out.println("Write CSV using BeanToCsv successfully!");
		} catch (IOException | CsvDataTypeMismatchException | CsvRequiredFieldEmptyException e) {
			System.out.println("Writing CSV error!");
			e.printStackTrace();
		} finally {
			try {
				fileWriter.flush();
				fileWriter.close();
			} catch (IOException e) {
				System.out.println("Flushing/closing error!");
				e.printStackTrace();
			}
		}

	}
}
