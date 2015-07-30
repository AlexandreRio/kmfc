#include "Group.h"
#include "ContainerNode.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
  Group *o = new_Group();
  o->name = "some_name";
  ContainerNode *ptr = new_ContainerNode();
  ptr->name = "some_name";
  o->VT->groupAddSubNodes(o, ptr);
}
