#include "Group.h"
#include "ContainerNode.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
    Group *g = new_Group();
    ContainerNode *n = new_ContainerNode();
    //print an error because nothing happen
    g->VT->groupAddSubNodes(g, n);

}
