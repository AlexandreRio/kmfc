#include "ContainerRoot.h"
#include "Group.h"

#include "hashmap.h"

#include <stdlib.h>
#include <stdio.h>

struct ref {
  char* id;
  void* whereToWrite;
};

char* getIdFromRef(any_t ref)
{
  struct ref *refS = (struct ref*) ref;
  return refS->id;
}

int main(void)
{
  map_t ref_map = hashmap_new(getIdFromRef);


  // create some ref struct and try to get them in the map
  //
  // also try to write in the memory location
}
