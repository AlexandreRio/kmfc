#include "ContainerRoot.h"
#include "Group.h"

int main(void)
{
  ContainerRoot *r = new_ContainerRoot();
  Group *g = new_Group();
  printf("%s id: %s key: %s\n", r->VT->metaClassName(r), r->generated_KMF_ID, r->VT->internalGetKey(r));
  g->name = "test";
  printf("%s key: %s\n", g->VT->metaClassName(g), g->VT->internalGetKey(g));
  r->VT->containerRootAddGroups(r, g);
  Group *get = r->VT->containerRootFindgroupsByID(r, "test");
  printf("%s key: %s\n", get->VT->metaClassName(get), get->VT->internalGetKey(get));
  return 0;
}
