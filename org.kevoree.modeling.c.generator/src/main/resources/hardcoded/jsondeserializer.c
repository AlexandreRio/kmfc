#include "jsondeserializer.h"

char attr[200];

void ContainerRootSetKMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type);
void doNothing(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type);


void DictionarySetgenerated_KMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
	void* dest = ((Dictionary*)o)->generated_KMF_ID;
	if (strlen(param) < 9)
		strcpy(dest, param);
}

void DictionarySetvalues(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void GroupSetsubNodes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void GroupSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Group*)o)->name = param;
}

void GroupSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Group*)o)->internalKey = param;
}

void GroupSetmetaData(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Group*)o)->metaData = param;
}

void GroupSetstarted(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void GroupSettypeDefinition(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void GroupSetdictionary(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void GroupSetfragmentDictionary(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void MessagePortTypeSetfilters(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void MessagePortTypeSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((MessagePortType*)o)->name = param;
}

void MessagePortTypeSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((MessagePortType*)o)->internalKey = param;
}

void MessagePortTypeSetversion(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((MessagePortType*)o)->version = param;
}

void MessagePortTypeSetfactoryBean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((MessagePortType*)o)->factoryBean = param;
}

void MessagePortTypeSetbean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((MessagePortType*)o)->bean = param;
}

void MessagePortTypeSetabstract(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void MessagePortTypeSetdeployUnit(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void MessagePortTypeSetdictionaryType(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void MessagePortTypeSetsuperTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void MessagePortTypeSetsynchrone(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void FragmentDictionarySetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((FragmentDictionary*)o)->name = param;
}

void FragmentDictionarySetgenerated_KMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
	void* dest = ((FragmentDictionary*)o)->generated_KMF_ID;
	if (strlen(param) < 9)
		strcpy(dest, param);
}

void FragmentDictionarySetvalues(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void InstanceSetmetaData(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Instance*)o)->metaData = param;
}

void InstanceSetstarted(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void InstanceSettypeDefinition(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void InstanceSetdictionary(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void InstanceSetfragmentDictionary(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void InstanceSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Instance*)o)->name = param;
}

void InstanceSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Instance*)o)->internalKey = param;
}

void PortSetbindings(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortSetportTypeRef(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Port*)o)->name = param;
}

void PortSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Port*)o)->internalKey = param;
}

void ServicePortTypeSetinterface(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ServicePortType*)o)->interface = param;
}

void ServicePortTypeSetoperations(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ServicePortTypeSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ServicePortType*)o)->name = param;
}

void ServicePortTypeSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ServicePortType*)o)->internalKey = param;
}

void ServicePortTypeSetversion(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ServicePortType*)o)->version = param;
}

void ServicePortTypeSetfactoryBean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ServicePortType*)o)->factoryBean = param;
}

void ServicePortTypeSetbean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ServicePortType*)o)->bean = param;
}

void ServicePortTypeSetabstract(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ServicePortTypeSetdeployUnit(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ServicePortTypeSetdictionaryType(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ServicePortTypeSetsuperTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ServicePortTypeSetsynchrone(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void TypedElementSetgenericTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void TypedElementSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((TypedElement*)o)->name = param;
}

void TypedElementSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((TypedElement*)o)->internalKey = param;
}

void MBindingSetgenerated_KMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
	void* dest = ((MBinding*)o)->generated_KMF_ID;
	if (strlen(param) < 9)
		strcpy(dest, param);
}

void MBindingSetport(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void MBindingSethub(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void DeployUnitSetgroupName(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DeployUnit*)o)->groupName = param;
}

void DeployUnitSetversion(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DeployUnit*)o)->version = param;
}

void DeployUnitSeturl(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DeployUnit*)o)->url = param;
}

void DeployUnitSethashcode(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DeployUnit*)o)->hashcode = param;
}

void DeployUnitSettype(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DeployUnit*)o)->type = param;
}

void DeployUnitSetrequiredLibs(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void DeployUnitSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DeployUnit*)o)->name = param;
}

void DeployUnitSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DeployUnit*)o)->internalKey = param;
}

void OperationSetparameters(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void OperationSetreturnType(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void OperationSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Operation*)o)->name = param;
}

void OperationSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Operation*)o)->internalKey = param;
}

void DictionaryTypeSetgenerated_KMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
	void* dest = ((DictionaryType*)o)->generated_KMF_ID;
	if (strlen(param) < 9)
		strcpy(dest, param);
}

void DictionaryTypeSetattributes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void TypeDefinitionSetversion(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((TypeDefinition*)o)->version = param;
}

void TypeDefinitionSetfactoryBean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((TypeDefinition*)o)->factoryBean = param;
}

void TypeDefinitionSetbean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((TypeDefinition*)o)->bean = param;
}

void TypeDefinitionSetabstract(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void TypeDefinitionSetdeployUnit(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void TypeDefinitionSetdictionaryType(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void TypeDefinitionSetsuperTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void TypeDefinitionSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((TypeDefinition*)o)->name = param;
}

void TypeDefinitionSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((TypeDefinition*)o)->internalKey = param;
}

void PortTypeMappingSetbeanMethodName(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((PortTypeMapping*)o)->beanMethodName = param;
}

void PortTypeMappingSetserviceMethodName(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((PortTypeMapping*)o)->serviceMethodName = param;
}

void PortTypeMappingSetparamTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((PortTypeMapping*)o)->paramTypes = param;
}

void PortTypeMappingSetgenerated_KMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
	void* dest = ((PortTypeMapping*)o)->generated_KMF_ID;
	if (strlen(param) < 9)
		strcpy(dest, param);
}

void ComponentInstanceSetprovided(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentInstanceSetrequired(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentInstanceSet_namespace(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentInstanceSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ComponentInstance*)o)->name = param;
}

void ComponentInstanceSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ComponentInstance*)o)->internalKey = param;
}

void ComponentInstanceSetmetaData(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ComponentInstance*)o)->metaData = param;
}

