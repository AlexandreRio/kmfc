#include "ContainerRoot.h"

int main(void)
{
  ContainerRoot *r = new_ContainerRoot();
  printf("id: %s", r->generated_KMF_ID);
  return 0;
}
