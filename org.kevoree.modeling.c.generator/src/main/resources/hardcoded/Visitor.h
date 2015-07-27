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

typedef void (*fptrVisitAction)(char*, Type, void*); /* (char* _path, Type type, void* value) */
typedef void *(*fptrVisitActionRef)(char*, void*); /* (char* _path, char *value) */

void Visitor_visitModelContainer(hashmap_map *m, int length, fptrVisitAction action);
void Visitor_visitPaths(hashmap_map *m, char *container, char *path, fptrVisitAction action, fptrVisitActionRef secondAction);
void Visitor_visitModelRefs(hashmap_map *m, int length, char* ref, char *path, fptrVisitAction action);
void Visitor_visitPathRefs(hashmap_map *m, char *container, char *path, fptrVisitAction action, fptrVisitActionRef secondAction, char *parent);
void Visitor_visitContainer(map_t container, char *containerName, char *parent, fptrVisitAction action, fptrVisitActionRef secondAction, bool visitPaths);

#endif /* H_VISITOR */
