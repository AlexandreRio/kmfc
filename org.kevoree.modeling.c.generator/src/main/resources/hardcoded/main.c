#include "ContainerRoot.h"
#include "Group.h"

#include "json.h"
#include "jsonparse.h"

#include <stdlib.h>
#include <stdio.h>

void parseCRoot(struct jsonparse_state *state, ContainerRoot *o, char* attr);

typedef enum TYPE {
  ContainerRoot_Type,
  Group_Type
} TYPE;


ContainerRoot* parse(struct jsonparse_state *state, char type)
{
  TYPE current = ContainerRoot_Type;
  char str[200];
  ContainerRoot* o = new_ContainerRoot();

  type = jsonparse_next(state);
  jsonparse_copy_value(state, str, sizeof str);
  if (type == JSON_TYPE_PAIR_NAME)
  {
    type = jsonparse_next(state); // should be JSON_TYPE_PAIR
    parseCRoot(state, o, str);
    //printf("type: %c str: %s\n", type, str);
    // we look for an attribute named "str" in the TYPE struct
  }
  //printf("type: %c str: %s\n", type, str);

  return o;
}

void parseCRoot(struct jsonparse_state *state, ContainerRoot *o, char* attr)
{
  char str[200];
  char type;
  if (strcmp(attr, "eClass") == 0)
  {
  } else if (strcmp(attr, "generated_KMF_ID") == 0) 
  {
    type = jsonparse_next(state);
    if (type != JSON_TYPE_STRING)
      exit(EXIT_FAILURE);
    jsonparse_copy_value(state, str, sizeof str);

    strcpy(o->generated_KMF_ID, str);
  } else if (strcmp(attr, "nodes") == 0)
  {
    type = jsonparse_next(state);
    if (type != JSON_TYPE_ARRAY)
      exit(EXIT_FAILURE);

    while((type = jsonparse_next(state)) != ']')
    {
      if (type == JSON_TYPE_OBJECT)
      {
        Group* g = new_Group();
        // eather loop over the attribute or delegate to the other parser
        parseGroup(state, g, "");
      }


    }
    //type = jsonparse_next(state); // should be JSON_TYPE_PAIR
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

  ContainerRoot *o = parse(&state, jsonparse_next(&state));

  o->VT->fptrToJSON(o);
  o->VT->delete(o);
  free(model);
  free(o);

  return EXIT_SUCCESS;
}
