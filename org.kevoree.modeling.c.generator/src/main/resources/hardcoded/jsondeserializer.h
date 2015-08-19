#ifndef __jsondeserializer_H
#define __jsondeserializer_H

#include "kevoree.h"
#include "json.h"
#include "jsonparse.h"

#include <stdlib.h>

#define NB_CLASSES 8
#define ContainerRoot_Type_SIZE 8

char* parseStr(struct jsonparse_state *state);


typedef enum TYPE {
  ContainerRoot_Type = 0, //FIXME values just for debug purposes
  Group_Type = 1,
  ContainerNode_Type = 2,
  TypeDefinition_Type = 4,
  Repository_Type = 5,
  TypeLibrary_Type = 6,
  TypedElement_Type = 7,
  Primitive_Type = 3 // beause null is not accepted
} TYPE;

typedef void (*fptrSetter)(struct jsonparse_state *state, void* object, TYPE obj_type, TYPE ptr_type);

struct at {
  char* attr_name;
  fptrSetter setter; // fptr to parse result from json
  TYPE obj_type; // NULL if primitive
  TYPE ptr_type;
};

struct ClassType {
  TYPE type;
  int nb_attributes;
  struct at* attributes;
};

const struct ClassType getClass(TYPE type);

//typedef char* (*fptrParseStr)(struct jsonparse_state*);
//typedef void (*fptrParser)(struct jsonparse_state*, fptrSetter*);

typedef void* (*fptrConstruct)();

fptrSetter getSetterFunc(struct ClassType ctype, char* name);

void parseObject(struct jsonparse_state *state, void* o, TYPE obj_type, TYPE ptr_type);
void parseArray(struct jsonparse_state *state, void* o, TYPE obj_type, TYPE ptr_type);


#endif
