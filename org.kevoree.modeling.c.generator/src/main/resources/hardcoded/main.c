#include "ContainerRoot.h"

#include "json.h"
#include "jsonparse.h"

#include <stdlib.h>
#include <stdio.h>

void parse(struct jsonparse_state *state, char type)
{
  char str[200];

  while (type != JSON_TYPE_ERROR)
  {
    type = jsonparse_next(state);
    jsonparse_copy_value(state, str, sizeof str);
    printf("%s\n", str);
  }
}

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

  parse(&state, jsonparse_next(&state));

  ContainerRoot *o = new_ContainerRoot();

  //o->VT->fptrToJSON(o);
  o->VT->delete(o);
  free(model);
  free(o);

  return EXIT_SUCCESS;
}
