cmake_minimum_required(VERSION 2.8)
project(kevoree-c-model)

include_directories(.)

set(SRC Channel.c ChannelType.c ComponentInstance.c ComponentType.c ContainerNode.c ContainerRoot.c DeployUnit.c DictionaryAttribute.c Dictionary.c DictionaryType.c DictionaryValue.c FragmentDictionary.c Group.c GroupType.c hashmap.c Instance.c jsonparse.c jsontree.c KMFContainer.c main.c MBinding.c MessagePortType.c NamedElement.c _Namespace.c NetworkInfo.c NetworkProperty.c NodeLink.c NodeNetwork.c NodeType.c Operation.c Parameter.c Port.c PortType.c PortTypeMapping.c PortTypeRef.c Repository.c ServicePortType.c tools.c TypeDefinition.c TypedElement.c TypeLibrary.c Visitor.c)
set(CMAKE_CXX_FLAGS "-std=c11 -m32 -g -O0 ${CMAKE_CXX_FLAGS}")
set(CMAKE_C_FLAGS "-std=c11 -m32 -g -D_BSD_SOURCE" ${CMAKE_C_FLAGS})

add_executable(main ${SRC})
