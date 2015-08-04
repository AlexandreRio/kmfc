#include "ContainerRoot.h"
#include "Group.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
	ContainerRoot *o = new_ContainerRoot();

	Group *ptr = new_Group();
	ptr->name = "some_name";

	o->VT->containerRootAddGroups(o, ptr);
	o->VT->delete(o);
	free(o);
}