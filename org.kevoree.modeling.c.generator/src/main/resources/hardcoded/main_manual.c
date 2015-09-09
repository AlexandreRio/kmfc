#include "ContainerRoot.h"
#include "Dictionary.h"
#include "Group.h"

#include "hashmap.h"

#include <stdlib.h>
#include <stdio.h>

typedef struct _ref {
  char* id;
  void** whereToWrite;
} ref;

union references {
  TypeDefinition* def;
};

char* getIdFromRef(any_t ref_t)
{
  ref *refS = (ref*) ref_t;
  return refS->id;
}

map_t ref_map;
Group* gr;

void createRef(void)
{
  printf("meta: %s\n", gr->VT->metaClassName(gr));
  printf("@: %p\n", &gr->dictionary);
  ref *ref1 = malloc(sizeof(ref));
  ref1->id = "ref1";
  ref1->whereToWrite = &gr->dictionary;
  hashmap_put(ref_map, ref1->id, ref1);

  ref *ref2 = malloc(sizeof(ref));
  ref2->id = "ref2";
  hashmap_put(ref_map, ref2->id, ref2);
}

int main(void)
{
  ref_map = hashmap_new(getIdFromRef);

  gr = new_Group();
  createRef();
  Dictionary* dico = new_Dictionary();
  hashmap_map* m = (hashmap_map*) ref_map;

  ref* get;
  hashmap_get(ref_map, "ref1", (void**)(&get));
  if (get != NULL)
  {
    printf("result is: %s and @: %p\n", get->id, get->whereToWrite);
    (*(get->whereToWrite)) = dico;
  }

  if (get->whereToWrite != NULL)
  {
    Dictionary* d = *get->whereToWrite;
    printf("get is correctly set\n");
    printf("meta: %s\n", d->VT->metaClassName(d));
  }

  if (gr->dictionary != NULL)
  {
    printf("Dictonnary is correctly set\n");
    printf("meta: %s\n", gr->dictionary->VT->metaClassName(gr->dictionary));
  }

  printf("-------------------------\n");
  for (int i=0; i<m->table_size; i++)
  {
    ref* r = (ref*) (m->data[i].data);
    printf("[%d]: %s\n", i, r->id);
    free(r);
  }
  hashmap_free(ref_map);
  dico->VT->delete(dico);
  free(dico);
  gr->VT->delete(gr);
  free(gr);
  // also try to write in the memory location
}