void ComponentInstanceSetstarted(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentInstanceSettypeDefinition(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentInstanceSetdictionary(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentInstanceSetfragmentDictionary(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerNodeSetcomponents(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerNodeSethosts(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerNodeSethost(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerNodeSetgroups(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerNodeSetnetworkInformation(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerNodeSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ContainerNode*)o)->name = param;
}

void ContainerNodeSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ContainerNode*)o)->internalKey = param;
}

void ContainerNodeSetmetaData(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ContainerNode*)o)->metaData = param;
}

void ContainerNodeSetstarted(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerNodeSettypeDefinition(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerNodeSetdictionary(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerNodeSetfragmentDictionary(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NodeNetworkSetgenerated_KMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
	void* dest = ((NodeNetwork*)o)->generated_KMF_ID;
	if (strlen(param) < 9)
		strcpy(dest, param);
}

void NodeNetworkSetlink(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NodeNetworkSetinitBy(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NodeNetworkSettarget(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentTypeSetrequired(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentTypeSetprovided(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentTypeSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ComponentType*)o)->name = param;
}

void ComponentTypeSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ComponentType*)o)->internalKey = param;
}

void ComponentTypeSetversion(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ComponentType*)o)->version = param;
}

void ComponentTypeSetfactoryBean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ComponentType*)o)->factoryBean = param;
}

void ComponentTypeSetbean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ComponentType*)o)->bean = param;
}

void ComponentTypeSetabstract(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentTypeSetdeployUnit(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentTypeSetdictionaryType(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ComponentTypeSetsuperTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NetworkInfoSetvalues(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NetworkInfoSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NetworkInfo*)o)->name = param;
}

void NetworkInfoSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NetworkInfo*)o)->internalKey = param;
}

void ChannelSetbindings(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Channel*)o)->name = param;
}

void ChannelSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Channel*)o)->internalKey = param;
}

void ChannelSetmetaData(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Channel*)o)->metaData = param;
}

void ChannelSetstarted(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelSettypeDefinition(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelSetdictionary(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelSetfragmentDictionary(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void DictionaryAttributeSetoptional(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void DictionaryAttributeSetstate(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void DictionaryAttributeSetdatatype(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DictionaryAttribute*)o)->datatype = param;
}

void DictionaryAttributeSetfragmentDependant(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void DictionaryAttributeSetdefaultValue(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DictionaryAttribute*)o)->defaultValue = param;
}

void DictionaryAttributeSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DictionaryAttribute*)o)->name = param;
}

void DictionaryAttributeSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DictionaryAttribute*)o)->internalKey = param;
}

void DictionaryAttributeSetgenericTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void DictionaryValueSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DictionaryValue*)o)->name = param;
}

void DictionaryValueSetvalue(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((DictionaryValue*)o)->value = param;
}

void PortTypeRefSetoptional(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortTypeRefSetnoDependency(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortTypeRefSetref(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortTypeRefSetmappings(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortTypeRefSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((PortTypeRef*)o)->name = param;
}

void PortTypeRefSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((PortTypeRef*)o)->internalKey = param;
}

void TypeLibrarySetsubTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void TypeLibrarySetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((TypeLibrary*)o)->name = param;
}

void TypeLibrarySetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((TypeLibrary*)o)->internalKey = param;
}

void _NamespaceSetelements(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void _NamespaceSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((_Namespace*)o)->name = param;
}

void _NamespaceSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((_Namespace*)o)->internalKey = param;
}

void NetworkPropertySetvalue(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NetworkProperty*)o)->value = param;
}

void NetworkPropertySetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NetworkProperty*)o)->name = param;
}

void NetworkPropertySetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NetworkProperty*)o)->internalKey = param;
}

void RepositorySeturl(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Repository*)o)->url = param;
}

void GroupTypeSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((GroupType*)o)->name = param;
}

void GroupTypeSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((GroupType*)o)->internalKey = param;
}

void GroupTypeSetversion(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((GroupType*)o)->version = param;
}

void GroupTypeSetfactoryBean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((GroupType*)o)->factoryBean = param;
}

void GroupTypeSetbean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((GroupType*)o)->bean = param;
}

void GroupTypeSetabstract(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void GroupTypeSetdeployUnit(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void GroupTypeSetdictionaryType(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void GroupTypeSetsuperTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NodeTypeSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NodeType*)o)->name = param;
}

void NodeTypeSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NodeType*)o)->internalKey = param;
}

void NodeTypeSetversion(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NodeType*)o)->version = param;
}

void NodeTypeSetfactoryBean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NodeType*)o)->factoryBean = param;
}

void NodeTypeSetbean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NodeType*)o)->bean = param;
}

void NodeTypeSetabstract(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NodeTypeSetdeployUnit(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NodeTypeSetdictionaryType(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NodeTypeSetsuperTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSetgenerated_KMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
	void* dest = ((ContainerRoot*)o)->generated_KMF_ID;
	if (strlen(param) < 9)
		strcpy(dest, param);
}

void ContainerRootSetnodes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSettypeDefinitions(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSetrepositories(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSetdataTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSetlibraries(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSethubs(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSetmBindings(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSetdeployUnits(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSetnodeNetworks(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ContainerRootSetgroups(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ParameterSetorder(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ParameterSettype(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ParameterSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Parameter*)o)->name = param;
}

void ParameterSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((Parameter*)o)->internalKey = param;
}

void NodeLinkSetnetworkType(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NodeLink*)o)->networkType = param;
}

void NodeLinkSetestimatedRate(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NodeLinkSetlastCheck(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NodeLink*)o)->lastCheck = param;
}

void NodeLinkSetzoneID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NodeLink*)o)->zoneID = param;
}

void NodeLinkSetgenerated_KMF_ID(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
	void* dest = ((NodeLink*)o)->generated_KMF_ID;
	if (strlen(param) < 9)
		strcpy(dest, param);
}

void NodeLinkSetnetworkProperties(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void NamedElementSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NamedElement*)o)->name = param;
}

void NamedElementSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((NamedElement*)o)->internalKey = param;
}

