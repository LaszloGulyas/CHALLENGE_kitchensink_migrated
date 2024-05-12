from enum import Enum

INSTRUCTION_CONVERT_CODE_TO_SPRING = "Convert the below java EE class to a Java 21 and Spring boot 3 compatible class.\n"
INSTRUCTION_USE_LOMBOK_FOR_CONSTRUCTORS = "Use lombok for constructors"
INSTRUCTION_USE_LOMBOK_FOR_LOGGING = "Use lombok for logging with slf4j"
INSTRUCTION_USE_JPA_REPOS = "Use JPA repositories for data management if required, ensure transactional applied where needed"
INSTRUCTION_USE_JPA_FOR_MODELS = "Ensure model can be handled by JPA hibernate and preserve validations"
INSTRUCTION_ENSURE_JAKARTA_DEPENDENCIES = "Ensure that dependencies are using jakarta instead of javax"
INSTRUCTION_PRINT_ONLY_CODE = "Print the result code of this class and embed it into ```java ``` bracket: "


class InstructionType(Enum):
    DEFAULT = 0
    CONTROLLER = 1
    MODEL = 2
    REPOSITORY = 3
    SERVICE = 4

    @staticmethod
    def generate_instructions(instruction_type, input_class_name):
        if instruction_type == InstructionType.DEFAULT:
            return None
        if instruction_type == InstructionType.CONTROLLER:
            return [INSTRUCTION_USE_LOMBOK_FOR_CONSTRUCTORS + " and " + INSTRUCTION_USE_LOMBOK_FOR_LOGGING]
        elif instruction_type == InstructionType.MODEL:
            return [INSTRUCTION_USE_JPA_FOR_MODELS,
                    INSTRUCTION_USE_LOMBOK_FOR_CONSTRUCTORS,
                    INSTRUCTION_ENSURE_JAKARTA_DEPENDENCIES,
                    INSTRUCTION_PRINT_ONLY_CODE + input_class_name]
        elif instruction_type == InstructionType.REPOSITORY:
            return [INSTRUCTION_USE_JPA_REPOS]
        elif instruction_type == InstructionType.SERVICE:
            return [INSTRUCTION_USE_JPA_REPOS,
                    INSTRUCTION_USE_LOMBOK_FOR_CONSTRUCTORS + " and " + INSTRUCTION_USE_LOMBOK_FOR_LOGGING,
                    INSTRUCTION_PRINT_ONLY_CODE + input_class_name]

    @classmethod
    def from_name(cls, name):
        for instruction_name, instruction_value in cls._member_map_.items():
            if instruction_name == name:
                return instruction_value
        return None
