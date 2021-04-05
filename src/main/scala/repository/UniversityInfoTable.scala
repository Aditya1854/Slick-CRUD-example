package repository
import connection.{DBComponent, MySqlDBComponent}
import scala.concurrent.Future
trait UniversityInfoRepository extends UniversityInfoTable{this:DBComponent =>
  import driver.api._

  def getAll():Future[List[University]] = {db.run(universityQuery.to[List].result)}

  def create(university:University):Future[Int] = {db.run(universityQuery += university)}

  def update(university:University):Future[Int] = {db.run(universityQuery.filter(_.id === university.id).update(university))}

  def getById(id: Int): Future[Option[University]] = {db.run (universityQuery.filter(_.id === id).result.headOption)}

  def delete(id:Int):Future[Int] = {db.run(universityQuery.filter(_.id === id).delete)}

}
private[repository] trait UniversityInfoTable {this:DBComponent=>
  import driver.api._
   private[UniversityInfoTable] class UniversityTable(tag : Tag) extends Table[University](tag, "university"){
    def id = column[Int]("id",O.PrimaryKey)
    def name = column[String]("name")
    def location = column[String]("location")
    def * = (id,name,location).mapTo[University]
  }
  protected val universityQuery = TableQuery[UniversityTable]
}
case class University( id:Int,name:String,location:String)
object UniversityRepository extends UniversityInfoRepository with MySqlDBComponent