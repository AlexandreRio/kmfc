#ifndef __jsondeserializer_H
#define __jsondeserializer_H

#include "kevoree.h"
#include "json.h"
#include "jsonparse.h"

#include <stdlib.h>

char* parseStr(struct jsonparse_state *state);

#define NB_CLASSES 35

#define Dictionary_NB_ATTR 3
#define Group_NB_ATTR 9
#define MessagePortType_NB_ATTR 12
#define FragmentDictionary_NB_ATTR 4
#define Instance_NB_ATTR 8
#define Port_NB_ATTR 5
#define ServicePortType_NB_ATTR 13
#define TypedElement_NB_ATTR 4
#define MBinding_NB_ATTR 4
#define DeployUnit_NB_ATTR 9
#define Operation_NB_ATTR 5
#define DictionaryType_NB_ATTR 3
#define TypeDefinition_NB_ATTR 10
#define PortTypeMapping_NB_ATTR 5
#define ComponentInstance_NB_ATTR 11
#define ContainerNode_NB_ATTR 13
#define NodeNetwork_NB_ATTR 5
#define ComponentType_NB_ATTR 12
#define NetworkInfo_NB_ATTR 4
#define Channel_NB_ATTR 9
#define DictionaryAttribute_NB_ATTR 9
#define DictionaryValue_NB_ATTR 3
#define PortTypeRef_NB_ATTR 7
#define TypeLibrary_NB_ATTR 4
#define _Namespace_NB_ATTR 4
#define NetworkProperty_NB_ATTR 4
#define Repository_NB_ATTR 2
#define GroupType_NB_ATTR 10
#define NodeType_NB_ATTR 10
#define ContainerRoot_NB_ATTR 12
#define Parameter_NB_ATTR 5
#define NodeLink_NB_ATTR 7
#define NamedElement_NB_ATTR 3
#define ChannelType_NB_ATTR 14
#define PortType_NB_ATTR 11

typedef enum TYPE {
	DICTIONARY_TYPE,
	GROUP_TYPE,
	MESSAGEPORTTYPE_TYPE,
	FRAGMENTDICTIONARY_TYPE,
	INSTANCE_TYPE,
	PORT_TYPE,
	SERVICEPORTTYPE_TYPE,
	TYPEDELEMENT_TYPE,
	MBINDING_TYPE,
	DEPLOYUNIT_TYPE,
	OPERATION_TYPE,
	DICTIONARYTYPE_TYPE,
	TYPEDEFINITION_TYPE,
	PORTTYPEMAPPING_TYPE,
	COMPONENTINSTANCE_TYPE,
	CONTAINERNODE_TYPE,
	NODENETWORK_TYPE,
	COMPONENTTYPE_TYPE,
	NETWORKINFO_TYPE,
	CHANNEL_TYPE,
	DICTIONARYATTRIBUTE_TYPE,
	DICTIONARYVALUE_TYPE,
	PORTTYPEREF_TYPE,
	TYPELIBRARY_TYPE,
	_NAMESPACE_TYPE,
	NETWORKPROPERTY_TYPE,
	REPOSITORY_TYPE,
	GROUPTYPE_TYPE,
	NODETYPE_TYPE,
	CONTAINERROOT_TYPE,
	PARAMETER_TYPE,
	NODELINK_TYPE,
	NAMEDELEMENT_TYPE,
	CHANNELTYPE_TYPE,
	PORTTYPE_TYPE,
	PRIMITIVE_TYPE
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
