#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include "tools.h"

#define DEBUG 0
#if DEBUG
#define PRINTF(...) printf(__VA_ARGS__)
#else
#define PRINTF(...)
#endif

char* Substring(char* string, int position, int length)
{
	char *pointer;
	int c;
	
	pointer = malloc(length+1);
	
	if (pointer == NULL)
	{
		printf("Unable to allocate memory.\n");
		/*exit(EXIT_FAILURE);*/
		return NULL;
	}
	
	for (c = 0 ; c < position -1 ; c++)
		string++;
	
	for (c = 0 ; c < length ; c++)
	{
		*(pointer+c) = *string;
		string++;
	}

	*(pointer+c) = '\0';

	return pointer;
}

/* Detecting whether base is starts with str */

short StartsWith (char* base, char* str)
{
	return (strstr(base, str) - base) == 0;
}

/* Detecting whether base is ends with str */

short EndsWith (char* base, char* str)
{
	int blen = strlen(base);
	int slen = strlen(str);

	return (blen >= slen) && (0 == strcmp(base + blen - slen, str));
}
/* getting the first index of str in base */

int indexOf (char* base, char* str)
{
	return indexOf_shift(base, str, 0);
}

int indexOf_shift (char* base, char* str, int startIndex)
{
	int result;
	int baselen = strlen(base);
	
	/* str should not longer than base*/
	if (strlen(str) > baselen || startIndex > baselen)
	{
		result = -1;

	}
	else
	{
		if (startIndex < 0 )
		{
			startIndex = 0;
		}
		
		char* pos = strstr(base+startIndex, str);
		
		if (pos == NULL)
		{
			result = -1;
		}
		else
		{
			result = pos - base;
		}
	}

	return result;
}

/** use two index to search in two part to prevent the worst case
 * (assume search 'aaa' in 'aaaaaaaa', you cannot skip three char each time)
 */

int lastIndexOf (char* base, char* str)
{
	int result;

	/* str should not longer than base*/
	
	if (strlen(str) > strlen(base))
	{
		result = -1;
	}
	else
	{
		int start = 0;
		int endinit = strlen(base) - strlen(str);
		int end = endinit;
		int endtmp = endinit;
		
		while(start != end)
		{
			start = indexOf_shift(base, str, start);
			end = indexOf_shift(base, str, end);

			/* not found from start */
			if (start == -1)
			{
				end = -1; /* then break; */
			} 
			else if (end == -1) 
			{
				/* found from start
				 * but not found from end
				 * move end to middle
				 */
				if (endtmp == (start+1))
				{
					end = start; /* then break; */
				}
				else
				{
					end = endtmp - (endtmp - start) / 2;
					
					if (end <= start)
					{
						end = start+1;
					}

					endtmp = end;
				}
			}
			else
			{
				/* found from both start and end
				 * move start to end and
				 * move end to base - strlen(str)
				 */
				start = end;
				end = endinit;
			}
		}
		
		result = start;
	}

	return result;
}

void rand_str(char *dest, size_t length)
{
	char charset[] = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

	while (length-- > 0)
	{
		size_t index = (double) rand() / RAND_MAX * (sizeof charset - 1);
		*dest++ = charset[index];
	}

	*dest = '\0';
}
