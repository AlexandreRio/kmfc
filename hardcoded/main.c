#include "ContainerRoot.h"

int main(void)
{
  ContainerRoot *r = new_ContainerRoot();
  printf("%s id: %s key: %s\n", r->VT->metaClassName(r), r->generated_KMF_ID, r->VT->internalGetKey(r));
  return 0;
}
