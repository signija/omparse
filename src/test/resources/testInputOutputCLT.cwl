cwlVersion: cwl:draft-3
class: CommandLineTool
inputs:
  id: message
  type: string
  inputBinding:
    valueFrom: "yo do u like workflows"
outputs:
  id: output
  type: File
  outputBinding:
    glob: test.txt
baseCommand: echo
stdout: test.txt
