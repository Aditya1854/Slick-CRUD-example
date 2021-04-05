package repository
import connection.H2DBComponent
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}
import java.sql.Date

class StudentInfoTableSpec extends AnyFunSuite with ScalaFutures{
  implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds), interval = Span(500, Millis))
  val studentObject =new  StudentInfoRepository with H2DBComponent {}
    test("Create new student") {
      val response = studentObject.create(Student("Abhijit", "abhijit@gmail.com", 1, Date.valueOf("1996-03-05")))
      whenReady(response) { studentId =>
        assert(studentId === 3)
      }
  }
  test("Update Student"){
    val response = studentObject.update(Student("Aditya","aditya@gmail.com",1,Date.valueOf("1995-07-03"),Some(1)))
    whenReady(response){
      isStudentUpdated => assert(isStudentUpdated === 1)
    }
  }
  test("get student By id"){
    val response = studentObject.getById(2)
    whenReady(response){
      studentData =>
        assert(studentData === Some(Student("Aman","aman@gmail.com",1,Date.valueOf("1994-08-03"),Some(2))))
    }
  }
  test("getAll student list"){
    val response = studentObject.getAll()
    whenReady(response){
      studentList => assert(studentList === List(Student("Aditya","aditya@gmail.com",1,Date.valueOf("1995-05-03"),Some(1)),
        Student("Aman","aman@gmail.com",1,Date.valueOf("1994-08-03"),Some(2))))
    }
  }
  test("delete student"){
    val response = studentObject.delete(2)
    whenReady(response){
      isStudentDeleted => assert(isStudentDeleted === 1)
    }
  }

  test("fetch student and their corresponding university"){
    val response = studentObject.joinStudentAndUniversity()
    whenReady(response){studentAndUniversity =>
      assert(studentAndUniversity === List(("Aditya","HCU"),("Aman","HCU")))
    }
  }

  test("fetch university and number of students in it"){
    val response = studentObject.joinUniversityAndNumberOfStudent()
    whenReady(response) {
      universityStudentCount =>  assert(universityStudentCount === List(("HCU",2)))
    }
  }


}