void ChannelTypeSetlowerBindings(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelTypeSetupperBindings(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelTypeSetlowerFragments(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelTypeSetupperFragments(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelTypeSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ChannelType*)o)->name = param;
}

void ChannelTypeSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ChannelType*)o)->internalKey = param;
}

void ChannelTypeSetversion(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ChannelType*)o)->version = param;
}

void ChannelTypeSetfactoryBean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ChannelType*)o)->factoryBean = param;
}

void ChannelTypeSetbean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((ChannelType*)o)->bean = param;
}

void ChannelTypeSetabstract(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelTypeSetdeployUnit(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelTypeSetdictionaryType(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void ChannelTypeSetsuperTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortTypeSetsynchrone(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortTypeSetname(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((PortType*)o)->name = param;
}

void PortTypeSetinternalKey(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((PortType*)o)->internalKey = param;
}

void PortTypeSetversion(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((PortType*)o)->version = param;
}

void PortTypeSetfactoryBean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((PortType*)o)->factoryBean = param;
}

void PortTypeSetbean(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
	char* param = parseStr(state);
((PortType*)o)->bean = param;
}

void PortTypeSetabstract(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortTypeSetdeployUnit(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortTypeSetdictionaryType(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

void PortTypeSetsuperTypes(struct jsonparse_state* state, void* o, TYPE obj_type, TYPE ptr_type)
{
}

const struct at Dictionary_Attr[Dictionary_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"generated_KMF_ID", parseStr, DICTIONARY_TYPE, PRIMITIVE_TYPE},
{"values", parseArray, DICTIONARY_TYPE, DICTIONARYVALUE_TYPE},
};

const struct at Group_Attr[Group_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"subNodes", parseArray, GROUP_TYPE, CONTAINERNODE_TYPE},
{"name", ContainerRootSetKMF_ID, GROUP_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, GROUP_TYPE, PRIMITIVE_TYPE},
{"metaData", ContainerRootSetKMF_ID, GROUP_TYPE, PRIMITIVE_TYPE},
{"started", ContainerRootSetKMF_ID, GROUP_TYPE, PRIMITIVE_TYPE},
{"typeDefinition", parseObject, GROUP_TYPE, TYPEDEFINITION_TYPE},
{"dictionary", parseObject, GROUP_TYPE, DICTIONARY_TYPE},
{"fragmentDictionary", parseArray, GROUP_TYPE, FRAGMENTDICTIONARY_TYPE},
};

const struct at MessagePortType_Attr[MessagePortType_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"filters", parseArray, MESSAGEPORTTYPE_TYPE, TYPEDELEMENT_TYPE},
{"name", ContainerRootSetKMF_ID, MESSAGEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, MESSAGEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"version", ContainerRootSetKMF_ID, MESSAGEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"factoryBean", ContainerRootSetKMF_ID, MESSAGEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"bean", ContainerRootSetKMF_ID, MESSAGEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"abstract", ContainerRootSetKMF_ID, MESSAGEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"deployUnit", parseObject, MESSAGEPORTTYPE_TYPE, DEPLOYUNIT_TYPE},
{"dictionaryType", parseObject, MESSAGEPORTTYPE_TYPE, DICTIONARYTYPE_TYPE},
{"superTypes", parseArray, MESSAGEPORTTYPE_TYPE, TYPEDEFINITION_TYPE},
{"synchrone", ContainerRootSetKMF_ID, MESSAGEPORTTYPE_TYPE, PRIMITIVE_TYPE},
};

const struct at FragmentDictionary_Attr[FragmentDictionary_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"name", ContainerRootSetKMF_ID, FRAGMENTDICTIONARY_TYPE, PRIMITIVE_TYPE},
{"generated_KMF_ID", ContainerRootSetKMF_ID, FRAGMENTDICTIONARY_TYPE, PRIMITIVE_TYPE},
{"values", parseArray, FRAGMENTDICTIONARY_TYPE, DICTIONARYVALUE_TYPE},
};

const struct at Instance_Attr[Instance_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"metaData", ContainerRootSetKMF_ID, INSTANCE_TYPE, PRIMITIVE_TYPE},
{"started", ContainerRootSetKMF_ID, INSTANCE_TYPE, PRIMITIVE_TYPE},
{"typeDefinition", parseObject, INSTANCE_TYPE, TYPEDEFINITION_TYPE},
{"dictionary", parseObject, INSTANCE_TYPE, DICTIONARY_TYPE},
{"fragmentDictionary", parseArray, INSTANCE_TYPE, FRAGMENTDICTIONARY_TYPE},
{"name", ContainerRootSetKMF_ID, INSTANCE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, INSTANCE_TYPE, PRIMITIVE_TYPE},
};

const struct at Port_Attr[Port_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"bindings", parseArray, PORT_TYPE, MBINDING_TYPE},
{"portTypeRef", parseObject, PORT_TYPE, PORTTYPEREF_TYPE},
{"name", ContainerRootSetKMF_ID, PORT_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, PORT_TYPE, PRIMITIVE_TYPE},
};

const struct at ServicePortType_Attr[ServicePortType_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"interface", ContainerRootSetKMF_ID, SERVICEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"operations", parseArray, SERVICEPORTTYPE_TYPE, OPERATION_TYPE},
{"name", ContainerRootSetKMF_ID, SERVICEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, SERVICEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"version", ContainerRootSetKMF_ID, SERVICEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"factoryBean", ContainerRootSetKMF_ID, SERVICEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"bean", ContainerRootSetKMF_ID, SERVICEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"abstract", ContainerRootSetKMF_ID, SERVICEPORTTYPE_TYPE, PRIMITIVE_TYPE},
{"deployUnit", parseObject, SERVICEPORTTYPE_TYPE, DEPLOYUNIT_TYPE},
{"dictionaryType", parseObject, SERVICEPORTTYPE_TYPE, DICTIONARYTYPE_TYPE},
{"superTypes", parseArray, SERVICEPORTTYPE_TYPE, TYPEDEFINITION_TYPE},
{"synchrone", ContainerRootSetKMF_ID, SERVICEPORTTYPE_TYPE, PRIMITIVE_TYPE},
};

