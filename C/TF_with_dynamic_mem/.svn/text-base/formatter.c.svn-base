/*
 * UVic SENG 265, Summer 2014,  A#4
 *
 * This will contain the bulk of the work for the fourth assignment. It
 * provide similar functionality to the class written in Python for
 * assignment #3.
 */

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "formatter.h"
/* -------------------------------------------------------------------------- */
int get_commands(char *line);
/* -------------------------------------------------------------------------- */
struct Output
{
    int size;
    char **data;
};
/* -------------------------------------------------------------------------- */
/* variables */
int fmt = 0;
int mrgn = 0;
int width = 0;
int current_pos = 0;
/* -------------------------------------------------------------------------- */
char **format_file(FILE *file)
{
    /* allocate buffer for input */
    /* getline()??? */
    char line_buffer[1000];
    /* initialize memory for string array */
    char **lines = calloc(1, sizeof(char*));
    /* initialize line count to 0 */
    int num_lines = 0;
    
    if (lines == NULL)
    {
        fprintf(stderr, "ERROR: Memory Allocation Failed\n");
        exit(1);
    }
    
    /* iterates through lines from input file */
    while (fgets(line_buffer, 1000, file))
    {
        /* reallocate memory for input */
        lines = (char**)realloc(lines, sizeof(char*) * (num_lines + 1));
        
        if (lines == NULL)
        {
            fprintf(stderr, "ERROR: Memory Allocation Failed\n");
            exit(1);
        }
        
        /* allocate memory for the new line (including newline char at end) */
        lines[num_lines] = calloc((strlen(line_buffer) + 1), sizeof(char));
        
        if (lines[num_lines] == NULL)
        {
            fprintf(stderr, "ERROR: Memory Allocation Failed\n");
            exit(1);
        }
        
        /* copy new line from buffer into initialized lines array, increase line count */
        strcpy(lines[num_lines], line_buffer);
        num_lines++;
    }
    
    /* lines now contains a string array copy of the input file */
  
    /* send lines to be formatted, then return the result */
    char **result = format_lines(lines, num_lines);
    return result;
}
/* -------------------------------------------------------------------------- */
char **format_lines(char **lines, int num_lines)
{
    struct Output output;
    
    /* initalize memory for output */
    output.data = (char**)calloc(1,sizeof(char*));
    
    if (output.data == NULL) {
        fprintf(stderr, "ERROR: Memory Allocation Failed\n");
        exit(1);
    }
    
    /* initialize size of output */
    output.size = 0;
    
    /* iterate through the input, line by line */
    int i;
    for (i = 0; i < num_lines; i++)
    {
        /* if the line is a command, it will be dealt with in the function get_commands() */
        if (get_commands(lines[i]))
        {
            continue;
        }
        /* else if it is a new line */
        else if (!strcmp(lines[i], "\n") && fmt)
        {
            /* record output size and reallocate space in full output array */
            output.size++;
            output.data = (char**)realloc(output.data, sizeof(char*) * (output.size + 1));
            
            if (output.data == NULL)
            {
                fprintf(stderr, "ERROR: Memory Allocation Failed\n");
                exit(1);
            }
            
            /* next, we initalize cleared space for the line in the output array */
            output.data[output.size] = (char*)calloc(2, sizeof(char));
            
            if (output.data[output.size] == NULL)
            {
                fprintf(stderr, "ERROR: Memory Allocation Failed\n");
                exit(1);
            }
           
            /* aaand copy the newline into output, preparing for the next line */
            strcpy(output.data[output.size], "\n");
            current_pos = width;
            continue;
        }
        /* else it is not a newline and if formatting is on */
        else if (fmt)
        {
            /* allocate memory for a buffer to hold the line to be formatted */
            char *line_buffer = (char*)calloc(strlen(lines[i]) + 1, sizeof(char));
            
            if (line_buffer == NULL)
            {
                fprintf(stderr, "ERROR: Memory Allocation Failed\n");
                exit(1);
            }
            
            /* copy the unformatted line into the buffer and tokenize by whitespace */
            strcpy(line_buffer, lines[i]);
            char* word = strtok(line_buffer, " \n");
            
            /* while there is a word */
            while (word)
            {
                /* if the word is too large to fit on the line within the given width, raise error */
                if (strlen(word) > (width - mrgn)) {
                    fprintf(stderr, "ERROR: Invalid input - word is too large to fit on a line.\nWord: %s\n", word);
                    exit(1);
                }
                
                /* if the next word will go over allocated width */
                if (current_pos + strlen(word) + 1 > width)
                {
                    /* make ze newline, increase output size */
                    strcat(output.data[output.size], "\n");
                    output.size++;
                    output.data = (char**)realloc(output.data, sizeof(char*) * (output.size + 1));
                    
                    if (output.data == NULL)
                    {
                        fprintf(stderr, "ERROR: Memory Allocation Failed\n");
                        exit(1);
                    }
                    
                    /* we just started a new line, so set pos back to 0 */
                    current_pos = 0;
                }
                /* else the next word will fit, add a space */
                else if (current_pos != 0)
                {
                    strcat(output.data[output.size], " ");
                    current_pos++;
                }
                
                /* if a new line has been started */
                if (current_pos == 0)
                {
                    /* initialize with calloc to clear for next line */
                    output.data[output.size] = (char*)calloc(width + 2, sizeof(char));
                   
                    if (output.data[output.size] == NULL)
                    {
                        fprintf(stderr, "ERROR: Memory Allocation Failed\n");
                        exit(1);
                    }
                   
                    /* add margin */
                    int margin_maker = 0;
                    while (margin_maker < mrgn)
                    {
                        strcat(output.data[output.size], " ");
                        current_pos++;
                        margin_maker++;
                    }
                }
                
                /* lastly, add the word to output array and get new word */
                strcat(output.data[output.size], word);
                current_pos += strlen(word);
                word = strtok(NULL, " \n");
            }
          
            free(line_buffer);
            continue;
        }
        /* if fmt is off, we're just going to output the unformatted line */
        else
        {
            /* add the unformatted line and a newline to output */
            if (current_pos < width && output.size > 0)
            {
                strcat(output.data[output.size], "\n");
                output.size++;
            }
            
            /* reallocate output for lines */
            output.data = (char**)realloc(output.data, (output.size + 1) * sizeof(char*));
            
            if (output.data == NULL)
            {
                fprintf(stderr, "ERROR: Memory Allocation Failed\n");
                exit(1);
            }
            
            /* initialize space for the line in output */ 
			output.data[output.size] = (char*)calloc((strlen(lines[i])+1), sizeof(char));
            
            if (output.data[output.size] == NULL)
            {
                fprintf(stderr, "ERROR: Memory Allocation Failed\n");
                exit(1);
            }
            
            /* finally, copy the string into output */
            strcpy(output.data[output.size], lines[i]);
            output.size++;
            current_pos = width + 1;
            continue;
        }
    }
    
    
    /* adds final newline at the end of the file */
    if (fmt)
    {
        output.size++;
        output.data = (char**)realloc(output.data, sizeof(char*) * (output.size + 1));
        output.data[output.size] = (char*)calloc(2, sizeof(char));
        strcpy(output.data[output.size], "\n");
        output.size++;
    }
    
    /* nullify very last line in output, else we get lots of seg faults when printing */
    output.data[output.size] = NULL;
    
    /* return output array */
    return output.data;
}
/* -------------------------------------------------------------------------- */
int get_commands(char *line) {

    /* first, a copy of the input line must be made */
    /* this allocates the length and initalizes a string of appropriate size */
    int len = strlen(line) + 1;
    char *line_buffer = (char*)calloc(len, sizeof(char));
    
    /* error handling */
    if (line_buffer == NULL)
    {
        fprintf(stderr, "ERROR: Memory Allocation Failed\n");
        exit(1);
    }
    
    /* now we can make a copy of the input line and tokenize by whitespace*/
    strcpy(line_buffer, line);
    strtok(line_buffer, " \n");
    
    /* we have our tokenized line, we need to see if it is a command */
 
    /* if the command is ?fmt */
    if (!strcmp(line_buffer, "?fmt"))
    {
        char* token = strtok(NULL, " \n");
        
        if (!strcmp(token, "on"))   /* if fmt is on, turn on format command */
        {
            fmt = 1;
        }
        else if (!strcmp(token, "off"))     /* if fmt is off, turn off formatting */
        {
            fmt = 0;
        }
        else    /* fmt has recieved something other than 'on' or 'off', print error msg */
        {
            fprintf(stderr, "Format only accepts 'on' and 'off'. Command given: %s\n", token);
            exit(1);
        }
        
        /* set the current position to 0 and return, ensuring the command doesn't get output */
        current_pos = 0;
        return 1;
    }
    
    /* if the command is ?mrgn */
    if (!strcmp(line_buffer, "?mrgn"))
    {
        char* token = strtok(NULL, " \n");
        
        /* mrgn can have an operator, so next we check for one */
        
        /* if the operator is plus */
        if (!strncmp(token, "+", 1))
        {
            /* add margin */
            token = token + 1;
            mrgn += atoi(token);
        }
        /* if the operator is minus */
        else if (!strncmp(token, "-", 1))
        {
            /* subtract margin */
            token = token + 1;
            mrgn -= atoi(token);
        }
        /* margin does not have an operator, set margin */
        else
        {
            mrgn = atoi(token);
        }
        
        /* ensuring mrgn is to spec */
        if (mrgn >= width - 20)
        {
            /* fprintf(stderr, "ERROR: Margin was set out of required specifications. Attempted margin: %d\n", mrgn); exit(1); */
            mrgn = width - 20;
        }
        if (mrgn < 0)
        {
            /* fprintf(stderr, "ERROR: Margin was set out of required specifications. Attempted margin: %d\n", mrgn); exit(1); */
            mrgn = 0;
        }
        
        return 1;
    }
    
    /* if the operator is width, set width and turn on formatting */
    if (!strcmp(line_buffer, "?width"))
    {
        char* token = strtok(NULL, " \n");
        width = atoi(token);
        fmt = 1;
        return 1;
    }
    
    /* if no command has been detected, free memory and return 0 */
    free(line_buffer);
    return 0;
}
