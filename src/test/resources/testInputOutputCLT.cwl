cwlVersion: cwl:draft-3
class: CommandLineTool
inputs:
  - id: message
    type: string
    inputBinding:
      valueFrom: "testing a command line tool"
outputs:
  - id: output
    type: File
    outputBinding:
      glob: test.txt
baseCommand: echo
stdout: test.txt
