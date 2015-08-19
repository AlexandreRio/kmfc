#include "json.h"
#include "jsonparse.h"
#include "jsondeserializer.h"

#include <stdlib.h>
#include <stdio.h>

int main(void)
{
  FILE* model_file = fopen("../5nodes1component.json", "r");
  if (model_file == NULL)
    return EXIT_FAILURE;

  fseek(model_file, 0L, SEEK_END);
  int size = ftell(model_file);
  fseek(model_file, 0L, SEEK_SET);

  char* model = malloc(size+1);
  int ch;
  bool firstChar = true;

  while ((ch = fgetc(model_file)) != EOF)
  {
    if (firstChar)
    {
      sprintf(model, "%c", ch);
      firstChar = false;
    } else
    {
      sprintf(model, "%s%c", model, ch);
    }
  }
  fclose(model_file);

  struct jsonparse_state state;
  jsonparse_setup(&state, model, size+1);

  //ContainerRoot *o = parse(&state, jsonparse_next(&state));
  ContainerRoot* o = new_ContainerRoot();
  parseObject(&state, o, ContainerRoot_Type, ContainerRoot_Type);

  printf("-------------------\n");
  o->VT->fptrToJSON(o);
  printf("-------------------\n");

  o->VT->delete(o);
  free(model);
  free(o);

  return EXIT_SUCCESS;
}

//int main(void)
//{
//  for (int i=0; i<NB_CLASSES; i++)
//  {
//    struct ClassType ct = Classes[i];
//    printf("Class: %d has %d attributes\n", ct.type, ct.nb_attributes);
//    struct at* attr = ct.attributes;
//    for (int j=0; j<ct.nb_attributes; j++)
//    {
//      struct at a = ct.attributes[j];
//      printf("%d is %s and point to %p\n", j, a.attr_name, a.fptr);
//    }
//  }
//}
//
