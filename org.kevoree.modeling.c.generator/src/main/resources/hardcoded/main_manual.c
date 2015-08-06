#include "ContainerRoot.h"
#include "Group.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
  ContainerRoot* o = new_ContainerRoot();

  Group* g1 = new_Group();
  g1->name = "some_group_name";
  o->VT->containerRootAddGroups(o, g1);

  Group* g2 = new_Group();
  g2->name = "other_group";
  o->VT->containerRootAddGroups(o, g2);

  /** reusing code */
  hashmap_map* m = (hashmap_map*) o->groups;
  for (int i=0; i<m->table_size; i++)
  {
    Group* data = (Group*) (m->data[i].data);
    printf("%s: %s\n", data->VT->metaClassName(data), data->VT->internalGetKey(data));
  }


  printf("size: %d", hashmap_length(o->groups));
}
