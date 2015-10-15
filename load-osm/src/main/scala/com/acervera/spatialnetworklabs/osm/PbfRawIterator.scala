package com.acervera.spatialnetworklabs.osm

import java.io._
import org.openstreetmap.osmosis.osmbinary.Fileformat

/**
 * Factory to work with PbfRawIterator.
 *
 * @author angelcervera
 */
object PbfRawIterator {

  /**
   * Create a new PbfRawIterator iterator.
   *
   * @param pbfInputStream
   * @return
   */
  def apply(pbfInputStream: InputStream) = new PbfRawIterator(pbfInputStream)

  /**
   * Iterate over all blocks using the visit function.
   *
   * @param pbfInputStream
   * @param visitFn
   */
  def apply(pbfInputStream: InputStream, visitFn: (Array[Byte]) => Unit): Unit = {
    PbfRawIterator(pbfInputStream) foreach (blob => visitFn(blob))
  }

}

/**
 * Iterator over a OSM file in pbf format.
 * The value is the raw of one block.
 *
 * @author angelcervera
 */
class PbfRawIterator(pbfInputStream: InputStream) extends Iterator[Array[Byte]] {

  // Read the input stream using DataInputStream to access easily to Int and raw fields.
  val pbfStream = new DataInputStream(pbfInputStream)

  // Temporal storage for the next block.
  var nextRawBlock: Array[Byte] = null

  // Flag the EOF.
  var eof: Boolean = false

  // Read the first block
  readNextBlock

  // Read the next osm pbf block
  private def readNextBlock() = {

    try {

      // Reading header.
      val nextRawBlobHeader = new Array[Byte](pbfStream.readInt)
      pbfStream.readFully(nextRawBlobHeader)

      // Reading message
      val nextRawBlobMessage = new Array[Byte](Fileformat.BlobHeader.parseFrom(nextRawBlobHeader).getDatasize)
      pbfStream.readFully(nextRawBlobMessage)

      // Regenerate the block
      nextRawBlock = Array(nextRawBlobHeader.length.toByte) ++ nextRawBlobHeader ++ nextRawBlobMessage

    } catch {
      case e: EOFException => {
        eof = true
      }
    }

  }

  override def hasNext: Boolean = {
    !eof
  }

  override def next(): Array[Byte] = {
    val returnBlob = nextRawBlock
    readNextBlock

    returnBlob
  }
}
