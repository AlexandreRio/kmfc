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

int main(void)
{
  Group* gr = new_Group();
  Dictionary* dico = new_Dictionary();

  printf("ptr is: %p\n", gr->dictionary);

  ref r;
  void** ptr = &gr->dictionary;
  printf("@: %p @: %p @:%p\n", ptr, *ptr, &gr->dictionary);
  memcpy(ptr, &dico, sizeof(ptr));
  printf("@: %p @: %p @:%p\n", ptr, *ptr, &gr->dictionary);

  printf("ptr is: %p\n", gr->dictionary);
  if (gr->dictionary != NULL)
    printf("meta: %s\n", gr->dictionary->VT->metaClassName(gr->dictionary));

  dico->VT->delete(dico);
  free(dico);
  gr->VT->delete(gr);
  free(gr);
}
