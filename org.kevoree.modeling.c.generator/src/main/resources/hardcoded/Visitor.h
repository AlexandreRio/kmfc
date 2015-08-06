#ifndef H_Visitor
#define H_Visitor

#include "hashmap.h"
#include <stdbool.h>

typedef enum _Type
{
	INTEGER,
	STRING,
	BOOL,
	BRACKET,
	CLOSEBRACKET,
	SQBRACKET,
	CLOSESQBRACKET,
	CLOSEBRACKETCOLON,
	CLOSESQBRACKETCOLON,
	COLON,
	STRREF,
	RETURN,
	REFERENCE
} Type;

typedef struct _visitor {

  /**
   * char* is for metaclassname, so it may need to be changed later
   * for an enum, because MEMORY
   */
  int (*visit)(struct _visitor*, char*, void*);
  int (*destroy)(struct _visitor*);
  void* impl;
} Visitor;

Visitor * visitor_new(void*, int (*)(Visitor*, char*, void*));

#endif /* H_VISITOR */
