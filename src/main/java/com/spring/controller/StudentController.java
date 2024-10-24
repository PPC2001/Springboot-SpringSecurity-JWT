package com.spring.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import com.spring.model.Student;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
public class StudentController {

	private static final Logger logger = LoggerFactory.getLogger(StudentController.class);

	private List<Student> students = new ArrayList<>(List.of(new Student(1, "Navin", 60), new Student(2, "Kiran", 65)));

	@GetMapping("/students")
	public ResponseEntity<?> getStudents() {
		logger.info("StudentController :: Fetching list of students");
		try {
			return new ResponseEntity<>(students, HttpStatus.OK);
		} catch (Exception e) {
			logger.error("Error occurred while fetching students", e);
			return new ResponseEntity<>("Unable to fetch students at the moment", HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
}