# Formatter.py
# Author: Carl Masri
import re
# -------------------------------------------------------------- #
class WrongTypeException(Exception):
	""" this exception is thrown when a command is given an innapropriate value """
	def __init__(self, value):
		self.value = value
# -------------------------------------------------------------- #
class EmptyFileException(Exception):
	""" this exception is thrown when an empty file is given as input """
	def __init__(self, value):
		self.value = value
# -------------------------------------------------------------- #
class Formatter:
	def __init__(self, input = ["?width 20", "This is a sentence that is generated as default input."]):
		""" constructor taking a list as a parameter """
		# initialize instance variables using a dictionary
		self.cmd = {'width': 0, 'mrgn': 0, 'fmt': False}
		# keeps track of line size and previous newlines
		self.line = {'size': 0, 'new': False}
		# gets input from script, else defaults to list as input
		self.input = input
		# stores output as a list of strings
		self.output = []
		# class method that will parse each line of input
		self.parse_lines()
# -------------------------------------------------------------- #
	def parse_lines(self):
		""" sends each line of output to be parsed and adds result to output """		
		# for each line of input
		for line in self.input:
			# send line to be parsed
			input_line = self.parse(line)
			# if there is an input line
			if input_line:
				# send formatted line to output
				self.output.extend([input_line])
# -------------------------------------------------------------- #
	def parse(self, line):
		""" parses and formats lines of input """
		# reads command from line using regex
		command = re.match(r"\?(mrgn|fmt|width) (\-|\+)?(\d+|on|off)\s*$", line)
		
		# if there is a command
		if command:
			the_command = command.groups()
			
			# if the command is fmt
			if the_command[0] == 'fmt':
				# if fmt is on
				if the_command[2] == 'on':
					# turn on formatting
					self.cmd['fmt'] = True
				# else if fmt is off
				elif the_command[2] == 'off':
					# turn off formatting
					self.cmd['fmt'] = False
				# elif fmt was called with something other than on/off
				else:
					raise WrongTypeException("Format was given an innapropriate value. This occurred here: " + line)
			
			# else if the command is width
			elif the_command[0] == 'width':
				self.cmd['width'] = int(the_command[2])
				self.cmd['fmt'] = True
			
			# else if the command is margin
			elif the_command[0] == 'mrgn':
				if the_command[1] == '+':
					self.cmd[the_command[0]] += int(the_command[2])
				elif the_command[1] == '-':
					self.cmd[the_command[0]] -= int(the_command[2])
				else:
					self.cmd[the_command[0]] = int(the_command[2])
			
			# margin error handling
			if self.cmd['mrgn'] < 0:
				# raise ValueError("Error: the margins were set to less than 0. The current margin is
				# + margin and the file has attempted to subtract + self.cmd['mrgn']. This occurs on line: + line")
				self.cmd['mrgn'] = 0
			if self.cmd['mrgn'] > self.cmd['width'] - 20:
				# raise ValueError("Error: the margins were set too large. The smallest width possible is 20,
				# but the attempted margin exceeded the minimum width. This occurred on line: + line")
				self.cmd['mrgn'] = self.cmd['width'] - 20
				if self.cmd['mrgn'] < 0:
					self.cmd['mrgn'] = 0
		
			# clears command line so it will not be sent to output
			line = None
				
		# if there is a line and formatting is on
		if self.cmd['fmt'] and line:
			# split the line
			splitlines = line.split()
			# if the line is empty
			if splitlines == []:
				# set line size to 0
				self.line['size'] = 0
				# if the last line was a new line
				if self.line['new']:
					return '\n'
				# last was not a new line, add an extra newline
				else:
					self.line['new'] = True
					return '\n\n'
			self.line['new'] = False
			
			output = ""
			
			# if it is a new line, add margin
			if self.line['size'] == 0:
				output = " " * self.cmd['mrgn']
				self.line['size'] = self.cmd['mrgn']
			
			# iterates through words and adds them to output
			for word in splitlines:
				# if the next word will go over the allowed width
				if self.line['size'] + len(word) >= self.cmd['width']:
					# add newline and margin, set new line size
					margin = " " * self.cmd['mrgn']
					output += '\n' + margin
					self.line['size'] = self.cmd['mrgn']
				# add space after word
				elif self.line['size'] != self.cmd['mrgn']:
					self.line['size'] += 1
					output += " "
				# concatenates word onto output string
				output += word
				# records new line length
				self.line['size'] += len(word)
			return output
		else:
			# else formatting is off, return unchanged line
			return line
# -------------------------------------------------------------- #
	def get_lines(self):
		""" returns the formatted lines """
		return self.output
