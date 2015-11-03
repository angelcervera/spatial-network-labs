import java.io.{FileInputStream, InputStream}
import java.nio.file.{Paths, Path, Files}

import com.acervera.spatialnetworklabs.osm.{OsmPbfPrimitives, PbfRawIterator}
import org.apache.commons.codec.digest.DigestUtils
import org.scalatest.WordSpec

class OsmPbfPrimitivesSpec extends WordSpec {

  "OsmPbfPrimitives" should {
    "Extract all primitives groups from a block" in {
      val testFile = "load-osm/src/test/resources/com/acervera/spatialnetworklabs/osm/block_OSMData_1.osm.pbf"

      var nodeCounter = 0

      OsmPbfPrimitives.processPrimitives(
        Files.readAllBytes(Paths.get(testFile)),
        processNode => {
          nodeCounter += 1
        },
        processWay => {
        },
        processRelation => {
        }
      )

      println(nodeCounter)

    }
  }
}