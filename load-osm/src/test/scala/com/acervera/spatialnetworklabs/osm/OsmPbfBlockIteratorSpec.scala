package com.acervera.spatialnetworklabs.osm

import java.io._

import org.apache.commons.codec.digest.DigestUtils
import org.scalatest.{WordSpec, FunSpec}


class OsmPbfBlockIteratorSpec extends WordSpec {

  
  "The PbfRawIteratorSpec should" should {
    "Extract rigth raw data" in {
      val testFile = "load-osm/src/test/resources/com/acervera/spatialnetworklabs/osm/three_blocks.osm.pbf"
      val checksum = List("70743dca7a2f784e6d21b81a9bd8e722", "32a1eb67e05faf69f85854678a3041bb", "925b786a3cd34751dfb73b73e383550e")
      var counter = 0
      var pbfIS: InputStream = null
      try {
        pbfIS = new FileInputStream(testFile)
        PbfRawIterator(pbfIS, rawBlock => {
          val currentChecksum = DigestUtils.md5Hex(rawBlock.raw)
          val expectedCheksum = checksum(counter)
          assert( currentChecksum == expectedCheksum, s"MD5 should be [$expectedCheksum] but it is [$currentChecksum]")
          counter += 1
        })

        assert(counter == 3, "There are 3 blocks!")
      } finally {
        if (pbfIS != null) pbfIS.close()
      }
    }
    
  }

}