const struct at TypedElement_Attr[TypedElement_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"genericTypes", parseArray, TYPEDELEMENT_TYPE, TYPEDELEMENT_TYPE},
{"name", ContainerRootSetKMF_ID, TYPEDELEMENT_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, TYPEDELEMENT_TYPE, PRIMITIVE_TYPE},
};

const struct at MBinding_Attr[MBinding_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"generated_KMF_ID", ContainerRootSetKMF_ID, MBINDING_TYPE, PRIMITIVE_TYPE},
{"port", parseObject, MBINDING_TYPE, PORT_TYPE},
{"hub", parseObject, MBINDING_TYPE, CHANNEL_TYPE},
};

const struct at DeployUnit_Attr[DeployUnit_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"groupName", ContainerRootSetKMF_ID, DEPLOYUNIT_TYPE, PRIMITIVE_TYPE},
{"version", ContainerRootSetKMF_ID, DEPLOYUNIT_TYPE, PRIMITIVE_TYPE},
{"url", ContainerRootSetKMF_ID, DEPLOYUNIT_TYPE, PRIMITIVE_TYPE},
{"hashcode", ContainerRootSetKMF_ID, DEPLOYUNIT_TYPE, PRIMITIVE_TYPE},
{"type", ContainerRootSetKMF_ID, DEPLOYUNIT_TYPE, PRIMITIVE_TYPE},
{"requiredLibs", parseArray, DEPLOYUNIT_TYPE, DEPLOYUNIT_TYPE},
{"name", ContainerRootSetKMF_ID, DEPLOYUNIT_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, DEPLOYUNIT_TYPE, PRIMITIVE_TYPE},
};

const struct at Operation_Attr[Operation_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"parameters", parseArray, OPERATION_TYPE, PARAMETER_TYPE},
{"returnType", parseObject, OPERATION_TYPE, TYPEDELEMENT_TYPE},
{"name", ContainerRootSetKMF_ID, OPERATION_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, OPERATION_TYPE, PRIMITIVE_TYPE},
};

const struct at DictionaryType_Attr[DictionaryType_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"generated_KMF_ID", ContainerRootSetKMF_ID, DICTIONARYTYPE_TYPE, PRIMITIVE_TYPE},
{"attributes", parseArray, DICTIONARYTYPE_TYPE, DICTIONARYATTRIBUTE_TYPE},
};

const struct at TypeDefinition_Attr[TypeDefinition_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"version", ContainerRootSetKMF_ID, TYPEDEFINITION_TYPE, PRIMITIVE_TYPE},
{"factoryBean", ContainerRootSetKMF_ID, TYPEDEFINITION_TYPE, PRIMITIVE_TYPE},
{"bean", ContainerRootSetKMF_ID, TYPEDEFINITION_TYPE, PRIMITIVE_TYPE},
{"abstract", ContainerRootSetKMF_ID, TYPEDEFINITION_TYPE, PRIMITIVE_TYPE},
{"deployUnit", parseObject, TYPEDEFINITION_TYPE, DEPLOYUNIT_TYPE},
{"dictionaryType", parseObject, TYPEDEFINITION_TYPE, DICTIONARYTYPE_TYPE},
{"superTypes", parseArray, TYPEDEFINITION_TYPE, TYPEDEFINITION_TYPE},
{"name", ContainerRootSetKMF_ID, TYPEDEFINITION_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, TYPEDEFINITION_TYPE, PRIMITIVE_TYPE},
};

const struct at PortTypeMapping_Attr[PortTypeMapping_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"beanMethodName", ContainerRootSetKMF_ID, PORTTYPEMAPPING_TYPE, PRIMITIVE_TYPE},
{"serviceMethodName", ContainerRootSetKMF_ID, PORTTYPEMAPPING_TYPE, PRIMITIVE_TYPE},
{"paramTypes", ContainerRootSetKMF_ID, PORTTYPEMAPPING_TYPE, PRIMITIVE_TYPE},
{"generated_KMF_ID", ContainerRootSetKMF_ID, PORTTYPEMAPPING_TYPE, PRIMITIVE_TYPE},
};

const struct at ComponentInstance_Attr[ComponentInstance_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"provided", parseArray, COMPONENTINSTANCE_TYPE, PORT_TYPE},
{"required", parseArray, COMPONENTINSTANCE_TYPE, PORT_TYPE},
{"_namespace", parseObject, COMPONENTINSTANCE_TYPE, _NAMESPACE_TYPE},
{"name", ContainerRootSetKMF_ID, COMPONENTINSTANCE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, COMPONENTINSTANCE_TYPE, PRIMITIVE_TYPE},
{"metaData", ContainerRootSetKMF_ID, COMPONENTINSTANCE_TYPE, PRIMITIVE_TYPE},
{"started", ContainerRootSetKMF_ID, COMPONENTINSTANCE_TYPE, PRIMITIVE_TYPE},
{"typeDefinition", parseObject, COMPONENTINSTANCE_TYPE, TYPEDEFINITION_TYPE},
{"dictionary", parseObject, COMPONENTINSTANCE_TYPE, DICTIONARY_TYPE},
{"fragmentDictionary", parseArray, COMPONENTINSTANCE_TYPE, FRAGMENTDICTIONARY_TYPE},
};

const struct at ContainerNode_Attr[ContainerNode_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"components", parseArray, CONTAINERNODE_TYPE, COMPONENTINSTANCE_TYPE},
{"hosts", parseArray, CONTAINERNODE_TYPE, CONTAINERNODE_TYPE},
{"host", parseObject, CONTAINERNODE_TYPE, CONTAINERNODE_TYPE},
{"groups", parseArray, CONTAINERNODE_TYPE, GROUP_TYPE},
{"networkInformation", parseArray, CONTAINERNODE_TYPE, NETWORKINFO_TYPE},
{"name", ContainerRootSetKMF_ID, CONTAINERNODE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, CONTAINERNODE_TYPE, PRIMITIVE_TYPE},
{"metaData", ContainerRootSetKMF_ID, CONTAINERNODE_TYPE, PRIMITIVE_TYPE},
{"started", ContainerRootSetKMF_ID, CONTAINERNODE_TYPE, PRIMITIVE_TYPE},
{"typeDefinition", parseObject, CONTAINERNODE_TYPE, TYPEDEFINITION_TYPE},
{"dictionary", parseObject, CONTAINERNODE_TYPE, DICTIONARY_TYPE},
{"fragmentDictionary", parseArray, CONTAINERNODE_TYPE, FRAGMENTDICTIONARY_TYPE},
};

