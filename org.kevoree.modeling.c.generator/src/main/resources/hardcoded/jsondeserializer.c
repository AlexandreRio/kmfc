#include "jsondeserializer.h"

char attr[200];

void ContainerRootSetKMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type);
void doNothing(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type);

const struct at ContainerRoot_Attr[ContainerRoot_Type_SIZE] = {
  {"generated_KMF_ID", ContainerRootSetKMF_ID, ContainerRoot_Type, Primitive_Type}     ,
  {"eClass"          , doNothing             , ContainerRoot_Type, Primitive_Type}     ,
  {"groups"          , parseArray            , ContainerRoot_Type, Group_Type}         ,
  {"nodes"           , parseArray            , ContainerRoot_Type, ContainerNode_Type} ,
  {"typeDefinitions" , parseArray            , ContainerRoot_Type, TypeDefinition_Type},
  {"repositories"    , parseArray            , ContainerRoot_Type, Repository_Type}    ,
  {"dataTypes"       , parseArray            , ContainerRoot_Type, TypedElement_Type}  ,
  {"libraries"       , parseArray            , ContainerRoot_Type, TypeLibrary_Type}
};

const struct ClassType Classes[NB_CLASSES] = {
  {
    .type = ContainerRoot_Type,
    .attributes = &ContainerRoot_Attr,
    .nb_attributes = ContainerRoot_Type_SIZE 
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

//Constuctor for abstact type,it
void* new_TypeDefinition()
{
}

const fptrConstruct construct[NB_CLASSES] = {
  [ContainerRoot_Type] = new_ContainerRoot,
  [Group_Type] = new_Group,
  [ContainerNode_Type] = new_ContainerNode,
  [Repository_Type] = new_Repository,
  [TypeLibrary_Type] = new_TypeLibrary,
  [TypedElement_Type] = new_TypedElement,
  [TypeDefinition_Type] = new_TypeDefinition
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
  char* param = parseStr(state);
  //FIXME ask paco to know how to deal with this
  if (strlen(attr) < 9)
    strcpy(((ContainerRoot*)o)->generated_KMF_ID, param);
}

void parseArray(struct jsonparse_state *state, void* o, TYPE obj_type, TYPE ptr_type)
{
  char type = JSON_TYPE_ARRAY;
  while((type = jsonparse_next(state)) != ']')
  {
    if (type == JSON_TYPE_OBJECT)
    {
      if (ptr_type == Group_Type)
      {
        void* o = construct[ptr_type]();
        printf("new object is at %p\n", o);
      }
      printf("In %d and need to create %d instance\n", obj_type, ptr_type);

    }
  }
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

      struct at get = getAttr(ctype, attr);
      get.setter(state, o, obj_type, get.ptr_type);
    }
  }
}
