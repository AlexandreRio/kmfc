#include "jsondeserializer.h"

char attr[200];

void ContainerRootSetKMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type);
void doNothing(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type);

const struct at ContainerRoot_Attr[ContainerRoot_Type_SIZE] = {
  {"generated_KMF_ID", ContainerRootSetKMF_ID, ContainerRoot_Type, Primitive_Type},
  {"eClass", doNothing, ContainerRoot_Type, Primitive_Type},
  {"groups", parseArray, ContainerRoot_Type, Group_Type}
};

const struct ClassType Classes[NB_CLASSES] = {
  {
    .type = ContainerRoot_Type,
    .attributes = &ContainerRoot_Attr,
    .nb_attributes = 3
  },
  {
    .type = Group_Type,
    .attributes = NULL,
    .nb_attributes = 0
  },
  {
    .type = ContainerNode_Type,
  }
};


const struct ClassType getClass(TYPE type)
{
  for (int i=0; i<NB_CLASSES; i++)
    if (Classes[i].type == type)
      return Classes[i];
  exit(EXIT_FAILURE);
}

struct at getAttr(struct ClassType ctype, char* name)
{
  struct at a;
  for (int i=0; i<ctype.nb_attributes; i++)
  {
    a = ctype.attributes[i];
    //printf("comparing %s and %s\n", a.attr_name, name);
    if (strcmp(a.attr_name, name) == 0)
    {
      return a;
    }
  }
  return a;
}

char* parseStr(struct jsonparse_state *state)
{
  char type;
  while((type = jsonparse_next(state)) != JSON_TYPE_STRING) {}
  jsonparse_copy_value(state, attr, sizeof attr);
  return attr;
}

void doNothing(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSetKMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
  printf("In %d and need to set %d type attribute\n", obj_type, ptr_type);
  char* param = parseStr(state);
  ContainerRoot* ptr = (ContainerRoot*)o;
  strcpy(ptr->generated_KMF_ID, param);
  printf("new generated_KMF_ID value is %s\n", ptr->generated_KMF_ID);
}

void parseArray(struct jsonparse_state *state, void* o, TYPE obj_type, TYPE ptr_type)
{
  printf("In %d and need to create %d instance\n", obj_type, ptr_type);
  //while((type = jsonparse_next(state)) != ']')
  //{
  //}
}

/**
 *
 * param o: object to set the value on
 *
 */
void parseObject(struct jsonparse_state *state, void* o, TYPE obj_type, TYPE ptr_type)
{
  char type = JSON_TYPE_PAIR_NAME;
  struct ClassType ctype = getClass(obj_type);
  //
  //printf("struct : %s\n", &ctype.attributes[0].attr_name);

  while (type != '}')
  {
    type = jsonparse_next(state);
    if (type == JSON_TYPE_PAIR_NAME)
    {
      jsonparse_copy_value(state, attr, sizeof attr);
      printf("In %d, need to parse: %s of type %c\n", obj_type, attr, type);

      //FIXME this can be removed when the generation is complete
      struct at get = getAttr(ctype, attr);
      get.setter(state, o, obj_type, get.ptr_type);

    }
  }
}

