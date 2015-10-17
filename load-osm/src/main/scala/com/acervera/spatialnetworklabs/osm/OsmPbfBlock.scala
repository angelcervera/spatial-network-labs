package com.acervera.spatialnetworklabs.osm

import java.io.DataInput
import org.openstreetmap.osmosis.osmbinary.Fileformat
import java.nio.ByteBuffer
import java.io.DataOutput

object OsmPbfBlock {
  
  def apply(pbfStream:DataInput) : OsmPbfBlock = {
    // Reading header.
    val blobHeader = new Array[Byte](pbfStream.readInt)
    pbfStream.readFully(blobHeader)

    // Reading message
    val blob = new Array[Byte]( Fileformat.BlobHeader.parseFrom(blobHeader).getDatasize )
    pbfStream.readFully(blob)

    // Create the object
    OsmPbfBlock(blobHeader, blob)
  }
  
}

case class OsmPbfBlock(header : Array[Byte], blob : Array[Byte]) {
  
  /**
   * Regenerate the original raw block.
   *
   * @return
   */
  def raw(out : DataOutput) = {
    out.writeInt(header.length)
    out.write(header)
    out.write(blob)
  }
  
  /**
   * Regenerate the original raw block.
   *
   * @return
   */
  def raw : Array[Byte] = {
    ByteBuffer.allocate(4 + header.length + blob.length)
      .putInt(header.length)
      .put(header)
      .put(blob)
      .array()
  }
  
}