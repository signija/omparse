import Parser._
import fastparse.all._

object Translator {

  def translate(parsedDocument: Parsed[Doc]): String = {
    val result = new StringBuilder

    val ast = parsedDocument match {
      case Parsed.Success(tree, index) => tree.asInstanceOf[Clt]
      case f: Parsed.Failure => throw new Exception(f.extra.traced.fullStack.mkString("\n"))
    }

    val inputs = ast.in
    val outputs = ast.out
    val task = ast.task

    val input_values = new Array[String](inputs.length)
    var count_input = 0

    for (input <- inputs) {
      result ++= "val " + translateId(input.id) + " = Val[" + translateType(input.t) + "]"
      input_values(count_input) = input.source.in
    }
    result += '\n'
    var count_output = 0
    for (output <- outputs) {
      result ++= "val outputTemp" + count_output + " = Val[String]"
      result += '\n'
      result ++= "val " + translateId(output.id) + " = Val[" + translateType(output.t) + "]"
      result += '\n'
      count_output += 1
    }

    result ++= translateSystemTask(task, input_values(0))
    result += '\n'
    result ++= translateFileHook("outputTemp0")
    result += '\n'
    result ++= translateFinalise("task", "fileHook")

    return result.toString
  }

  def translateId(identifier: Ident): String = {
    return identifier.id
  }

  def translateType(typ: Type): String = {
    val t = typ.value
    val res = t match {
      case "string" => "String"
      case "file" => "File"
      case _ => t
    }
    return res
  }

  def translateSystemTask(task: SysTask, in: String): String = {
    val res = new StringBuilder
    res ++= "val task = SystemExecTask("
    res += '\n'
    res ++= """ " """ + task.task + """ ${message}" """
    res += '\n'
    res ++= ")set("
    res += '\n'
    res ++= "(inputs, outputs) += message,"
    res += '\n'
    res ++= "stdOut := outputTemp0,"
    res += '\n'
    res ++= """message := """" + in + """" """
    res += '\n'
    res ++= ")"
    return res.toString
  }

  def translateFileHook(hook: String): String = {
    val res = """val fileHook = AppendToFileHook("/tmp/test.txt", "${""" + hook + """}")"""
    return res
  }

  def translateFinalise(task: String, hook: String): String = {
    val res = task + " hook " + hook
    return res
  }

}
