package tw.dictionary.api.parser.utils
import java.io.BufferedReader
import java.io.FileWriter
import java.io.FileReader

object CSVPretreatmenter {
  def handleLines(srcPath: String, destPath: String)(handler: (String) => String) = {
    val reader = new BufferedReader(new FileReader(srcPath))
    val writer = new FileWriter(destPath, false)

    def handleAllLine(reader: BufferedReader, writer: FileWriter, handler: (String) => String): Unit = {
      val line = reader.readLine()
      if (line != null) {
        writer.write(handler(line))
        handleAllLine(reader, writer, handler)
      }
    }

    handleAllLine(reader, writer, handler)
  }

  /*
  def main(args: Array[String]) {
    handleLines("./data/GEPTmediumhigh.csv", "./data/GEPTmediumhigh.txt") {
      line =>
        try {
          line.split(",").first + "\n"
        } catch {
          case _ => ""
        }
    }
  }
  */
}