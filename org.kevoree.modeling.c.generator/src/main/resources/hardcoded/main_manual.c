#include "Group.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
  Group *o = new_Group();
  char* str = "my_str";
  o->internalKey = str;
  printf("%s\n", o->internalKey);
  printf("%p", o->VT->namedElementRemoveInternalKey);
  //o->VT->namedElementRemoveInternalKey((NamedElement*)o, str);

}
