package com.cst438.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.cst438.domain.FinalGradeDTO;
import com.cst438.domain.Enrollment;
import com.cst438.domain.EnrollmentDTO;
import com.cst438.domain.EnrollmentRepository;

@Service
@ConditionalOnProperty(prefix = "gradebook", name = "service", havingValue = "rest")
@RestController
public class GradebookServiceREST implements GradebookService {

	private RestTemplate restTemplate = new RestTemplate();

	@Value("${gradebook.url}")
	private static String gradebook_url;

	@Override
	public void enrollStudent(String student_email, String student_name, int course_id) {
	    System.out.println("Start Message "+ student_email +" " + course_id);

	    
	    EnrollmentDTO enrollmentDTO = new EnrollmentDTO(0, student_email, student_name, course_id);

	    // Send the EnrollmentDTO object as a POST request to the Gradebook backend
	    String url = gradebook_url + "/enrollment";
	    restTemplate.postForObject(url, enrollmentDTO, EnrollmentDTO.class);
	}


	
	@Autowired
	EnrollmentRepository enrollmentRepository;
	/*
	 * endpoint for final course grades
	 */
	@PutMapping("/course/{course_id}")
	@Transactional
	public void updateCourseGrades(@RequestBody FinalGradeDTO[] grades, @PathVariable("course_id") int course_id) {
	    System.out.println("Grades received " + grades.length);

	    for (FinalGradeDTO gradeDTO : grades) {
	        // Fetch Enrollment using studentEmail and courseId
	        Enrollment enrollment = enrollmentRepository.findByEmailAndCourseId(gradeDTO.studentEmail(), gradeDTO.courseId());
	        
	        if (enrollment != null) {
	            // Update the course grade
	            enrollment.setCourseGrade(gradeDTO.grade());
	            enrollmentRepository.save(enrollment);
	        } else {
	            System.out.println("No enrollment found for student " + gradeDTO.studentEmail() + " in course " + gradeDTO.courseId());
	        }
	    }
	}


}