const struct at NodeNetwork_Attr[NodeNetwork_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"generated_KMF_ID", ContainerRootSetKMF_ID, NODENETWORK_TYPE, PRIMITIVE_TYPE},
{"link", parseArray, NODENETWORK_TYPE, NODELINK_TYPE},
{"initBy", parseObject, NODENETWORK_TYPE, CONTAINERNODE_TYPE},
{"target", parseObject, NODENETWORK_TYPE, CONTAINERNODE_TYPE},
};

const struct at ComponentType_Attr[ComponentType_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"required", parseArray, COMPONENTTYPE_TYPE, PORTTYPEREF_TYPE},
{"provided", parseArray, COMPONENTTYPE_TYPE, PORTTYPEREF_TYPE},
{"name", ContainerRootSetKMF_ID, COMPONENTTYPE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, COMPONENTTYPE_TYPE, PRIMITIVE_TYPE},
{"version", ContainerRootSetKMF_ID, COMPONENTTYPE_TYPE, PRIMITIVE_TYPE},
{"factoryBean", ContainerRootSetKMF_ID, COMPONENTTYPE_TYPE, PRIMITIVE_TYPE},
{"bean", ContainerRootSetKMF_ID, COMPONENTTYPE_TYPE, PRIMITIVE_TYPE},
{"abstract", ContainerRootSetKMF_ID, COMPONENTTYPE_TYPE, PRIMITIVE_TYPE},
{"deployUnit", parseObject, COMPONENTTYPE_TYPE, DEPLOYUNIT_TYPE},
{"dictionaryType", parseObject, COMPONENTTYPE_TYPE, DICTIONARYTYPE_TYPE},
{"superTypes", parseArray, COMPONENTTYPE_TYPE, TYPEDEFINITION_TYPE},
};

const struct at NetworkInfo_Attr[NetworkInfo_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"values", parseArray, NETWORKINFO_TYPE, NETWORKPROPERTY_TYPE},
{"name", ContainerRootSetKMF_ID, NETWORKINFO_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, NETWORKINFO_TYPE, PRIMITIVE_TYPE},
};

const struct at Channel_Attr[Channel_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"bindings", parseArray, CHANNEL_TYPE, MBINDING_TYPE},
{"name", ContainerRootSetKMF_ID, CHANNEL_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, CHANNEL_TYPE, PRIMITIVE_TYPE},
{"metaData", ContainerRootSetKMF_ID, CHANNEL_TYPE, PRIMITIVE_TYPE},
{"started", ContainerRootSetKMF_ID, CHANNEL_TYPE, PRIMITIVE_TYPE},
{"typeDefinition", parseObject, CHANNEL_TYPE, TYPEDEFINITION_TYPE},
{"dictionary", parseObject, CHANNEL_TYPE, DICTIONARY_TYPE},
{"fragmentDictionary", parseArray, CHANNEL_TYPE, FRAGMENTDICTIONARY_TYPE},
};

const struct at DictionaryAttribute_Attr[DictionaryAttribute_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"optional", ContainerRootSetKMF_ID, DICTIONARYATTRIBUTE_TYPE, PRIMITIVE_TYPE},
{"state", ContainerRootSetKMF_ID, DICTIONARYATTRIBUTE_TYPE, PRIMITIVE_TYPE},
{"datatype", ContainerRootSetKMF_ID, DICTIONARYATTRIBUTE_TYPE, PRIMITIVE_TYPE},
{"fragmentDependant", ContainerRootSetKMF_ID, DICTIONARYATTRIBUTE_TYPE, PRIMITIVE_TYPE},
{"defaultValue", ContainerRootSetKMF_ID, DICTIONARYATTRIBUTE_TYPE, PRIMITIVE_TYPE},
{"name", ContainerRootSetKMF_ID, DICTIONARYATTRIBUTE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, DICTIONARYATTRIBUTE_TYPE, PRIMITIVE_TYPE},
{"genericTypes", parseArray, DICTIONARYATTRIBUTE_TYPE, TYPEDELEMENT_TYPE},
};

const struct at DictionaryValue_Attr[DictionaryValue_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"name", ContainerRootSetKMF_ID, DICTIONARYVALUE_TYPE, PRIMITIVE_TYPE},
{"value", ContainerRootSetKMF_ID, DICTIONARYVALUE_TYPE, PRIMITIVE_TYPE},
};

const struct at PortTypeRef_Attr[PortTypeRef_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"optional", ContainerRootSetKMF_ID, PORTTYPEREF_TYPE, PRIMITIVE_TYPE},
{"noDependency", ContainerRootSetKMF_ID, PORTTYPEREF_TYPE, PRIMITIVE_TYPE},
{"ref", parseObject, PORTTYPEREF_TYPE, PORTTYPE_TYPE},
{"mappings", parseArray, PORTTYPEREF_TYPE, PORTTYPEMAPPING_TYPE},
{"name", ContainerRootSetKMF_ID, PORTTYPEREF_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, PORTTYPEREF_TYPE, PRIMITIVE_TYPE},
};

const struct at TypeLibrary_Attr[TypeLibrary_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"subTypes", parseArray, TYPELIBRARY_TYPE, TYPEDEFINITION_TYPE},
{"name", ContainerRootSetKMF_ID, TYPELIBRARY_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, TYPELIBRARY_TYPE, PRIMITIVE_TYPE},
};

