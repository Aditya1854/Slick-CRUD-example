package repository
import connection.H2DBComponent
import org.h2.jdbc.JdbcSQLException
import org.scalatest.funsuite.AnyFunSuite
import org.scalatest.concurrent.ScalaFutures
import org.scalatest.time.{Millis, Seconds, Span}

import java.sql.Date

class UniversityInfoTableSpec extends AnyFunSuite with ScalaFutures{
  implicit val defaultPatience = PatienceConfig(timeout = Span(10, Seconds), interval = Span(500, Millis))
  val universityObject =new  UniversityInfoRepository with H2DBComponent
  test("getAll university list"){
    val response = universityObject.getAll()
    whenReady(response){
      universityList => assert(universityList === List(University(1,"HCU","Hyderabad")))
    }
  }
  test("create university"){
    val response = universityObject.create(University(2,"BHU","varanasi"))
    whenReady(response){
      universityId => assert(universityId === 1)
    }
  }
  test("update university"){
    val response = universityObject.update(University(1,"HCU","Gachibowli"))
    whenReady(response){
      isUniversityUpdated => assert(isUniversityUpdated === 1)
    }
  }
  test("deleted university"){
      val response = universityObject.delete(1)
     whenReady(response.failed){
       isUniversityDeleted =>
         intercept[JdbcSQLException]{
           throw  isUniversityDeleted
         }
     }

  }
  test("getUniversity By Id"){
    val response = universityObject.getById(1)
    whenReady(response){
      universityData => assert(universityData === Some(University(1,"HCU","Hyderabad")))
    }
  }

}
