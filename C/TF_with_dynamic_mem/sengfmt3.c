/*
 * UVic SENG 265, Summer 2014, A#4
 *
 * This will contain a solution to sengfmt3. In order to complete the
 * task of formatting a file, it must open the file and pass the result
 * to a routine in formatter.c.
 */

#include <stdio.h>
#include <stdlib.h>
#include "formatter.h"

int main(int argc, char *argv[])
{
    /* file variables */
    char *filename;
    FILE *file;

    /* if a filename is NOT given, take input from stdin */
    if (argc < 2)
    {	
		file = stdin;
		while (!file)   /* error handling */
		{
			printf("ERROR: No input was given!\n");
			printf("Please type some input:\n");
			file = stdin;
		}
    }
	else
	{
    	/* filename given, attempt to open and read the file */
    	filename = argv[1];
    	file = fopen(filename, "r");
    
    	/* if the file cannot be read, print error message */
    	if (file == NULL)
    	{
        	fprintf(stderr, "ERROR: The file could not be opened\n");
        	exit(1);
    	}
	}

    /* send the input to be formatted, store the results in a string array */
    char **output = format_file(file);

    /* print output */
    int i;
    while (output[i])
    {
        printf("%s", output[i]);
        i++;
    }
    
    /* close and free file memory */
    fclose(file);
    exit(0);
}
