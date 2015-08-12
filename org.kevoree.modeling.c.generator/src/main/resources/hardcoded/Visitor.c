#include "KMFContainer.h"
#include "Visitor.h"

#include <stddef.h>
#include <stdbool.h>
#include <stdio.h>
#include <string.h>
#include <stdlib.h>

static void _destroy(Visitor* this)
{
  if (NULL != this) {
    free(this);
    this = NULL;
  }
}

Visitor * visitor_new(void* impl, int (*visit)( Visitor*, char*, void*))
{
  Visitor * this;
  this = (Visitor *) calloc(1, sizeof(*this));
  this->visit = visit;
  this->destroy = _destroy;
  this->impl = impl;
  return this;
}
