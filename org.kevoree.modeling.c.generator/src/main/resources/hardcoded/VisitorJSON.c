#include "VisitorJSON.h"
#include "ContainerRoot.h"



static int _ContainerRoot(VisitorJSON * this, ContainerRoot* o)
{
  printf("Class is: %s\n", o->VT->internalGetKey(o));
  return 32; // yay magic number again
}

static int _visit(Visitor* this, char* meta, void* visited)
{
  if (strcmp(meta, "ContainerRoot") == 0)
  {
    return _ContainerRoot(this->impl, (ContainerRoot*)visited);
  } else
  {
    printf("nothing to do here");
  }
}


static void _destroy(VisitorJSON* this)
{
  if (NULL != this) {
    this->visitor->destroy(this->visitor);
    free(this);
    this = NULL;
  }
}

VisitorJSON * visitor_json_new()
{
  VisitorJSON * this;
  this = (VisitorJSON *) calloc(1, sizeof(*this));
  this->visitor = visitor_new(this , _visit);
  this->destroy = _destroy;
  return this;
}
