#include "ContainerRoot.h"
#include "ContainerNode.h"
#include "Group.h"

#include "json.h"
#include "jsonparse.h"

#include <stdlib.h>
#include <stdio.h>


#define NB_CLASSES 3
#define ContainerRoot_Type_SIZE 2

typedef char* (*fptrParseStr)(struct jsonparse_state*);

typedef enum TYPE {
  ContainerRoot_Type = 0,
  Group_Type = 1,
  ContainerNode_Type = 2
} TYPE;

struct at {
  char* attr_name;
  fptrParseStr* fptr; // fptr to parse result from json
  //why not void* fptr to set result on the object, like groupAddSubNodes(void* o, type)
};

struct ClassType {
  TYPE type;
  int nb_attributes;
  struct at* attributes;
};

// something like (json state, where to put the result, 
void parserFunct()
{}

void parseCRoot(struct jsonparse_state *state, ContainerRoot *o);
void parseObject(struct jsonparse_state *state, void* o, TYPE type);
char* parseStr(struct jsonparse_state *state);

const struct at ContainerRoot_Attr[ContainerRoot_Type_SIZE] = {
  {"generated_KMF_ID", parseStr},
  {"oe", parseStr}
};

const struct ClassType Classes[NB_CLASSES] = {
  {
    .type = ContainerRoot_Type,
    .attributes = &ContainerRoot_Attr,
    .nb_attributes = 2
  },
  {
    .type = Group_Type,
    .attributes = {{"name", parserFunct}, {"started", parserFunct}, {"subNodes", parserFunct}},
    .nb_attributes = 3
  },
  {
    .type = ContainerNode_Type,
  }
};

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
const struct ClassType getClass(TYPE type)
{
  for (int i=0; i<NB_CLASSES; i++)
    if (Classes[i].type == type)
      return Classes[i];
  exit(EXIT_FAILURE);
}

fptrParseStr getParseFunc(struct ClassType ctype, char* name)
{
  for (int i=0; i<ctype.nb_attributes; i++)
  {
    struct at a = ctype.attributes[i];
    //printf("comparing %s and %s\n", a.attr_name, name);
    if (strcmp(a.attr_name, name) == 0)
    {
      return a.fptr;
    }
  }
  return NULL;
}

char attr[200];

char* parseStr(struct jsonparse_state *state)
{
  char type;
  while((type = jsonparse_next(state)) != JSON_TYPE_STRING) {}
  jsonparse_copy_value(state, attr, sizeof attr);
  return attr;
}

void parseObject(struct jsonparse_state *state, void* o, TYPE class_type)
{
  char type = JSON_TYPE_PAIR_NAME;
  struct ClassType ctype = getClass(class_type);
  //printf("struct : %s\n", &ctype.attributes[0].attr_name);

  while (type != '}')
  {
    type = jsonparse_next(state);
    if (type == JSON_TYPE_PAIR_NAME)
    {
      jsonparse_copy_value(state, attr, sizeof attr);
      printf("In %d, need to parse: %s of type %c\n", class_type, attr, type);
      //if (strcmp(attr, "name") == 0)
      //  parseStr(state);
      //printf("parser is %p\n", parseStr);
      fptrParseStr get = getParseFunc(ctype, attr);
      //printf("getter returned %p\n", get);
      if (get != NULL)
      {
        char* res = getParseFunc(ctype, attr)(state);
        printf("result from function pointer is %s\n", res);
      }

      //TODO remove this when the generation is complete, we have
      // possible null value only because I didn't write every attributes
      // by hand
      //if (parseFunc != NULL)
      //{
      //  parseFunc(state);
      //}

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

  //ContainerRoot *o = parse(&state, jsonparse_next(&state));
  ContainerRoot* o = new_ContainerRoot();
  parseObject(&state, o, ContainerRoot_Type);

  //printf("-------------------\n");
  //o->VT->fptrToJSON(o);
  //printf("-------------------\n");

  o->VT->delete(o);
  free(model);
  free(o);

  return EXIT_SUCCESS;
}
