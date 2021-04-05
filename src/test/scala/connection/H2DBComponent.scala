package connection

import org.slf4j.LoggerFactory

import java.util.UUID

trait H2DBComponent extends DBComponent {

  val logger = LoggerFactory.getLogger(this.getClass)

  val driver = slick.jdbc.H2Profile

  import driver.api._

  val randomDB = "jdbc:h2:mem:test" + UUID.randomUUID().toString() + ";"

  val h2Url = randomDB + "MODE=MySql;DATABASE_TO_UPPER=false;INIT=runscript from 'src/test/resources/Schema.sql'\\;runscript from 'src/test/resources/SchemaData.sql'"

  val db: Database = {
    logger.info("Creating test connection ..................................")
    Database.forURL(url = h2Url, driver = "org.h2.Driver")
  }

}
