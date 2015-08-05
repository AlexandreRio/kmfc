#include "ContainerRoot.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
	ContainerRoot *o = new_ContainerRoot();
	o->VT->delete(o);
	//visitor call
	free(o);
}
