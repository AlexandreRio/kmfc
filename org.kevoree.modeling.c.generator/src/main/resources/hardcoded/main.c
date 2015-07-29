#include "ContainerRoot.h"
#include "Group.h"
#include "MessagePortType.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
    ContainerRoot *r = new_ContainerRoot();
    printf("root %s\n", r->VT->internalGetKey(r));
    Group *g = new_Group();
    printf("group %s\n", g->VT->internalGetKey(g));
    g->name = "my_name";
    printf("group %s\n", g->VT->internalGetKey(g));

    MessagePortType *m = new_MessagePortType();
    printf("msgPT %s\n", m->VT->internalGetKey(m));
}