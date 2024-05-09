from components.code_transformer import CodeTransformer
from components.instructions import *

input_file_path = "io/input.java"
output_file_path = "io/output.java"
input_class_name = "MemberResourceRESTService"
input_class_type = InstructionType.CONTROLLER

instructions = InstructionType.generate_instructions(input_class_type, input_class_name)
transformer = CodeTransformer()
transformer.transform_code(input_file_path, output_file_path, instructions)
