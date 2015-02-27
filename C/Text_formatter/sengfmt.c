#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define MAX_LINE_LEN 133 /* max 132 char per line plus one extra for the new line char */
#define MAX_LINES 300 /* max 300 lines per input file  */

/* Initializes arrays */
char input[MAX_LINE_LEN];
char unformatted[MAX_LINE_LEN];
char word[MAX_LINE_LEN];
char output[MAX_LINE_LEN] = "\0";	 /* Declares empty array */

/* Functions */
void parameters(FILE *);
void add_margin(char[]);

/* Variables */
int width;
int margin;
int fmt;

/* ------------------------------------------------------------------------------ */

/* argc is the count of input arguments
argv is a pointer to the input arguments */

int main (int argc, char *argv[])
{
    /* Creates file pointers */
    FILE *fp = fopen (argv[1], "r"); /* r for read */
    FILE *f = fopen (argv[1], "r");

    if (!fp) /* Error checking */
    {
    	printf ("Error: Could not open file");
    	return 0;
    }
	
    /* Retrieves width and margin parameters from input file */
    /* Required second file pointer */
	parameters (f);

    /* Initalizes pointer to traverse arrays */
    char *p;

    /* While there is a new line */
    while (fgets(input, MAX_LINE_LEN, fp))
    {
		/* Saves unformatted line of text */
		strcpy(unformatted, input);

		/* If the line is empty, print a new line */
		if (input[0] == '\n')
		{
			printf("\n");
		}

		/* The line is not empty */
		else
		{
			/* Tokenizing deliniated by whitespace */
			p = strtok(input, "\t ");
				
			/* While there is a next word */
			while (p)
			{
				/* If formatting is on, copy the tokenized word into tokens array */
				if (fmt != 0)
				{ 
					/* This loop eliminates formatting commands */
					while (input[0] == '?')
					{
						fgets(input, MAX_LINE_LEN, fp);
						p = strtok(input, "\t ");
					}
					
					/* Scans input line into word array */
					sscanf(p, "%s", word);
					
					/* Line length */
					int length = strlen(output);	
					
					/* If the length of the line is 0, it is the start
					   of a new line and a margin must be added */
					if (length == 0)
					{
						add_margin(output);
					}

					/* Adds spaces after each word */
					strcat(word, " ");

					/* Word length */
					int word_length = strlen(word);

					/* If the next word to be added doesn't go over the width */
					/* Must account for spaces which are included in the length */
					if (length + word_length < width+2)
					{
						/* Add the next word to output */
						strcat(output, word);
					}
					/* Width is exceeded by next word, push word onto new line */
					else
					{
						strcat(output, "\n");		/* Ends current line */
						printf("%s", output);		/* Prints output */
						strcpy(output, "\0");		/* Nullifies output array for next line */
						add_margin(output);			/* New line, margin must be added */
						strcat(output, word);		/* Add word from previous line to new line */
					}

				}

				/* Formatting is off, print unformatted text */
				else
				{
					/* This loop eliminates formatting commands */
					while (unformatted[0] == '?')
					{
						fgets(unformatted, MAX_LINE_LEN, fp);
					}

					/* Prints unfomatted text */
					printf("%s", unformatted);
					break;
				}

				/* Advances p to the next token */
				p = strtok(NULL, " ");
			}
		}
    }

	/* Prints last line */
	printf("%s \n", output);

    /* Closes file streams */
    fclose (fp);
	fclose(f);

    return 0;
}

/* ------------------------------------------------------------------------------ */
/* This function gets all formatting commands at the beginning of the input file
 * These commands are then stored in global variables
 */
void parameters (FILE *f)
{
    /* Creates a pointer to traverse array */
    char *p = input;
    /* Creates char array for fgets to put commands into for tokenization */
    char buffer[MAX_LINE_LEN];
    /* Initalizes int for for loop */
    int i;

    for (i=0; i<=3; i++) /* Arbitrary max 3 commands at top of input file */
    {
		/* Reads command */
		fgets (buffer, 30, f);		/* Gets line */
		strtok (buffer, " ");		/* Tokenizes first command, stores in buffer */
		p = strtok (NULL, " "); 	/* p is now pointing at the number */

		/* If the command is width, store variable and turn formatting on */
		if (buffer[0] == '?' && buffer[1] == 'w')
		{
		    width = atoi(p);
		    fmt = 1;
		}
	
		/* If the command is margin, store variable */
		if (buffer[0] == '?' && buffer[1] == 'm')
		{
		    margin = atoi(p);
		}
		
		/* If the command is fmt, turn off formatting */
		if (buffer[0] == '?' && buffer[1] == 'f')
		{
		    fmt = 0;
		}
    }
}

/* ------------------------------------------------------------------------------ */
/* This function adds margins to a single line of text */
void add_margin (char *input_array)
{
    int i;

    for (i=0; i < margin; i++)
    {
		strcat(input_array, " ");
    }
		
}