const struct at _Namespace_Attr[_Namespace_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"elements", parseArray, _NAMESPACE_TYPE, INSTANCE_TYPE},
{"name", ContainerRootSetKMF_ID, _NAMESPACE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, _NAMESPACE_TYPE, PRIMITIVE_TYPE},
};

const struct at NetworkProperty_Attr[NetworkProperty_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"value", ContainerRootSetKMF_ID, NETWORKPROPERTY_TYPE, PRIMITIVE_TYPE},
{"name", ContainerRootSetKMF_ID, NETWORKPROPERTY_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, NETWORKPROPERTY_TYPE, PRIMITIVE_TYPE},
};

const struct at Repository_Attr[Repository_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"url", ContainerRootSetKMF_ID, REPOSITORY_TYPE, PRIMITIVE_TYPE},
};

const struct at GroupType_Attr[GroupType_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"name", ContainerRootSetKMF_ID, GROUPTYPE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, GROUPTYPE_TYPE, PRIMITIVE_TYPE},
{"version", ContainerRootSetKMF_ID, GROUPTYPE_TYPE, PRIMITIVE_TYPE},
{"factoryBean", ContainerRootSetKMF_ID, GROUPTYPE_TYPE, PRIMITIVE_TYPE},
{"bean", ContainerRootSetKMF_ID, GROUPTYPE_TYPE, PRIMITIVE_TYPE},
{"abstract", ContainerRootSetKMF_ID, GROUPTYPE_TYPE, PRIMITIVE_TYPE},
{"deployUnit", parseObject, GROUPTYPE_TYPE, DEPLOYUNIT_TYPE},
{"dictionaryType", parseObject, GROUPTYPE_TYPE, DICTIONARYTYPE_TYPE},
{"superTypes", parseArray, GROUPTYPE_TYPE, TYPEDEFINITION_TYPE},
};

const struct at NodeType_Attr[NodeType_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"name", ContainerRootSetKMF_ID, NODETYPE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, NODETYPE_TYPE, PRIMITIVE_TYPE},
{"version", ContainerRootSetKMF_ID, NODETYPE_TYPE, PRIMITIVE_TYPE},
{"factoryBean", ContainerRootSetKMF_ID, NODETYPE_TYPE, PRIMITIVE_TYPE},
{"bean", ContainerRootSetKMF_ID, NODETYPE_TYPE, PRIMITIVE_TYPE},
{"abstract", ContainerRootSetKMF_ID, NODETYPE_TYPE, PRIMITIVE_TYPE},
{"deployUnit", parseObject, NODETYPE_TYPE, DEPLOYUNIT_TYPE},
{"dictionaryType", parseObject, NODETYPE_TYPE, DICTIONARYTYPE_TYPE},
{"superTypes", parseArray, NODETYPE_TYPE, TYPEDEFINITION_TYPE},
};

const struct at ContainerRoot_Attr[ContainerRoot_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"generated_KMF_ID", ContainerRootSetKMF_ID, CONTAINERROOT_TYPE, PRIMITIVE_TYPE},
{"nodes", parseArray, CONTAINERROOT_TYPE, CONTAINERNODE_TYPE},
{"typeDefinitions", parseArray, CONTAINERROOT_TYPE, TYPEDEFINITION_TYPE},
{"repositories", parseArray, CONTAINERROOT_TYPE, REPOSITORY_TYPE},
{"dataTypes", parseArray, CONTAINERROOT_TYPE, TYPEDELEMENT_TYPE},
{"libraries", parseArray, CONTAINERROOT_TYPE, TYPELIBRARY_TYPE},
{"hubs", parseArray, CONTAINERROOT_TYPE, CHANNEL_TYPE},
{"mBindings", parseArray, CONTAINERROOT_TYPE, MBINDING_TYPE},
{"deployUnits", parseArray, CONTAINERROOT_TYPE, DEPLOYUNIT_TYPE},
{"nodeNetworks", parseArray, CONTAINERROOT_TYPE, NODENETWORK_TYPE},
{"groups", parseArray, CONTAINERROOT_TYPE, GROUP_TYPE},
};

const struct at Parameter_Attr[Parameter_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"order", ContainerRootSetKMF_ID, PARAMETER_TYPE, PRIMITIVE_TYPE},
{"type", parseObject, PARAMETER_TYPE, TYPEDELEMENT_TYPE},
{"name", ContainerRootSetKMF_ID, PARAMETER_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, PARAMETER_TYPE, PRIMITIVE_TYPE},
};

const struct at NodeLink_Attr[NodeLink_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"networkType", ContainerRootSetKMF_ID, NODELINK_TYPE, PRIMITIVE_TYPE},
{"estimatedRate", ContainerRootSetKMF_ID, NODELINK_TYPE, PRIMITIVE_TYPE},
{"lastCheck", ContainerRootSetKMF_ID, NODELINK_TYPE, PRIMITIVE_TYPE},
{"zoneID", ContainerRootSetKMF_ID, NODELINK_TYPE, PRIMITIVE_TYPE},
{"generated_KMF_ID", ContainerRootSetKMF_ID, NODELINK_TYPE, PRIMITIVE_TYPE},
{"networkProperties", parseArray, NODELINK_TYPE, NETWORKPROPERTY_TYPE},
};

const struct at NamedElement_Attr[NamedElement_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"name", ContainerRootSetKMF_ID, NAMEDELEMENT_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, NAMEDELEMENT_TYPE, PRIMITIVE_TYPE},
};

