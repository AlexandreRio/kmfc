#include "ContainerRoot.h"
#include "VisitorJSON.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
	ContainerRoot *o = new_ContainerRoot();
	o->VT->delete(o);
	VisitorJSON* visitor = visitor_json_new();
	visitor->visitor->visit(visitor, o->VT->metaClassName(o), o);
	visitor->destroy(visitor);
	free(o);
}
