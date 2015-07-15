/**
 * Author: fco.ja.ac@gmail.com
 * Date: 10/05/2015
 * Time: 19:25
 */

#include "KMFContainer.h"
#include "Visitor.h"

#include <stddef.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

void Visitor_visitModelContainer(hashmap_map *m, int length, fptrVisitAction action)
{
	int i;
	for(i = 0; i< m->table_size; i++) {
		action(NULL, BRACKET, NULL);
		any_t data = (any_t) (m->data[i].data);
		KMFContainer *n = data;
		n->VT->visit(n, NULL, action, NULL, false);
		if(length > 1) {
			action(NULL, CLOSEBRACKETCOLON, NULL);
			length--;
		} else {
			action(NULL, CLOSEBRACKET, NULL);
		}
	}
	/*action(NULL, CLOSESQBRACKETCOLON, NULL);*/
}

void Visitor_visitPaths(hashmap_map *m, char *container, char *path, fptrVisitAction action, fptrVisitActionRef secondAction)
{
	int i;
	char *originalPath = strdup(path);

	for(i = 0; i< m->table_size; i++) {
		any_t data = (any_t) (m->data[i].data);
		KMFContainer *n = data;
		sprintf(path, "%s[%s]", originalPath, n->VT->internalGetKey(n));
		if (secondAction != NULL) {
			if (secondAction(path, container)) {
				n->VT->visit(n, path, action, secondAction, true);
			}
		} else {
			n->VT->visit(n, path, action, secondAction, true);
		}
	}
	free(originalPath);
}

void Visitor_visitModelRefs(hashmap_map *m, int length, char* ref, char *path, fptrVisitAction action)
{
	int i;
	for(i = 0; i< m->table_size; i++) {
		any_t data = (any_t) (m->data[i].data);
		KMFContainer *n = data;
		sprintf(path, "%s[%s]", ref, n->VT->internalGetKey(n));
		action(path, STRREF, NULL);
		/*action(NULL, RETURN, NULL);*/
		if(length > 1) {
			action(NULL, COLON, NULL);
			length--;
		} else {
			action(NULL, RETURN, NULL);
		}
	}
	/*action(NULL, CLOSESQBRACKETCOLON, NULL);*/
}

void Visitor_visitPathRefs(hashmap_map *m, char *container, char *path, fptrVisitAction action, fptrVisitActionRef secondAction, char *parent)
{
	int i;

	for(i = 0; i< m->table_size; i++) {
		any_t data = (any_t) (m->data[i].data);
		KMFContainer* n = data;
		char* this_path = n->VT->getPath(n);
		sprintf(path, "%s/%s\\%s", parent, this_path, container);
		free(this_path);
		action(path, REFERENCE, parent);
	}
}

void Visitor_visitContainer(map_t container, char *containerName, char *parent, fptrVisitAction action, fptrVisitActionRef secondAction, bool visitPaths)
{
	char path[256];
	memset(&path[0], 0, sizeof(path));

	hashmap_map *m;
	int length;

	if((m = (hashmap_map*)container) != NULL) {
		length = hashmap_length(container);
		if (visitPaths) {
			sprintf(path,"%s/%s", parent, containerName);
			Visitor_visitPaths(m, containerName, path, action, secondAction);
		} else {
			action(containerName, SQBRACKET, NULL);
			Visitor_visitModelContainer(m, length, action);
			action(NULL, CLOSESQBRACKETCOLON, NULL);
		}
	} else if (!visitPaths) {
		action(containerName, SQBRACKET, NULL);
		action(NULL, CLOSESQBRACKETCOLON, NULL);
	}

}
