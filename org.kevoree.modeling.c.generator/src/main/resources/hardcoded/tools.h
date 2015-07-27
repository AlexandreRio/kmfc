#ifndef H_TOOLS
#define H_TOOLS

#include <stdlib.h>

short StartsWith (char* base, char* str);
short EndsWith (char* base, char* str);
int indexOf (char* base, char* str);
int indexOf_shift (char* base, char* str, int startIndex);
int lastIndexOf (char* base, char* str);
char* Substring(char *string, int position, int length);
void rand_str(char *dest, size_t length);


#endif
