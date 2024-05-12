import sys

from components.code_transformer import CodeTransformer
from components.instructions import *

if __name__ == "__main__":
    invalid_argument_error_msg = "Usage: python transformer.py <class_type:DEFAULT|CONTROLLER|MODEL|REPOSITORY|SERVICE> <class_name>"
    input_file_path = "io/input.java"
    output_file_path = "io/output.java"

    if len(sys.argv) != 3:
        print(invalid_argument_error_msg)
        sys.exit(1)

    arg_class_type = sys.argv[1]
    arg_class_name = sys.argv[2]

    instruction_type = InstructionType.from_name(arg_class_type)
    if instruction_type is None:
        print(invalid_argument_error_msg)
        sys.exit(1)

    instructions = InstructionType.generate_instructions(arg_class_type, arg_class_name)
    transformer = CodeTransformer()
    transformer.transform_code(input_file_path, output_file_path, instructions)