const struct at ChannelType_Attr[ChannelType_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"lowerBindings", ContainerRootSetKMF_ID, CHANNELTYPE_TYPE, PRIMITIVE_TYPE},
{"upperBindings", ContainerRootSetKMF_ID, CHANNELTYPE_TYPE, PRIMITIVE_TYPE},
{"lowerFragments", ContainerRootSetKMF_ID, CHANNELTYPE_TYPE, PRIMITIVE_TYPE},
{"upperFragments", ContainerRootSetKMF_ID, CHANNELTYPE_TYPE, PRIMITIVE_TYPE},
{"name", ContainerRootSetKMF_ID, CHANNELTYPE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, CHANNELTYPE_TYPE, PRIMITIVE_TYPE},
{"version", ContainerRootSetKMF_ID, CHANNELTYPE_TYPE, PRIMITIVE_TYPE},
{"factoryBean", ContainerRootSetKMF_ID, CHANNELTYPE_TYPE, PRIMITIVE_TYPE},
{"bean", ContainerRootSetKMF_ID, CHANNELTYPE_TYPE, PRIMITIVE_TYPE},
{"abstract", ContainerRootSetKMF_ID, CHANNELTYPE_TYPE, PRIMITIVE_TYPE},
{"deployUnit", parseObject, CHANNELTYPE_TYPE, DEPLOYUNIT_TYPE},
{"dictionaryType", parseObject, CHANNELTYPE_TYPE, DICTIONARYTYPE_TYPE},
{"superTypes", parseArray, CHANNELTYPE_TYPE, TYPEDEFINITION_TYPE},
};

const struct at PortType_Attr[PortType_NB_ATTR] = {
{"eClass", doNothing, PRIMITIVE_TYPE, PRIMITIVE_TYPE},
{"synchrone", ContainerRootSetKMF_ID, PORTTYPE_TYPE, PRIMITIVE_TYPE},
{"name", ContainerRootSetKMF_ID, PORTTYPE_TYPE, PRIMITIVE_TYPE},
{"internalKey", ContainerRootSetKMF_ID, PORTTYPE_TYPE, PRIMITIVE_TYPE},
{"version", ContainerRootSetKMF_ID, PORTTYPE_TYPE, PRIMITIVE_TYPE},
{"factoryBean", ContainerRootSetKMF_ID, PORTTYPE_TYPE, PRIMITIVE_TYPE},
{"bean", ContainerRootSetKMF_ID, PORTTYPE_TYPE, PRIMITIVE_TYPE},
{"abstract", ContainerRootSetKMF_ID, PORTTYPE_TYPE, PRIMITIVE_TYPE},
{"deployUnit", parseObject, PORTTYPE_TYPE, DEPLOYUNIT_TYPE},
{"dictionaryType", parseObject, PORTTYPE_TYPE, DICTIONARYTYPE_TYPE},
{"superTypes", parseArray, PORTTYPE_TYPE, TYPEDEFINITION_TYPE},
};

const struct ClassType Classes[NB_CLASSES] = {
	{
		.type = DICTIONARY_TYPE,
		.attributes = &Dictionary_Attr,
		.nb_attributes = Dictionary_NB_ATTR,
	},
	{
		.type = GROUP_TYPE,
		.attributes = &Group_Attr,
		.nb_attributes = Group_NB_ATTR,
	},
	{
		.type = MESSAGEPORTTYPE_TYPE,
		.attributes = &MessagePortType_Attr,
		.nb_attributes = MessagePortType_NB_ATTR,
	},
	{
		.type = FRAGMENTDICTIONARY_TYPE,
		.attributes = &FragmentDictionary_Attr,
		.nb_attributes = FragmentDictionary_NB_ATTR,
	},
	{
		.type = INSTANCE_TYPE,
		.attributes = &Instance_Attr,
		.nb_attributes = Instance_NB_ATTR,
	},
	{
		.type = PORT_TYPE,
		.attributes = &Port_Attr,
		.nb_attributes = Port_NB_ATTR,
	},
	{
		.type = SERVICEPORTTYPE_TYPE,
		.attributes = &ServicePortType_Attr,
		.nb_attributes = ServicePortType_NB_ATTR,
	},
	{
		.type = TYPEDELEMENT_TYPE,
		.attributes = &TypedElement_Attr,
		.nb_attributes = TypedElement_NB_ATTR,
	},
	{
		.type = MBINDING_TYPE,
		.attributes = &MBinding_Attr,
		.nb_attributes = MBinding_NB_ATTR,
	},
	{
		.type = DEPLOYUNIT_TYPE,
		.attributes = &DeployUnit_Attr,
		.nb_attributes = DeployUnit_NB_ATTR,
	},
	{
		.type = OPERATION_TYPE,
		.attributes = &Operation_Attr,
		.nb_attributes = Operation_NB_ATTR,
	},
	{
		.type = DICTIONARYTYPE_TYPE,
		.attributes = &DictionaryType_Attr,
		.nb_attributes = DictionaryType_NB_ATTR,
	},
	{
		.type = TYPEDEFINITION_TYPE,
		.attributes = &TypeDefinition_Attr,
		.nb_attributes = TypeDefinition_NB_ATTR,
	},
	{
		.type = PORTTYPEMAPPING_TYPE,
		.attributes = &PortTypeMapping_Attr,
		.nb_attributes = PortTypeMapping_NB_ATTR,
	},
	{
		.type = COMPONENTINSTANCE_TYPE,
		.attributes = &ComponentInstance_Attr,
		.nb_attributes = ComponentInstance_NB_ATTR,
	},
	{
		.type = CONTAINERNODE_TYPE,
		.attributes = &ContainerNode_Attr,
		.nb_attributes = ContainerNode_NB_ATTR,
	},
	{
		.type = NODENETWORK_TYPE,
		.attributes = &NodeNetwork_Attr,
		.nb_attributes = NodeNetwork_NB_ATTR,
	},
	{
		.type = COMPONENTTYPE_TYPE,
		.attributes = &ComponentType_Attr,
		.nb_attributes = ComponentType_NB_ATTR,
	},
	{
		.type = NETWORKINFO_TYPE,
		.attributes = &NetworkInfo_Attr,
		.nb_attributes = NetworkInfo_NB_ATTR,
	},
	{
		.type = CHANNEL_TYPE,
		.attributes = &Channel_Attr,
		.nb_attributes = Channel_NB_ATTR,
	},
	{
		.type = DICTIONARYATTRIBUTE_TYPE,
		.attributes = &DictionaryAttribute_Attr,
		.nb_attributes = DictionaryAttribute_NB_ATTR,
	},
	{
		.type = DICTIONARYVALUE_TYPE,
		.attributes = &DictionaryValue_Attr,
		.nb_attributes = DictionaryValue_NB_ATTR,
	},
	{
		.type = PORTTYPEREF_TYPE,
		.attributes = &PortTypeRef_Attr,
		.nb_attributes = PortTypeRef_NB_ATTR,
	},
	{
		.type = TYPELIBRARY_TYPE,
		.attributes = &TypeLibrary_Attr,
		.nb_attributes = TypeLibrary_NB_ATTR,
	},
	{
		.type = _NAMESPACE_TYPE,
		.attributes = &_Namespace_Attr,
		.nb_attributes = _Namespace_NB_ATTR,
	},
	{
		.type = NETWORKPROPERTY_TYPE,
		.attributes = &NetworkProperty_Attr,
		.nb_attributes = NetworkProperty_NB_ATTR,
	},
	{
		.type = REPOSITORY_TYPE,
		.attributes = &Repository_Attr,
		.nb_attributes = Repository_NB_ATTR,
	},
	{
		.type = GROUPTYPE_TYPE,
		.attributes = &GroupType_Attr,
		.nb_attributes = GroupType_NB_ATTR,
	},
	{
		.type = NODETYPE_TYPE,
		.attributes = &NodeType_Attr,
		.nb_attributes = NodeType_NB_ATTR,
	},
	{
		.type = CONTAINERROOT_TYPE,
		.attributes = &ContainerRoot_Attr,
		.nb_attributes = ContainerRoot_NB_ATTR,
	},
	{
		.type = PARAMETER_TYPE,
		.attributes = &Parameter_Attr,
		.nb_attributes = Parameter_NB_ATTR,
	},
	{
		.type = NODELINK_TYPE,
		.attributes = &NodeLink_Attr,
		.nb_attributes = NodeLink_NB_ATTR,
	},
	{
		.type = NAMEDELEMENT_TYPE,
		.attributes = &NamedElement_Attr,
		.nb_attributes = NamedElement_NB_ATTR,
	},
	{
		.type = CHANNELTYPE_TYPE,
		.attributes = &ChannelType_Attr,
		.nb_attributes = ChannelType_NB_ATTR,
	},
	{
		.type = PORTTYPE_TYPE,
		.attributes = &PortType_Attr,
		.nb_attributes = PortType_NB_ATTR,
	},
};

