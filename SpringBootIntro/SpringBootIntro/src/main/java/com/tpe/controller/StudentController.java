package com.tpe.controller;


import com.tpe.domain.Student;
import com.tpe.dto.StudentDTO;
import com.tpe.service.StudentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//@Controller
@RestController
@RequestMapping("/students")//http://localhost:8080/students
public class StudentController {

    @Autowired
    private StudentService studentService;

    //spring boot u selamlama:)
    //http://localhost:8080/students/greet+GET
    @GetMapping("/greet")
    public String greet() {
        return "Hello Spring Boot";
    }

    //1-tüm studentları listeleyelim:READ
    //http://localhost:8080/students + GET
    @GetMapping
    public ResponseEntity<List<Student>> listAllStudents() {
        List<Student> studentList = studentService.getAllStudent();
        //return new ResponseEntity<>(studentList, HttpStatus.OK);//200
        return ResponseEntity.ok(studentList);//200
    }
    //response:body(data)+HTTP status code
    //ResponseEntity: response bodysi ile birlikte HTTP status code nu göndermemizi sağlar.
    //ResponseEntity.ok() metodu HTTP status olarak OK yada 200 dönmek için bir kısayoldur.

    //3-yeni bir student CREATE etme
    //http://localhost:8080/students + POST + RequestBody(JSON)
    @PostMapping
    public ResponseEntity<Map<String, String>> createStudent(@Valid @RequestBody Student student) {

        studentService.saveStudent(student);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Student is created successfully");
        response.put("status", "success");
        return new ResponseEntity<>(response, HttpStatus.CREATED);//201
    }
//@RequestBody:HTTP requestin bodysindeki JSON formatındaki bilgiyi student objesine mapler.
// (Entity obje<->JSON)-->Jackson

    //5-belirli bir id ile studentı görüntüleyelim
    //http://localhost:8080/students + GET

    @GetMapping("/query")
    public ResponseEntity<Student> getStudent(@RequestParam("id") Long id){
        Student student=studentService.getStudentById(id);
        return new ResponseEntity<>(student,HttpStatus.OK);//200
    }

    //clienttan bilgi almak için:JSON formatında request body
    //                           :request param query?id=1
    //                           :path param /1

    //5'-belirli bir id ile studentı görüntüleyelim +Path Param
    //http://localhost:8080/students/1 + GET
    @GetMapping("/{id}")
    public ResponseEntity<Student> getStudentById(@PathVariable("id") Long id){
        Student student=studentService.getStudentById(id);
        return new ResponseEntity<>(student,HttpStatus.OK);//200
    }


    //7-belirli bir id ile studentı silelim
//http://localhost:8080/students/1
    @DeleteMapping("/{id}")
    public ResponseEntity<Map<String,String>> deleteStudent(@PathVariable Long id){

        studentService.deleteStudent(id);

        Map<String,String> response = new HashMap<>();
        response.put("message","Student is deleted successfully");
        response.put("status","success");
        return ResponseEntity.ok(response);//200
    }

    //9-belirli bir id ile studenti update edelim.(name,lastName,grade,email,phoneNumber)
    //http://localhost:8080/students/1 +  UPDATE + JSON

    @PutMapping("/{id}")
    public ResponseEntity<Map<String,String>> updateStudent(@PathVariable("id")Long id,
                                                            @Valid @RequestBody StudentDTO studentDTO){

        studentService.updateStudent(id,studentDTO);

        Map<String,String> response = new HashMap<>();
        response.put("message","Student is updated successfully");
        response.put("status","success");
        return ResponseEntity.ok(response);
    }

    //11-pagination-sayfalandırma
    //tüm kayıtları page page listeleyelim
    //http://localhost:8080/students/page?
    //                               page=1&
    //                               size=10&
    //                               sort=name&
    //                               direction=DESC + GET
    @GetMapping("/page")
    public ResponseEntity<Page<Student>> getAllStudentByPage(@RequestParam("page") int page,//hangi page gösterilsin
                                                             @RequestParam("size") int size,//kaç kayıt
                                                             @RequestParam("sort") String prop,//hangi fielda göre
                                                             @RequestParam("direction") Sort.Direction direction)//sıralama yönü
    {
        Pageable pageable= PageRequest.of(page,size,Sort.by(direction,prop));
            Page<Student> studentsByPage=studentService.getAllStudentPaging(pageable);
        return new ResponseEntity<>(studentsByPage,HttpStatus.OK);
    }

    //13-lastName ile studentlari listeleyim.
    // //http://localhost:8080/students/querylastname?lastName=Bey
    //13-lastName ile studentları listeleyelim. **SIRA SİZDE**
    ////http://localhost:8080/students/querylastname?lastName=Bey
    @GetMapping("/querylastname")
    public ResponseEntity<List<Student>> getAllStudentsByLastName(@RequestParam("lastName") String lastname){

        List<Student> studentList=studentService.getAllStudentByLastName(lastname);

        return ResponseEntity.ok(studentList);
    }

    }















