#ifndef __KMF4C_H
#define __KMF4C_H

#include <stdbool.h>

#include "hashmap.h"

typedef struct _VT_KMFContainer VT_KMFContainer;
typedef struct _KMFContainer KMFContainer;

typedef char* (*fptrKMFMetaClassName)(void*);
typedef char* (*fptrKMFInternalGetKey)(void*);
typedef char* (*fptrKMFGetPath)(void*);
typedef int (*fptrToJSON)(void*);
typedef void* (*fptrFindByPath)(void*, char*);
typedef void (*fptrDelete)(void*);

typedef struct _VT_KMFContainer {
	void *super;
	/*
	 * KMFContainer_VT
	 * TODO should use the template
	 */
	fptrKMFMetaClassName metaClassName;
	fptrKMFInternalGetKey internalGetKey;
	fptrKMFGetPath getPath;
	int (*fptrToJSON)(void*);
	fptrFindByPath findByPath;
	fptrDelete delete;
} VT_KMFContainer;

typedef struct _KMFContainer {
	VT_KMFContainer *VT;
	/*
	 * KMFContainer
	 */
	KMFContainer *eContainer;
} KMFContainer;

void delete(KMFContainer *object);
void deleteContainerContents(map_t container);
void initKMFContainer(KMFContainer * const this);
char* KMFContainer_get_path(void* this);

char* get_key_for_hashmap(any_t t);

extern const VT_KMFContainer vt_KMFContainer;

/* usefull functions */
char *my_strdup(const char *string);

char* get_eContainer_path(KMFContainer* kmf);



#endif