//Constuctor for abstact type,it
void* new_TypeDefinition()
{
}

void* new_Instance()
{}

void* new_NamedElement()
{}

void* new_PortType()
{}


const fptrConstruct construct[NB_CLASSES] = {
	[DICTIONARY_TYPE] = new_Dictionary,
	[GROUP_TYPE] = new_Group,
	[MESSAGEPORTTYPE_TYPE] = new_MessagePortType,
	[FRAGMENTDICTIONARY_TYPE] = new_FragmentDictionary,
	[INSTANCE_TYPE] = new_Instance,
	[PORT_TYPE] = new_Port,
	[SERVICEPORTTYPE_TYPE] = new_ServicePortType,
	[TYPEDELEMENT_TYPE] = new_TypedElement,
	[MBINDING_TYPE] = new_MBinding,
	[DEPLOYUNIT_TYPE] = new_DeployUnit,
	[OPERATION_TYPE] = new_Operation,
	[DICTIONARYTYPE_TYPE] = new_DictionaryType,
	[TYPEDEFINITION_TYPE] = new_TypeDefinition,
	[PORTTYPEMAPPING_TYPE] = new_PortTypeMapping,
	[COMPONENTINSTANCE_TYPE] = new_ComponentInstance,
	[CONTAINERNODE_TYPE] = new_ContainerNode,
	[NODENETWORK_TYPE] = new_NodeNetwork,
	[COMPONENTTYPE_TYPE] = new_ComponentType,
	[NETWORKINFO_TYPE] = new_NetworkInfo,
	[CHANNEL_TYPE] = new_Channel,
	[DICTIONARYATTRIBUTE_TYPE] = new_DictionaryAttribute,
	[DICTIONARYVALUE_TYPE] = new_DictionaryValue,
	[PORTTYPEREF_TYPE] = new_PortTypeRef,
	[TYPELIBRARY_TYPE] = new_TypeLibrary,
	[_NAMESPACE_TYPE] = new__Namespace,
	[NETWORKPROPERTY_TYPE] = new_NetworkProperty,
	[REPOSITORY_TYPE] = new_Repository,
	[GROUPTYPE_TYPE] = new_GroupType,
	[NODETYPE_TYPE] = new_NodeType,
	[CONTAINERROOT_TYPE] = new_ContainerRoot,
	[PARAMETER_TYPE] = new_Parameter,
	[NODELINK_TYPE] = new_NodeLink,
	[NAMEDELEMENT_TYPE] = new_NamedElement,
	[CHANNELTYPE_TYPE] = new_ChannelType,
	[PORTTYPE_TYPE] = new_PortType,
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
  void* dest;
  switch (obj_type)
  {
    case CONTAINERROOT_TYPE:
      if (strlen(attr) < 9)
      {
        dest = ((ContainerRoot*)o)->generated_KMF_ID;
        strcpy(dest, param);
      }
      break;
    case GROUP_TYPE:
      ((Group*)o)->VT->namedElementAddName(o, param);
      break;
    default:
      dest = NULL;
      break;
  }
  //FIXME ask paco to know how to deal with this
}

void parseArray(struct jsonparse_state *state, void* o, TYPE obj_type, TYPE ptr_type)
{
  char type = JSON_TYPE_ARRAY;
  while((type = jsonparse_next(state)) != ']')
  {
    if (type == JSON_TYPE_OBJECT)
    {
      if (ptr_type == GROUP_TYPE)
      {
        void* ptr = construct[ptr_type]();
        printf("new object is at %p\n", ptr);
	parseObject(state, ptr, ptr_type, ptr_type);
	if (obj_type == CONTAINERROOT_TYPE && ptr_type == GROUP_TYPE)
	{
	  ((ContainerRoot*)o)->VT->containerRootAddGroups(o, ptr);
	}
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
