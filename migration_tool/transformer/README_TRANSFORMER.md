# Migration Tool: Transformer

## Introduction
The purpose of this python project is to support the process of migrating JavaEE projects to Java 21 / Spring Boot 3 stack.

## Description
It communicates with OpenAI API endpoints to send JavaEE source code of a class and gives instructions to ChatGPT 4 turbo engine to convert the code. The communication has multiple iterations with different instructions based on the type of the provided class. The result code must be reviewed and corrected before ready to commit.

## Requirements
- Python 3.11 environment with PIP (package manager) installed
- Working OpenAI API key with license and available credits for the communication

## How to use

### Setup, configuration
- `cd migration_tool` - enter folder
- `python -m venv .venv` - create a new virtual environment for python packages
- `source .venv/bin/activate` - activate the venv
- `cd transformer` - enter folder
- `pip install -r requirements.txt` - install project dependencies to venv
- `export OPENAI_API_KEY=YOUR_API_KEY` - create a EnvVar for your OpenAI API key (this command for Mac)
- `python transform.py DEFAULT Main` - starts the transformer

The transformer reads an input file for the source code and writes the result into a output file on the following path:
- `/transformer/io/input.java`
- `/transformer/io/output.java`

### CLI arguments:
- Class type: the expected class type must be provided as argument. This is required to select the set of instructions for the specific class type to have better result. Available values:
  - DEFAULT
  - CONTROLLER
  - MODEL
  - REPOSITORY
  - SERVICE
- Class name: the name of the class provided in the input.java file. This is required to ensure chatgpt provides the desired class as result.

### Usage, example:
- Copy and paste the code from a JavaEE Service class to the input.java file
- `python transform.py SERVICE YourServiceClassName`

The DEFAULT command is only good for testing purpose, it will only send 1 instruction to gpt and won't provide quality result.
The other commands usually takes 4-5 instruction iterations with ChatGPT.

IMPORTANT NOTE: chatgpt 4 engine is pretty slow to respond, meaning that 1 instruction iteration could take 30-60 seconds.

Logging:
- Every command sent to the API is logged
- No news is good news: until there is no error message the app is waiting for chatgpt to respond
