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

  VisitorJSON* visitorJSON = visitor_json_new();
  o->VT->fptrAccept(o, visitorJSON->visitor);
  visitorJSON->destroy(visitorJSON);
  o->VT->delete(o);
  free(o);
}
