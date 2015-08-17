#include "ContainerRoot.h"
#include "Group.h"

#include "json.h"
#include "jsonparse.h"

#include <stdlib.h>
#include <stdio.h>

void parseCRoot(struct jsonparse_state *state, ContainerRoot *o);

//typedef enum TYPE {
//  ContainerRoot_Type,
//  Group_Type
//} TYPE;

char attr[200];

ContainerRoot* parse(struct jsonparse_state *state, char type)
{
  char str[200];
  ContainerRoot* o = new_ContainerRoot();

  type = jsonparse_next(state);
  jsonparse_copy_value(state, str, sizeof str);
  if (type == JSON_TYPE_PAIR_NAME)
  {
    printf("parsing Croot\n");
    parseCRoot(state, o);
  }

  return o;
}

void parseGroup(struct jsonparse_state *state, Group *g)
{
  char type = JSON_TYPE_OBJECT;
  while((type = jsonparse_next(state)) != '}')
  {
    jsonparse_copy_value(state, attr, sizeof attr);
    printf("Group loop attr: %s type: %c\n", attr, type);
    if (strcmp(attr, "name") == 0 && type == JSON_TYPE_PAIR_NAME)
    {
      type = jsonparse_next(state);
      type = jsonparse_next(state);
      jsonparse_copy_value(state, attr, sizeof attr);
      g->VT->namedElementAddName((NamedElement*) g, attr);
    } else if (strcmp(attr, "started") == 0 && type == JSON_TYPE_PAIR_NAME)
    {
      type = jsonparse_next(state);
      type = jsonparse_next(state);

      jsonparse_copy_value(state, attr, sizeof attr);
      bool b = true;
      if (strcmp(attr, "false") == 0)
      {
        b = false;
      }
      g->VT->instanceAddStarted((Instance*) g, b);
    }
  }
}

void parseCRoot(struct jsonparse_state *state, ContainerRoot *o)
{
  char type = JSON_TYPE_PAIR_NAME;
  type = jsonparse_next(state);
  jsonparse_copy_value(state, attr, sizeof attr);

  while (type != JSON_TYPE_ERROR)
  {
    type = jsonparse_next(state);
    jsonparse_copy_value(state, attr, sizeof attr);
    //printf("type: %c attr: %s\n", type, attr);

    if (strcmp(attr, "eClass") == 0)
    {
    } else if (strcmp(attr, "generated_KMF_ID") == 0 && type == JSON_TYPE_PAIR_NAME) 
    {
      type = jsonparse_next(state);
      type = jsonparse_next(state);
      if (type != JSON_TYPE_STRING)
        exit(EXIT_FAILURE);
      jsonparse_copy_value(state, attr, sizeof attr);

      //strcpy(o->generated_KMF_ID, attr);
    } else if (strcmp(attr, "groups") == 0 && type == JSON_TYPE_ARRAY)
    {
      while((type = jsonparse_next(state)) != ']')
      {
        if (type == JSON_TYPE_OBJECT)
        {
          Group* g = new_Group();
          printf("parsing Group\n");
          parseGroup(state, g);
          o->VT->containerRootAddGroups(o, g);
        }


      }
      //type = jsonparse_next(state); // should be JSON_TYPE_PAIR
    }
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

  printf("-------------------\n");
  o->VT->fptrToJSON(o);
  printf("-------------------\n");

  o->VT->delete(o);
  free(model);
  free(o);

  return EXIT_SUCCESS;
}
