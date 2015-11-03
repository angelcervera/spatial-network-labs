package com.acervera.spatialnetworklabs.osm

import java.io.{DataInputStream, ByteArrayInputStream, DataInput, DataOutput}
import org.openstreetmap.osmosis.osmbinary.Fileformat
import java.nio.ByteBuffer

object OsmPbfBlock {
  
  def apply(pbfStream:DataInput) : OsmPbfBlock = {
    // Reading header.
    val blobHeader = new Array[Byte](pbfStream.readInt)
    pbfStream.readFully(blobHeader)

    // Reading message
    val headerParsed = Fileformat.BlobHeader.parseFrom(blobHeader);
    val blob = new Array[Byte](headerParsed.getDatasize)
    pbfStream.readFully(blob)

    // Create the object
    OsmPbfBlock(headerParsed.getType, blobHeader, blob)
  }

  def apply(rawBlock: Array[Byte]): OsmPbfBlock = {
    apply(new DataInputStream(new ByteArrayInputStream(rawBlock)))
  }
  
}

case class OsmPbfBlock(blockType: String, header: Array[Byte], blob: Array[Byte]) {

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