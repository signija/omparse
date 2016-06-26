import fastparse.parsers.Combinators.Rule

object WsApi extends fastparse.WhitespaceApi.Wrapper({
  import fastparse.all._
  ("- " | " ").rep
})

object Parser {
  import fastparse.noApi._
  import WsApi._

  case class Ident(id: String)
  case class Type(value: String)
  case class Input(id: Ident, t: Type, source: InValue) // id, type, valueFrom
  case class Output(id: Ident, t: Type, loc: Location)   // id, type, binding
  case class InValue(in: String)
  case class Location(loc: String)
  
  sealed trait Task
  case class SysTask(task: String) extends Task
  case class SubTask(id: Ident, task: String) extends Task

  sealed trait Doc
  case class Clt(in: Seq[Input], out: Seq[Output], task: SysTask) extends Doc
  case class Wf(in:Seq[Input], out: Seq[Output], task: SubTask) extends Doc


  val identifier = P("id:" ~ (letter ~ character.rep).! ~ "\n".rep).map {
    x => Ident(x)
  }
  val character = P(letter | decDigit)
  val letter = P(CharIn('A' to 'Z', 'a' to 'z'))
  val decDigit = P(CharIn('0' to '9'))

  val typ = P("type:" ~ (letter.rep).! ~ "\n".rep).map {
    x => Type(x)
  }

  val document = P(version ~ content)
  val content : P[Doc] = P(cls ~ inputs ~ outputs ~ tasks).map {
    case ("CommandLineTool", in: Seq[Input], out: Seq[Output], tasks: SysTask) => Clt(in, out, tasks)
    case ("Workflow", in: Seq[Input], out: Seq[Output], tasks: SubTask) => Wf(in, out, tasks)
  }
  val tasks = P(("baseCommand:" ~ base) | ("steps:" ~ step.rep))
  val version = P("cwlVersion:" ~ "cwl:draft-3" ~ "\n") // future: add other versions
  val cls = P("class:" ~ ("CommandLineTool" | "Workflow").! ~ "\n")
  val inputs : P[Seq[Input]] = P("inputs:" ~ "\n" ~ input.rep)
  val outputs : P[Seq[Output]] = P("outputs:" ~ "\n" ~ output.rep)

  val base : P[SysTask] = P(character.rep.! ~ "\n").map {
    x => SysTask(x)
  }
  val step : P[SubTask] = P(identifier ~ "\n" ~ "run:" ~ "\n" ~ (character.rep ~ "." ~ letter.rep).! ~ "\n".rep).map {
    case (id, loc) => SubTask(id, loc)
  }

  val input : P[Input] = P(identifier ~ typ ~ inBound ~ "\n".rep).map {
    case (id: Ident, t: Type, binding: InValue) => Input(id, t, binding)
  }
  val output : P[Output] = P(identifier ~ typ ~ outBound ~ "\n".rep).map {
    case(id: Ident, t: Type, binding: Location) => Output(id, t, binding)
  }

  val inBound : P[InValue] = P("inputBinding:" ~ "\n" ~ "valueFrom:" ~ "\"" ~ character.rep.! ~ "\"").map {
    in => InValue(in)
  }
  val outBound : P[Location]= P("outputBinding:" ~ "\n" ~ "glob:" ~ (character.rep ~ "." ~ letter.rep).!).map {
    out => Location(out)
  }
}
