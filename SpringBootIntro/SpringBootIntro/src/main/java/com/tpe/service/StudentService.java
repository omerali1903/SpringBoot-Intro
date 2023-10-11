package com.tpe.service;

import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.exception.ConflictException;
import com.tpe.exception.ResourceNotFoundException;
import com.tpe.repository.StudentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {

    @Autowired
    private StudentRepository studentRepository;

    //2-
    public List<Student> getAllStudent() {
        //List<Student> students=studentRepository.findAll();//select * from student;
        //return students;
        return studentRepository.findAll();
    }

    //4-
    public void saveStudent(Student student) {
        //student daha önce kaydedilmiş mi?-->aynı emaile sahip student var mı?
        if(studentRepository.existsByEmail(student.getEmail())){
            throw new ConflictException("Email is already exist!");
        }
        studentRepository.save(student);
    }

//6-
    public Student getStudentById(Long id) {
    Student student =    studentRepository.findById(id).orElseThrow(()->new ResourceNotFoundException("Student not found by id : "+id));
    return student;

    }

    public void deleteStudent(Long id) {
        //bu id ye sahip bir student var mi?
      Student foundStudent =  getStudentById(id);
        studentRepository.delete(foundStudent);
    }

    public void updateStudent(Long id, StudentDTO studentDTO) {
        //gelen id ile öğrenci var mı? varsa bulalım
        Student foundStudent=getStudentById(id);

        //studentDTO.getEmail() zaten daha önceden DB de varsa??
        boolean existsEmail=studentRepository.existsByEmail(studentDTO.getEmail());

        //existsEmail true ise bu email başka bir studentın olabilir, studentın kendi emaili olabilir??
        // id:3 student email:a@email.com
        //dto                student
        //b@email.com        id:4 b@email.com->existsEmail:true--->exception
        //c@email.com        DB de yok-------->existsEmail:false--update:OK
        //a@email.com        id:3 a@email.com ->existsEmail:true--update:OK


        if(existsEmail && !foundStudent.getEmail().equals(studentDTO.getEmail())){
            throw new ConflictException("Email already exists!!!");
        }

        foundStudent.setName(studentDTO.getName());
        foundStudent.setLastName(studentDTO.getLastName());
        foundStudent.setGrade(studentDTO.getGrade());
        foundStudent.setPhoneNumber(studentDTO.getPhoneNumber());
        foundStudent.setEmail(studentDTO.getEmail());

        studentRepository.save(foundStudent);//saveOrUpdate gibi islem yapar

    }

    public Page<Student> getAllStudentPaging(Pageable pageable) {
     return studentRepository.findAll(pageable);

    }

    //14-

    public List<Student> getAllStudentByLastName (String lastname){


    }
}