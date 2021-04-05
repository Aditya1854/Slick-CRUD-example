package repository
import connection.{DBComponent, MySqlDBComponent}
import java.sql.Date
import scala.concurrent.Future

trait StudentInfoRepository extends StudentInfoTable{this:DBComponent=>
  import driver.api._

  def getAll():Future[List[Student]] = {db.run(studentQuery.to[List].result)}

  def create(student:Student):Future[Int] = {db.run(autoIncrementStudentId += student)}

  def update(student:Student):Future[Int] = {db.run(studentQuery.filter(_.id === student.id).update(student))}

  def getById(id: Int): Future[Option[Student]] = {db.run(studentQuery.filter(_.id === id).result.headOption)}

  def delete(id:Int):Future[Int] = {db.run(studentQuery.filter(_.id === id).delete)}

  def joinStudentAndUniversity():Future[Seq[(String,String)]] ={
    val q3 = for {(c, s) <- studentQuery join universityQuery on (_.universityId === _.id)}
    yield (c.name, s.name)
    db.run(q3.result)
  }

  def joinUniversityAndNumberOfStudent():Future[Seq[(String,Int)]] = {
    val res = (for {(s, u) <- studentQuery join universityQuery on (_.universityId === _.id)}
      yield (s, u)).groupBy(_._2.name).map {
      case (university, len) => (university, len.map(_._1.universityId).length)
    }
   db.run(res.result)
  }

}

private[repository] trait StudentInfoTable extends UniversityInfoTable {this: DBComponent =>
  import driver.api._
  private[StudentInfoTable] class StudentTable(tag:Tag) extends Table[Student](tag,"student"){
    def id = column[Int]("id",O.PrimaryKey,O.AutoInc)
    def name = column[String]("name")
    def email = column[String]("email",O.Unique)
    def universityId = column[Int]("university_id")
    def DOB = column[Date]("DOB")
    def * = (name,email,universityId,DOB,id.?).mapTo[Student]
    def fk = foreignKey("SUP_FK", universityId, universityQuery)(_.id)
  }
  protected val studentQuery = TableQuery[StudentTable]
  protected def autoIncrementStudentId = studentQuery returning studentQuery.map(_.id)
}



case class Student(name:String,email:String,universityId :Int,DOB:Date,id:Option[Int]=None)
object StudentRepository extends StudentInfoRepository with MySqlDBComponent