/*
 * KMF4C.c
 *
 *  Created on: May 11, 2015 12:45:19 AM
 *      Author: Francisco Acosta
 *       eMail: fco.ja.ac@gmail.com
 */

#include "KMFContainer.h"

#include <stdlib.h>
#include <stddef.h>
#include <stdio.h>
#include <string.h>

char *my_strdup(const char *string)
{
	char *r = malloc (strlen(string) + 1);
	strcpy(r, string);
	if (!strcmp(string, "Group")) {
		printf("Te agarramos cabron! : %s\n", string);
	}
	return r;
}

char*
KMFContainer_get_path(void* this) {
	KMFContainer* c = (KMFContainer*)this;
	return strdup("");
}

char*
get_eContainer_path(KMFContainer* kmf)
{
	return kmf->eContainer->VT->getPath(kmf->eContainer);
}

char*
get_key_for_hashmap(any_t t)
{
	KMFContainer* c = (KMFContainer*)t;
	return c->VT->internalGetKey(c); 
}

void
initKMFContainer(KMFContainer * const this)
{
	/*
	 * KMFContainer
	 */
	this->eContainer = NULL;
}

static void
delete_KMFContainer(KMFContainer * const this)
{

}

void
delete(KMFContainer *container)
{
	container->VT->delete(container);
	free(container);
}


void
deleteContainerContents(map_t container)
{
	hashmap_map *m = (hashmap_map*)container;
	int i;
	for(i = 0; i< m->table_size; i++) {
		any_t data = (any_t) (m->data[i].data);
		KMFContainer *n = data;
		delete(n);
	}
}

static void
KMFContainer_visit(KMFContainer * const this, char *parent, fptrVisitAction action, fptrVisitActionRef secondAction, bool visitPaths)
{
	char class[100];
	sprintf(class, "org.kevoree.%s", this->VT->metaClassName(this));
	action("eClass", STRING, class);
	action(NULL, COLON, NULL);
}

const KMFContainer_VT KMF_VT = {
		.super = NULL,
		.metaClassName = NULL,
		.internalGetKey = NULL,
		.getPath = KMFContainer_get_path,
		.visit = KMFContainer_visit,
		.findByPath = NULL,
		.delete = delete_KMFContainer
};
