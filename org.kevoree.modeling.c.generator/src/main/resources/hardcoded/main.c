#include "ContainerRoot.h"
#include "Group.h"
#include "VisitorJSON.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
  ContainerRoot *o = new_ContainerRoot();
  Group* g1 = new_Group();
  g1->name = "some_group_name";
  o->VT->containerRootAddGroups(o, g1);

  Group* g2 = new_Group();
  g2->name = "other_group";
  o->VT->containerRootAddGroups(o, g2);

  VisitorJSON* visitor = visitor_json_new();
  //visitor->visitor->visit(visitor, o->VT->metaClassName(o), o);
  o->VT->fptrAccept(o, visitor);
  visitor->destroy(visitor);
  o->VT->delete(o);
  free(o);
}
