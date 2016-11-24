import scala.io.Source._
import java.io._
import Parser._
import Translator._

object Main {

  def main(args: Array[String]): Unit = {
    val source = fromFile(args(0).toString)
    val lines = try source.mkString finally source.close()

    val file = new File("intermediate.txt")
    val bw = new BufferedWriter(new FileWriter(file))
    val ast = Parser.document.parse(lines)
    bw.write(ast.toString)
    bw.close()

    val code = Translator.translate(ast)

    val fileFinal = new File("example.oms")
    val bwFinal = new BufferedWriter(new FileWriter(fileFinal))
    bwFinal.write(code)
    bwFinal.close()
  }

}
